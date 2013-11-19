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
package org.smartparam.engine.editor;

import java.util.Arrays;
import org.smartparam.engine.util.EngineUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntriesFilter {

    private final int page;

    private final int pageSize;

    private final String[] levelFilters;

    public ParameterEntriesFilter(int page, int pageSize, String... levelFilters) {
        this.page = page;
        this.pageSize = pageSize;
        this.levelFilters = levelFilters;
    }

    public int page() {
        return page;
    }

    public int pageSize() {
        return pageSize;
    }

    public int offset() {
        return page * pageSize;
    }

    public String[] levelFilters() {
        return Arrays.copyOf(levelFilters, levelFilters.length);
    }

    public String levelFilter(int levelIndex) {
        return levelFilters[levelIndex];
    }

    public boolean hasFilter(int levelIndex) {
        return EngineUtil.hasText(levelFilters[levelIndex]);
    }

    public int levelFiltersLength() {
        return levelFilters.length;
    }
}
