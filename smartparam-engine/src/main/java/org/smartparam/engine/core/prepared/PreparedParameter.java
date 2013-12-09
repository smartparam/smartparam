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
package org.smartparam.engine.core.prepared;

import java.util.Collections;
import java.util.HashMap;
import org.smartparam.engine.core.index.LevelIndex;

import java.util.Map;
import org.smartparam.engine.core.parameter.Parameter;

/**
 * Compiled parameter object, which contains all resolved references and
 * only necessary fields. It also contains prepared LevelIndex for quick search.
 *
 * @see org.smartparam.engine.model.Parameter
 * @see org.smartparam.engine.model.ParameterEntry
 * @see org.smartparam.engine.model.Level
 * @see LevelIndex
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedParameter {

    /**
     * Unique name of parameter.
     */
    private final String name;

    /**
     * Prepared (compiled) levels.
     */
    private final PreparedLevel[] levels;

    /**
     * Search index built for this parameter.
     */
    private LevelIndex<PreparedEntry> index;

    /**
     * Number of input (criteria) levels.
     * Zero means this is no-criteria parameter.
     */
    private final int inputLevelsCount;

    private final boolean nullable;

    private final boolean cacheable;

    /**
     * Prepared mapping: level name to level index.
     */
    private final Map<String, Integer> levelNameMap = new HashMap<String, Integer>();

    private final char arraySeparator;

    public PreparedParameter(Parameter parameter, PreparedLevel[] levels) {
        this.name = parameter.getName();
        this.inputLevelsCount = parameter.getInputLevels();
        this.nullable = parameter.isNullable();
        this.cacheable = parameter.isCacheable();
        this.arraySeparator = parameter.getArraySeparator();
        this.levels = levels;
    }

    /**
     * Returns prepared search index.
     *
     * @return search index
     */
    public LevelIndex<PreparedEntry> getIndex() {
        return index;
    }

    public void setIndex(LevelIndex<PreparedEntry> index) {
        this.index = index;
    }

    public PreparedLevel[] getLevels() {
        return levels;
    }

    public String getName() {
        return name;
    }

    public int getLevelCount() {
        return levels != null ? levels.length : 0;
    }

    /**
     * Returns number of input (criteria) levels.
     *
     * @see #getLevelCount()
     *
     * @return number of input levels (k)
     */
    public int getInputLevelsCount() {
        return inputLevelsCount;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public boolean isNotNull() {
        return !isNullable();
    }

    public char getArraySeparator() {
        return arraySeparator;
    }

    public Map<String, Integer> getLevelNameMap() {
        return Collections.unmodifiableMap(levelNameMap);
    }

    public void setLevelNameMap(Map<String, Integer> levelNameMap) {
        this.levelNameMap.putAll(levelNameMap);
    }

    public PreparedLevel getOutputLevel(int k) {
        return levels[inputLevelsCount + k - 1];
    }
}
