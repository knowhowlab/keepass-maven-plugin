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

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static java.lang.System.getProperty;

/**
 * @author dpishchukhin
 */
public class JceWorkaround {
    public static void apply() throws Exception {
        if (isJavaRequiresJCE()) {
            try {
                JceWorkaround.setJceSecurityUnrestricted();
            } catch (Exception e) {
                JceWorkaround.setJceSecurityUnrestricted(JceWorkaround.getUnsafe());
            }
        }
    }

    static boolean isJavaRequiresJCE() {
        String javaVersion = getProperty("java.specification.version");
        return "1.7".equals(javaVersion) || "1.8".equals(javaVersion) || "1.9".equals(javaVersion);
    }

    static void setJceSecurityUnrestricted(Unsafe unsafe) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        unsafe.putBoolean(Class.forName("javax.crypto.JceSecurity"), unsafe.staticFieldOffset(field), false);
    }

    static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    static void setJceSecurityUnrestricted() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        field.setAccessible(true);
        field.setBoolean(null, false);
    }
}
