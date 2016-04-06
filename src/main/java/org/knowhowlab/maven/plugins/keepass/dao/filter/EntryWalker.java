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

package org.knowhowlab.maven.plugins.keepass.dao.filter;

import org.knowhowlab.maven.plugins.keepass.dao.KeePassEntry;
import org.knowhowlab.maven.plugins.keepass.dao.KeePassGroup;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * @author dpishchukhin.
 */
public class EntryWalker implements DataWalker<KeePassEntry> {
    private KeePassGroup rootGroup;

    public EntryWalker(KeePassGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public KeePassEntry findAny(Filter<KeePassEntry> filter) {
        KeePassEntry entry = findEntry(rootGroup, filter);
        if (entry != null) return entry;
        throw new IllegalArgumentException(format("Invalid filter: %s", filter));
    }

    private KeePassEntry findEntry(KeePassGroup searchGroup, Filter<KeePassEntry> filter) {
        List<KeePassEntry> entries = searchGroup.getEntries();
        for (KeePassEntry entry : entries) {
            if (filter.matches(entry)) {
                return entry;
            }
        }
        List<KeePassGroup> groups = searchGroup.getGroups();
        for (KeePassGroup group : groups) {
            KeePassEntry entry = findEntry(group, filter);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }

    public List<KeePassEntry> findAll(Filter<KeePassEntry> filter) {
        List<KeePassEntry> result = findEntries(rootGroup, filter, new ArrayList<KeePassEntry>());
        if (!result.isEmpty()) return result;
        throw new IllegalArgumentException(format("Invalid filter: %s", filter));
    }

    private List<KeePassEntry> findEntries(KeePassGroup searchGroup, Filter<KeePassEntry> filter, ArrayList<KeePassEntry> list) {
        List<KeePassEntry> entries = searchGroup.getEntries();
        for (KeePassEntry entry : entries) {
            if (filter.matches(entry)) {
                list.add(entry);
            }
        }
        List<KeePassGroup> groups = searchGroup.getGroups();
        for (KeePassGroup group : groups) {
            findEntries(group, filter, list);
        }
        return list;
    }
}
