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
import java.util.List;

import static org.apache.maven.plugins.annotations.LifecyclePhase.VALIDATE;

/**
 * @author dpishchukhin.
 */
@Mojo(name = "read", defaultPhase = VALIDATE)
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
    private Record record;

    public void execute() throws MojoExecutionException, MojoFailureException {
        KeePassFile keePassFile = KeePassDatabase.getInstance(file).openDatabase(password);

        Group group = keePassFile.getRoot();
        if (record.getGroup() != null) {
            group = findGroup(group, record.getGroup());
        }

        List<Entry> entries = group.getEntries();
        for (Entry entry : entries) {
            if (entry.getTitle().equals(record.getTitle())) {
                project.getProperties().setProperty(record.getPrefix() + "username", entry.getUsername());
                project.getProperties().setProperty(record.getPrefix() + "password", entry.getPassword());
                project.getProperties().setProperty(record.getPrefix() + "url", entry.getUrl());
            }
        }
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
