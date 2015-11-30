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

package org.knowhowlab.maven.plugins.keepass.dao.filter;

import org.knowhowlab.maven.plugins.keepass.dao.KeePassGroup;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * @author dpishchukhin.
 */
public class GroupWalker implements DataWalker<KeePassGroup> {
    private KeePassGroup rootGroup;

    public GroupWalker(KeePassGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public KeePassGroup findAny(Filter<KeePassGroup> filter) {
        KeePassGroup group = findGroup(rootGroup, filter);
        if (group != null) return group;
        throw new IllegalArgumentException(format("Invalid filter: %s", filter));
    }

    private KeePassGroup findGroup(KeePassGroup searchGroup, Filter<KeePassGroup> filter) {
        if (filter.matches(searchGroup)) {
            return searchGroup;
        }
        List<KeePassGroup> groups = searchGroup.getGroups();
        for (KeePassGroup group : groups) {
            if (filter.matches(group)) {
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

    public List<KeePassGroup> findAll(Filter<KeePassGroup> filter) {
        List<KeePassGroup> result = findGroups(rootGroup, filter, new ArrayList<KeePassGroup>());
        if (!result.isEmpty()) return result;
        throw new IllegalArgumentException(format("Invalid filter: %s", filter));
    }

    public List<KeePassGroup> findAll(FilterWithIndex<KeePassGroup> filter) {
        List<KeePassGroup> result = findGroups(rootGroup, 0, filter, new ArrayList<KeePassGroup>());
        if (!result.isEmpty()) return result;
        throw new IllegalArgumentException(format("Invalid filter: %s", filter));
    }

    private List<KeePassGroup> findGroups(KeePassGroup searchGroup, Filter<KeePassGroup> filter, List<KeePassGroup> list) {
        if (filter.matches(searchGroup)) {
            list.add(searchGroup);
        }
        List<KeePassGroup> groups = searchGroup.getGroups();
        for (KeePassGroup group : groups) {
            findGroups(group, filter, list);
        }
        return list;
    }

    private List<KeePassGroup> findGroups(KeePassGroup searchGroup, int index,
                                          FilterWithIndex<KeePassGroup> filter, List<KeePassGroup> list) {
        if (!filter.matches(searchGroup, index)) {
            return list;
        } else if (filter.isLastIndex(index)) {
            list.add(searchGroup);
        }
        List<KeePassGroup> groups = searchGroup.getGroups();
        for (KeePassGroup group : groups) {
            findGroups(group, index + 1, filter, list);
        }
        return list;
    }
}
