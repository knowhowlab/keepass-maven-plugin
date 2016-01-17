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

package org.knowhowlab.maven.plugins.keepass;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Definition of extra entry attribute
 *
 * @author dpishchukhin.
 */
public class Attribute {
    /**
     * Attribute name
     */
    @Parameter(required = true)
    private String name;

    /**
     * A new name that has to be used instead of original one. Optional.
     */
    @Parameter(required = false)
    private String mapTo;

    public String getName() {
        return name;
    }

    public String getMapTo() {
        return mapTo;
    }
}
