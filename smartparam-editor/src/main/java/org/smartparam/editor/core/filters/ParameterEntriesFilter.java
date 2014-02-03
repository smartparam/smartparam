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
package org.smartparam.editor.core.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntriesFilter {

    private int page = -1;

    private int pageSize = -1;

    private final Map<String, LevelFilter> levelFilters = new HashMap<String, LevelFilter>();

    private final List<LevelSorting> levelSorting = new ArrayList<LevelSorting>();

    public ParameterEntriesFilter() {
    }

    public ParameterEntriesFilter(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public static ParameterEntriesFilter empty() {
        return new ParameterEntriesFilter();
    }

    public int page() {
        return page;
    }

    public ParameterEntriesFilter selectPage(int page) {
        this.page = page;
        return this;
    }

    public int pageSize() {
        return pageSize;
    }

    public ParameterEntriesFilter withPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public boolean applyPaging() {
        return page > -1;
    }

    public boolean applyLimits() {
        return pageSize > -1;
    }

    public int offset() {
        return page * pageSize;
    }

    public ParameterEntriesFilter filter(String levelName, String value) {
        this.levelFilters.put(levelName, new LevelFilter(value, false));
        return this;
    }

    public ParameterEntriesFilter filterOrStar(String levelName, String value) {
        this.levelFilters.put(value, new LevelFilter(value, true));
        return this;
    }

    public LevelFilter levelFilter(String levelName) {
        return levelFilters.get(levelName);
    }

    public boolean hasFilter(String levelName) {
        return levelFilters.containsKey(levelName);
    }

    public boolean applySorting() {
        return !levelSorting.isEmpty();
    }

    public List<LevelSorting> sorting() {
        return Collections.unmodifiableList(levelSorting);
    }

    public ParameterEntriesFilter orderBy(String levelName, SortDirection direction) {
        this.levelSorting.add(new LevelSorting(levelName, direction));
        return this;
    }
}
