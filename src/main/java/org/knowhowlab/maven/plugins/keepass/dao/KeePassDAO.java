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

import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.KeePassFile;
import de.slackspace.openkeepass.exception.KeePassDatabaseUnreadable;
import org.knowhowlab.maven.plugins.keepass.dao.filter.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;

/**
 * @author dpishchukhin.
 */
public class KeePassDAO {
    private final KeePassDatabase keePassDatabase;
    private KeePassFile keePassFile;

    public KeePassDAO(File file) {
        keePassDatabase = KeePassDatabase.getInstance(file);
    }

    public static UUID convertToUUID(String digits) {
        if (!digits.contains("-")) {
            return fromString(digits.replaceAll(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5"));
        } else {
            return fromString(digits);
        }
    }

    public KeePassDAO open(String password) {
        try {
            keePassFile = keePassDatabase.openDatabase(password);
            return this;
        } catch (KeePassDatabaseUnreadable e) {
            throw new IllegalArgumentException("Invalid password?", e);
        }
    }

    public KeePassDAO open(String password, File keyFile) {
        try {
            keePassFile = keePassDatabase.openDatabase(password, keyFile);
            return this;
        } catch (KeePassDatabaseUnreadable e) {
            throw new IllegalArgumentException("Invalid password and/or key file?", e);
        }
    }

    public KeePassDAO open(File keyFile) {
        try {
            keePassFile = keePassDatabase.openDatabase(keyFile);
            return this;
        } catch (KeePassDatabaseUnreadable e) {
            throw new IllegalArgumentException("Invalid key file?", e);
        }
    }

    public KeePassGroup getRootGroup() {
        return new KeePassGroup(keePassFile.getRoot());
    }

    public KeePassGroup getGroup(UUID uuid) {
        return new GroupWalker(getRootGroup()).findAny(new GroupUUIDFilter(uuid));
    }

    public KeePassEntry getEntry(UUID uuid) {
        return new KeePassEntry(keePassFile.getEntryByUUID(uuid));
    }

    public List<KeePassGroup> getGroupsByName(String name) {
        return new GroupWalker(getRootGroup()).findAll(new GroupNameFilter(name));
    }

    public List<KeePassEntry> getEntriesByTitle(String title) {
        return getEntriesByTitle(getRootGroup(), title);
    }

    public List<KeePassEntry> getEntriesByTitle(KeePassGroup group, String title) {
        return new EntryWalker(group).findAll(new EntryTitleFilter(title));
    }

    public List<KeePassEntry> getEntriesByTitleRegex(String regex) {
        return getEntriesByTitleRegex(getRootGroup(), regex);
    }

    public List<KeePassEntry> getEntriesByTitleRegex(KeePassGroup group, String regex) {
        return new EntryWalker(group).findAll(new EntryTitleRegexFilter(regex));
    }

    public List<KeePassGroup> getGroupsByNameRegex(String regex) {
        return new GroupWalker(getRootGroup()).findAll(new GroupNameRegexFilter(regex));
    }

    public List<KeePassGroup> getGroupsByPath(String path) {
        return new GroupWalker(getRootGroup()).findAll(new GroupPathFilter(path.split("/")));
    }
}
