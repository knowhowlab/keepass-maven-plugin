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

import java.util.List;

/**
 * @author dpishchukhin.
 */
abstract class KeePassGroupFinder {
    private KeePassGroup rootGroup;

    public KeePassGroupFinder(KeePassGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public KeePassGroup find(String filter) {
        KeePassGroup group = findGroup(rootGroup, filter);
        if (group != null) return group;
        throw new IllegalArgumentException(String.format("Invalid filter: %s - %s", getFilterName(), filter));
    }

    private KeePassGroup findGroup(KeePassGroup searchGroup, String filter) {
        if (matches(searchGroup, filter)) {
            return searchGroup;
        }
        List<KeePassGroup> groups = searchGroup.getGroups();
        for (KeePassGroup group : groups) {
            if (matches(group, filter)) {
                return group;
            } else {
                KeePassGroup child = findGroup(group, filter);
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }

    protected abstract boolean matches(KeePassGroup group, String filter);

    protected abstract String getFilterName();
}
