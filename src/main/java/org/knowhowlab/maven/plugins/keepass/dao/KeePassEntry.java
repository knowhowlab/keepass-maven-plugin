/*
 * Copyright (c) 2010-2016 Dmytro Pishchukhin (http://knowhowlab.org)
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

package org.knowhowlab.maven.plugins.keepass.dao;

import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author dpishchukhin.
 */
public class KeePassEntry {
    private Entry entry;

    public KeePassEntry(Entry entry) {
        this.entry = entry;
    }

    public String getTitle() {
        return entry.getTitle();
    }

    public UUID getUuid() {
        return entry.getUuid();
    }

    public String getUsername() {
        return entry.getUsername();
    }

    public String getPassword() {
        return entry.getPassword();
    }

    public String getUrl() {
        return entry.getUrl();
    }

    public List<KeePassProperty> getProperties() {
        List<KeePassProperty> properties = new ArrayList<KeePassProperty>(entry.getProperties().size());
        for (Property property : entry.getProperties()) {
            properties.add(new KeePassProperty(property));
        }
        return properties;
    }

    public KeePassProperty getPropertyByName(String name) {
        return new KeePassProperty(entry.getPropertyByName(name));
    }
}
