package org.knowhowlab.maven.plugins.keepass;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author dpishchukhin.
 */
public class Record {
    @Parameter(required = true)
    private String prefix;

    @Parameter(required = false)
    private String group;

    @Parameter(required = true)
    private String title;

    public String getPrefix() {
        return prefix;
    }

    public String getGroup() {
        return group;
    }

    public String getTitle() {
        return title;
    }
}
