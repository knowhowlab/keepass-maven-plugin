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

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author dpishchukhin.
 */
public class KeePassDAOWithKeyTest {
    private File dbFile;
    private File dbOnlyWithKeyFile;
    private File keyFile;

    @Before
    public void setUp() throws Exception {
        dbFile = new File("./src/test/resources/test-with-key.kdbx");
        dbOnlyWithKeyFile = new File("./src/test/resources/test-only-with-key.kdbx");
        keyFile = new File("./src/test/resources/keyfile.key");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpen_invalidPassword() throws Exception {
        new KeePassDAO(dbFile).open("testpass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpen_invalidPassword2() throws Exception {
        new KeePassDAO(dbOnlyWithKeyFile).open("testpass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpen_invalidKey() throws Exception {
        new KeePassDAO(dbFile).open(new File("wrong-key-file.key"));
    }

    @Test
    public void testOpen() throws Exception {
        assertThat(new KeePassDAO(dbFile).open("test123", keyFile), notNullValue());
    }

    @Test
    public void testOpen2() throws Exception {
        assertThat(new KeePassDAO(dbOnlyWithKeyFile).open(keyFile), notNullValue());
    }
}