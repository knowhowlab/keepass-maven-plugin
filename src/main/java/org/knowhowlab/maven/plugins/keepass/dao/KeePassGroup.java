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

package org.knowhowlab.maven.plugins.keepass.dao;

import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dpishchukhin.
 */
public class KeePassGroup {
    private Group group;

    KeePassGroup(Group group) {
        this.group = group;
    }
    
    public String getName() {
        return group.getName();
    }

    public String getUuid() {
        return group.getUuid();
    }

    public List<KeePassEntry> getEntries() {
        List<KeePassEntry> entries = new ArrayList<KeePassEntry>(group.getEntries().size());
        for (Entry entry : group.getEntries()) {
            entries.add(new KeePassEntry(entry));
        }
        return entries;
    }

    public List<KeePassGroup> getGroups() {
        List<KeePassGroup> groups = new ArrayList<KeePassGroup>(group.getGroups().size());
        for (Group group : this.group.getGroups()) {
            groups.add(new KeePassGroup(group));
        }
        return groups;
    }
}
