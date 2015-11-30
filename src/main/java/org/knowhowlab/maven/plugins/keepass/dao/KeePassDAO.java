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

import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.KeePassFile;
import de.slackspace.openkeepass.exception.KeePassDatabaseUnreadable;

import java.io.File;
import java.util.List;

/**
 * @author dpishchukhin.
 */
public class KeePassDAO {
    private final KeePassDatabase keePassDatabase;
    private KeePassFile keePassFile;

    public KeePassDAO(File file) {
        keePassDatabase = KeePassDatabase.getInstance(file);
    }

    public KeePassDAO open(String password) {
        try {
            keePassFile = keePassDatabase.openDatabase(password);
            return this;
        } catch (KeePassDatabaseUnreadable e) {
            throw new IllegalArgumentException("Invalid password?", e);
        }
    }

    @Deprecated
    public KeePassGroup getRootGroup() {
        return new KeePassGroup(keePassFile.getRoot());
    }

    @Deprecated
    public KeePassGroup findGroup(KeePassGroup parent, String name) {
        List<KeePassGroup> groups = parent.getGroups();
        for (KeePassGroup group : groups) {
            if (group.getName().equals(name)) {
                return group;
            }
            KeePassGroup found = findGroup(group, name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Deprecated
    public KeePassEntry findEntry(KeePassGroup parent, String title) {
        List<KeePassEntry> entries = parent.getEntries();
        for (KeePassEntry entry : entries) {
            if (entry.getTitle().equals(title)) {
                return entry;
            }
        }
        return null;    }

    public KeePassGroup getGroup(String uuid) {
        return new UUIDFinder(getRootGroup()).find(uuid);
    }
}
