/*
 * Copyright (c) 2010-2015 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.knowhowlab.maven.plugins.keepass;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.knowhowlab.maven.plugins.keepass.dao.KeePassDAO;
import org.knowhowlab.maven.plugins.keepass.dao.KeePassEntry;
import org.knowhowlab.maven.plugins.keepass.dao.KeePassGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.apache.maven.plugins.annotations.LifecyclePhase.VALIDATE;

/**
 * @author dpishchukhin.
 */
@Mojo(name = "read", defaultPhase = VALIDATE, threadSafe = true)
public class ReadMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Location of the file.
     */
    @Parameter(property = "keepass.file", required = true)
    private File file;

    /**
     * File password
     */
    @Parameter(property = "keepass.password", required = true)
    private String password;

    /**
     * Properties
     */
    @Parameter(required = true)
    private List<Record> records = new ArrayList<Record>();

    /**
     * JCE Workaround
     */
    @Parameter(property = "keepass.jce-workaround", required = false, defaultValue = "false")
    private boolean jceWorkaround;

    /**
     * Ignore entry duplicates
     */
    @Parameter(property = "keepass.ignore-duplicates", required = false, defaultValue = "false")
    private boolean ignoreDuplicates;


    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isJavaRequiresJCE() && jceWorkaround) {
            applyJCEWorkaround();
        }

        KeePassDAO dao = new KeePassDAO(file);

        try {
            dao.open(password);
            getLog().info(format("KeePass file is open: %s", file.getAbsolutePath()));
        } catch (Exception e) {
            getLog().error(format("Unable to open file: %s", file.getAbsolutePath()), e);
            throw new MojoFailureException(format("Unable to open file: %s", file.getAbsolutePath()));
        }

        for (Record record : records) {
            handleRecord(dao, record);
        }
    }

    private boolean isJavaRequiresJCE() {
        String javaVersion = getProperty("java.specification.version");
        return "1.7".equals(javaVersion) || "1.8".equals(javaVersion);
    }

    private void applyJCEWorkaround() {
        getLog().debug("Applying JCE workaround...");
        try {
            java.lang.reflect.Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, Boolean.FALSE);

            getLog().info("JCE workaround is applied.");
        } catch (Exception e) {
            getLog().warn("Unable to apply JCE workaround. Try to install JCE.", e);
        }
    }

    private void handleRecord(KeePassDAO dao, Record record) throws MojoFailureException {
        KeePassGroup group = dao.getRootGroup();
        if (record.getGroup() != null) {
            group = dao.findGroup(group, record.getGroup());
            if (group == null) {
                getLog().error(format("Group name: %s is unknown", record.getGroup()));
                throw new MojoFailureException(format("Group name: %s is unknown", record.getGroup()));
            }
        }

        KeePassEntry entry;

        List<KeePassEntry> entries = findEntries(dao, group, record.getEntry());
        if (entries == null || entries.isEmpty()) {
            getLog().error(format("Entry: %s is unknown", record.getEntry()));
            throw new MojoFailureException(format("Entry: %s is unknown", record.getEntry()));
        } else if (entries.size() > 1) {
            if (ignoreDuplicates) {
                entry = entries.get(0);
                getLog().warn(format("Duplicates found. Select Entry with UUID: %s", entry.getUuid()));
            } else {
                getLog().error(format("Entry duplication: %s", record.getEntry()));
                throw new MojoFailureException(format("Entry duplication: %s", record.getEntry()));
            }
        } else {
            entry = entries.get(0);
        }

        getLog().info(format("Entry with UUID: %s is found", entry.getUuid()));

        project.getProperties().setProperty(record.getPrefix() + "username", entry.getUsername());
        project.getProperties().setProperty(record.getPrefix() + "password", entry.getPassword());
        project.getProperties().setProperty(record.getPrefix() + "url", entry.getUrl());
    }

    private List<KeePassEntry> findEntries(KeePassDAO dao, KeePassGroup group, String entryFilter) throws MojoFailureException {
        ArrayList<KeePassEntry> result = new ArrayList<KeePassEntry>();

        String[] filterFields = entryFilter.split(":", 2);

        EntryFilterType filterType;
        String filterData;
        if (filterFields.length == 1) {
            filterType = EntryFilterType.title;
            filterData = filterFields[0];
            getLog().warn(format("Entry filter type is missed for entry: %s. Use it as title", filterFields[0]));
        } else {
            try {
                filterType = EntryFilterType.valueOf(filterFields[0].toLowerCase());
            } catch (IllegalArgumentException e) {
                getLog().error(format("Unknown Entry filter type: %s", filterFields[0].toLowerCase()));
                throw new MojoFailureException(format("Unknown Entry filter type: %s", filterFields[0].toLowerCase()));
            }
            filterData = filterFields[1];
        }

        try {
            switch (filterType) {
                case title:
                    result.addAll(dao.getEntriesByTitle(group, filterData));
                    break;
                case regex:
                    result.addAll(dao.getEntriesByTitleRegex(group, filterData));
                    break;
                case uuid:
                    result.add(dao.getEntry(KeePassDAO.convertToUUID(filterData)));
                    break;
            }
        } catch (Exception e) {
            getLog().error(format("Unable to find entry by filter: %s", entryFilter), e);
            throw new MojoFailureException(format("Unable to find entry by filter: %s", entryFilter));
        }
        return result;
    }

    private enum EntryFilterType {
        title, regex, uuid
    }
}
