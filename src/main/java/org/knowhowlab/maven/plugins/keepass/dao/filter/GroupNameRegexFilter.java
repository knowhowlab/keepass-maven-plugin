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

import org.knowhowlab.maven.plugins.keepass.dao.KeePassGroup;

/**
 * @author dpishchukhin.
 */
public class GroupNameRegexFilter implements Filter<KeePassGroup> {
    private String regex;

    public GroupNameRegexFilter(String regex) {
        this.regex = regex;
    }

    public boolean matches(KeePassGroup item) {
        return item.getName() != null && item.getName().matches(regex);
    }
}
