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

import org.knowhowlab.maven.plugins.keepass.dao.KeePassEntry;

import java.util.UUID;

/**
 * @author dpishchukhin.
 */
public class EntryUUIDFilter implements Filter<KeePassEntry> {
    private UUID uuid;

    public EntryUUIDFilter(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean matches(KeePassEntry item) {
        return uuid.equals(item.getUuid());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntryUUIDFilter{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
