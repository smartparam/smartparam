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
package org.smartparam.editor.viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.smartparam.engine.util.EngineUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntriesFilter {

    private int page = -1;

    private int pageSize = -1;

    private String[] levelFilters = new String[]{};

    private final List<LevelSorting> levelSorting = new ArrayList<LevelSorting>();

    public ParameterEntriesFilter() {
    }

    public ParameterEntriesFilter(int page, int pageSize, String[] levelFilters, List<LevelSorting> levelSorting) {
        this.page = page;
        this.pageSize = pageSize;
        this.levelFilters = Arrays.copyOf(levelFilters, levelFilters.length);
        this.levelSorting.addAll(levelSorting);
    }

    public ParameterEntriesFilter(int page, int pageSize, String[] levelFilters, LevelSorting levelSorting) {
        this(page, pageSize, levelFilters, Arrays.asList(levelSorting));
    }

    public ParameterEntriesFilter(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int page() {
        return page;
    }

    public ParameterEntriesFilter withPage(int page) {
        this.page = page;
        return this;
    }

    public boolean applyPaging() {
        return page > -1;
    }

    public boolean applyLimits() {
        return pageSize > -1;
    }

    public int pageSize() {
        return pageSize;
    }

    public ParameterEntriesFilter withPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int offset() {
        return page * pageSize;
    }

    public String[] levelFilters() {
        return Arrays.copyOf(levelFilters, levelFilters.length);
    }

    public ParameterEntriesFilter filterBy(String... levelFilters) {
        this.levelFilters = Arrays.copyOf(levelFilters, levelFilters.length);
        return this;
    }

    public String levelFilter(int levelIndex) {
        return levelFilters[levelIndex];
    }

    public boolean hasFilter(int levelIndex) {
        return levelIndex < levelFiltersLength() && EngineUtil.hasText(levelFilters[levelIndex]);
    }

    public int levelFiltersLength() {
        return levelFilters.length;
    }

    public boolean applyOrdering() {
        return !levelSorting.isEmpty();
    }

    public List<LevelSorting> sorting() {
        return Collections.unmodifiableList(levelSorting);
    }

    public ParameterEntriesFilter orderBy(LevelSorting levelSorting) {
        this.levelSorting.add(levelSorting);
        return this;
    }

    public ParameterEntriesFilter orderBy(int orderByLevelIndex) {
        levelSorting.add(new LevelSorting(orderByLevelIndex));
        return this;
    }

    public ParameterEntriesFilter orderBy(int orderByLevelIndex, SortDirection orderDirection) {
        levelSorting.add(new LevelSorting(orderByLevelIndex, orderDirection));
        return this;
    }
}
