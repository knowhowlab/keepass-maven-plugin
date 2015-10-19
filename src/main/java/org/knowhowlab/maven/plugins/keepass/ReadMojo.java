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

import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.Group;
import de.slackspace.openkeepass.domain.KeePassFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
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
    @Parameter(property = "file", required = true)
    private File file;

    /**
     * File password
     */
    @Parameter(property = "password", required = true)
    private String password;

    /**
     * Properties
     */
    @Parameter(required = true)
    private List<Record> records = new ArrayList<Record>();

    public void execute() throws MojoExecutionException, MojoFailureException {
        KeePassFile keePassFile;
        try {
            keePassFile = KeePassDatabase.getInstance(file).openDatabase(password);
        } catch (Exception e) {
            getLog().error(format("Unable to open file: %s", file.getAbsolutePath()));
            throw new MojoFailureException(format("Unable to open file: %s", file.getAbsolutePath()));
        }

        for (Record record : records) {
            handleRecord(keePassFile, record);
        }
    }

    private void handleRecord(KeePassFile keePassFile, Record record) throws MojoFailureException {
        Group group = keePassFile.getRoot();
        if (record.getGroup() != null) {
            group = findGroup(group, record.getGroup());
            if (group == null) {
                getLog().error(format("Group name: %s is unknown", record.getGroup()));
                throw new MojoFailureException(format("Group name: %s is unknown", record.getGroup()));
            }
        }

        Entry entry = findEntry(group, record.getTitle());
        if (entry == null) {
            getLog().error(format("Entry title: %s is unknown", record.getTitle()));
            throw new MojoFailureException(format("Entry title: %s is unknown", record.getTitle()));
        }

        project.getProperties().setProperty(record.getPrefix() + "username", entry.getUsername());
        project.getProperties().setProperty(record.getPrefix() + "password", entry.getPassword());
        project.getProperties().setProperty(record.getPrefix() + "url", entry.getUrl());
    }

    private Entry findEntry(Group group, String title) {
        List<Entry> entries = group.getEntries();
        for (Entry entry : entries) {
            if (entry.getTitle().equals(title)) {
                return entry;
            }
        }
        return null;
    }

    private Group findGroup(Group parent, String groupName) {
        List<Group> groups = parent.getGroups();
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group;
            }
            Group found = findGroup(group, groupName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
