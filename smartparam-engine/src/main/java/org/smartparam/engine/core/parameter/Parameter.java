/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.core.parameter;

import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import java.util.List;
import java.util.Set;

/**
 * Interface for parameter that is loaded from storage and evaluated inside engine.
 * Two logical parts are metadata (attributes) and matrix (entries).
 *
 * Parameter MIGHT contain unique key, but this depends on repository implementation, might
 * be useful for auditing purposes, but this should be generally avoided (see {@link #identifyEntries()} for reasons).
 *
 * @see ParameterEntry
 * @see Level
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.0.1
 */
public interface Parameter {

    /**
     * Default value for array separator.
     */
    char DEFAULT_ARRAY_SEPARATOR = ',';

    /**
     * Returns optional repository-scope unique identifier of parameter.
     */
    ParameterKey getKey();

    /**
     * Returns unique name of parameter.
     *
     * @return parameter name
     */
    String getName();

    /**
     * Returns ordered list of levels.
     *
     * @return list of levels
     */
    List<Level> getLevels();

    /**
     * Returns number of input levels (criteria levels).
     *
     * @return number of input levels
     */
    int getInputLevels();

    /**
     * Returns set of parameter entries representing (unordered) parameter matrix.
     *
     * @return parameter matrix
     */
    Set<ParameterEntry> getEntries();

    /**
     * Whether parameter's search index is stored in cache.
     *
     * @return is cacheable
     */
    boolean isCacheable();

    /**
     * Can parameter return null-value, which means that there might be no
     * matching parameter row for given input levels values.
     *
     * @return is nullable
     */
    boolean isNullable();

    /**
     * Separator used when storing array as level value, this is set globally
     * per parameter.
     */
    char getArraySeparator();

    /**
     * Should ParamEngine keep track of unique keys of entries, might be useful if for auditing reasons
     * one needs to know the exact entry from which the result came. Defaults to false and use with caution, as
     * key itself might be larger than entry values, which leads to bigger memory consumption
     * (depending on identifier implementation it can even double the size of cached index!). Exact content and format
     * of identifier depends on repository.
     *
     * Setting cacheable to false makes all entries identifiable by default and this options has no effect (as there is
     * no cache and no memory resources to preserve).
     *
     * @see ParameterEntry#getKey()
     */
    boolean isIdentifyEntries();
}
