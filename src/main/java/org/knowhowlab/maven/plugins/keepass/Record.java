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

import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Record that should be read from a KeePass file and set to system properties
 * By default, it reads only 3 standard attributes: <code>username, password and url</code>
 *
 * @author dpishchukhin.
 */
public class Record {
    /**
     * System property prefix. e.g. <code>http.</code>
     */
    @Parameter(required = true)
    private String prefix;

    /**
     * Groups filter. It has a format: [filter-type]:[filter-data].
     * Filter types: <code>uuid, name, regex and path</code>
     */
    @Parameter(required = false)
    private String group;

    /**
     * Entries filter. It has a format [filter-type]:[filter-data].
     * Filter types: <code>uuid, title and regex</code>
     */
    @Parameter(required = true)
    private String entry;

    /**
     * List of extra attributes have to be read for the found entry.
     * @see org.knowhowlab.maven.plugins.keepass.Attribute
     */
    @Parameter(required = false)
    private List<Attribute> attributes = new ArrayList<Attribute>();

    public String getPrefix() {
        return prefix;
    }

    public String getGroup() {
        return group;
    }

    public String getEntry() {
        return entry;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
