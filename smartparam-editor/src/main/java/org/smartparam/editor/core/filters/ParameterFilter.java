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

import org.smartparam.engine.util.EngineUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterFilter {

    private String nameFilter;

    private SortDirection sortDirection;

    public ParameterFilter(String nameFilter, SortDirection sortDirection) {
        this.nameFilter = nameFilter;
        this.sortDirection = sortDirection;
    }

    public ParameterFilter(String nameFilter) {
        this(nameFilter, SortDirection.ASC);
    }

    public ParameterFilter(SortDirection sortDirection) {
        this(null, sortDirection);
    }

    public boolean applyNameFilter() {
        return EngineUtil.hasText(nameFilter);
    }

    public String nameFilter() {
        return nameFilter;
    }

    public SortDirection sortDirection() {
        return sortDirection;
    }
}
