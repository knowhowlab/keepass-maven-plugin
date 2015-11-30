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

import static de.slackspace.openkeepass.util.ByteUtils.hexStringToByteArray;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
    // find entries by title
    // find entries by regex

    // find entries by title and group
    // find entries by regex and group

    // find group by UUID
    @Test
    public void testFindGroupByUUID() throws Exception {
        String groupUuid = printBase64Binary(hexStringToByteArray("8b7e6300b873d32b8c20811b6de5f2ac"));

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
                .getGroup("123321");
    }

    // find group by path
    // find groups by name
    // find groups by name regex

    // filter properties
}