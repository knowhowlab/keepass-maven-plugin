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

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.knowhowlab.maven.plugins.keepass.dao.KeePassDAO.convertToUUID;

/**
 * @author dpishchukhin.
 */
public class KeePassDAOTest {
    private File dbFile;

    @Before
    public void setUp() throws Exception {
        dbFile = new File("./src/test/resources/testdb.kdbx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeePassDAO_null() throws Exception {
        new KeePassDAO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeePassDAO_unknownFile() throws Exception {
        new KeePassDAO(new File("/aaa/bbb/aaa"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpen_null() throws Exception {
        new KeePassDAO(dbFile).open(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpen_invalidPassword() throws Exception {
        new KeePassDAO(dbFile).open("fakepass");
    }

    @Test
    public void testOpen() throws Exception {
        assertThat(new KeePassDAO(dbFile).open("testpass"), notNullValue());
    }

    // find entry by UUID
    @Test
    public void testFindEntryByUUID() throws Exception {
        UUID entryUuid = convertToUUID("878bc61b9a16259c476564d1b82945f3");

        KeePassEntry entry = new KeePassDAO(dbFile)
                .open("testpass")
                .getEntry(entryUuid);

        assertThat(entry, notNullValue());
        assertThat(entry.getUuid(), is(entryUuid));
        assertThat(entry.getTitle(), is("Deployment"));
        assertThat(entry.getUsername(), is("test-deploy"));
        assertThat(entry.getPassword(), is("testtest"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEntryByUUID_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getEntry(fromString("123321"));
    }

    // find entries by title
    @Test
    public void testFindEntryByTitle() throws Exception {
        List<KeePassEntry> entries = new KeePassDAO(dbFile)
                .open("testpass")
                .getEntriesByTitle("Deployment");

        assertThat(entries, notNullValue());
        assertThat(entries.size(), is(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEntryByTitle_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getEntriesByTitle("Deployment123");
    }

    // find entries by regex
    @Test
    public void testFindEntryByTitleRegex() throws Exception {
        List<KeePassEntry> entries = new KeePassDAO(dbFile)
                .open("testpass")
                .getEntriesByTitleRegex("Dep[l|o]{2}yme.*");

        assertThat(entries, notNullValue());
        assertThat(entries.size(), is(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEntryByTitleRegex_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getEntriesByTitleRegex("Deployme[0-9]{2}");
    }

    // find group by UUID
    @Test
    public void testFindGroupByUUID() throws Exception {
        UUID groupUuid = convertToUUID("8b7e6300b873d32b8c20811b6de5f2ac");

        KeePassGroup group = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroup(groupUuid);

        assertThat(group, notNullValue());
        assertThat(group.getUuid(), is(groupUuid));
        assertThat(group.getName(), is("development"));
        assertThat(group.getEntries(), notNullValue());
        assertThat(group.getEntries().size(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindGroupByUUID_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getGroup(convertToUUID("123321"));
    }

    // find group by path
    @Test
    public void testFindGroupByPath() throws Exception {
        UUID groupUuid = convertToUUID("8b7e6300b873d32b8c20811b6de5f2ac");

        List<KeePassGroup> groups = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByPath("/Root/server/development");

        assertThat(groups, notNullValue());
        assertThat(groups.size(), is(1));
        assertThat(groups.get(0).getUuid(), is(groupUuid));
        assertThat(groups.get(0).getName(), is("development"));
        assertThat(groups.get(0).getEntries(), notNullValue());
        assertThat(groups.get(0).getEntries().size(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindGroupByPath_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByPath("/Root/server/staging");
    }

    // find groups by name
    @Test
    public void testFindGroupByName() throws Exception {
        List<KeePassGroup> groups = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByName("test");

        assertThat(groups, notNullValue());
        assertThat(groups.size(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindGroupByName_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByName("test123");
    }

    // find groups by name regex
    @Test
    public void testFindGroupByNameRegex() throws Exception {
        List<KeePassGroup> groups = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByNameRegex("[t|e|s]{3}t");

        assertThat(groups, notNullValue());
        assertThat(groups.size(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindGroupByNameRegex_invalidValue() throws Exception {
        new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByNameRegex("[t|e|s]{3}[0-9]?");
    }

    // find groups by name regex
    @Test
    public void testFindGroupByPath_and_EntryProperties() throws Exception {
        KeePassDAO dao = new KeePassDAO(dbFile)
                .open("testpass");

        List<KeePassGroup> groups = dao
                .getGroupsByPath("/Root/server/test");

        assertThat(groups, notNullValue());
        assertThat(groups.size(), is(1));

        List<KeePassEntry> entries = dao.getEntriesByTitle(groups.get(0), "Deployment");
        assertThat(entries, notNullValue());
        assertThat(entries.size(), is(1));
        assertThat(entries.get(0).getPropertyByName("check").getValue(), is("true"));
    }
}