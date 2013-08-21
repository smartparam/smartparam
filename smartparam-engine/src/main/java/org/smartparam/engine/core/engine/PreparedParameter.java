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
package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.index.LevelIndex;

import java.util.Map;

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
    private String name;

    /**
     * Prepared (compiled) levels.
     */
    private PreparedLevel[] levels;

    /**
     * Search index built for this parameter.
     */
    private LevelIndex<PreparedEntry> index;

    /**
     * Number of input (criteria) levels.
     * Zero means this is no-criteria parameter.
     */
    private int inputLevelsCount;

    private boolean nullable;

    private boolean cacheable;

    /**
     * Prepared mapping: level name to (1-based) level position.
     */
    private Map<String, Integer> levelNameMap;

    private char arraySeparator;

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

    public void setLevels(PreparedLevel[] levels) {
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setInputLevelsCount(int inputLevelsCount) {
        this.inputLevelsCount = inputLevelsCount;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isNotNull() {
        return !isNullable();
    }

    public char getArraySeparator() {
        return arraySeparator;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    public Map<String, Integer> getLevelNameMap() {
        return levelNameMap;
    }

    public void setLevelNameMap(Map<String, Integer> levelNameMap) {
        this.levelNameMap = levelNameMap;
    }

    public PreparedLevel getOutputLevel(int k) {
        return levels[inputLevelsCount + k - 1];
    }
}
