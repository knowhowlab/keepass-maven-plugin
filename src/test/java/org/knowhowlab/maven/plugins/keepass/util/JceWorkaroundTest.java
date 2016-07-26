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

package org.knowhowlab.maven.plugins.keepass.util;

import org.junit.Test;

import java.lang.reflect.Modifier;

import static java.lang.Class.forName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 * @author dpishchukhin
 */
public class JceWorkaroundTest {
    @Test
    public void setJceSecurityUnrestricted_fieldNotFinal() throws Exception {
        assumeThat(JceWorkaround.isJavaRequiresJCE(), is(true));
        assumeThat(Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted").getModifiers() & Modifier.FINAL, is(0));

        JceWorkaround.setJceSecurityUnrestricted();

        assertThat(getFieldValue(), is(false));
    }

    @Test
    public void setJceSecurityUnrestricted_fieldIsFinal() throws Exception {
        assumeThat(JceWorkaround.isJavaRequiresJCE(), is(true));
        assumeThat(Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted").getModifiers() & Modifier.FINAL, is(not(0)));

        JceWorkaround.setJceSecurityUnrestricted(JceWorkaround.getUnsafe());

        assertThat(getFieldValue(), is(false));
    }

    private Boolean getFieldValue() throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        java.lang.reflect.Field field = forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        field.setAccessible(true);
        return (Boolean) field.get(null);
    }

}