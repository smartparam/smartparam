/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.engine.core.output;

import java.util.*;

import org.smartparam.engine.core.repository.RepositoryName;


/**
 *
 * @author Adam Dubiel
 */
public final class RawParamValueBuilder {

    private final List<MultiValue> rows = new ArrayList<MultiValue>();

    private final Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private RepositoryName sourceRepository = RepositoryName.from("default-test-repo");

    private RawParamValueBuilder() {
    }

    public static RawParamValueBuilder rawParamValue() {
        return new RawParamValueBuilder();
    }

    public static ParamValue rawParamValue(Object value) {
        return new RawParamValueBuilder().returning(value).build();
    }

    public static ParamValue rawParamValue(Object... singleRowValues) {
        return new RawParamValueBuilder().withRow(singleRowValues).build();
    }

    public ParamValue build() {
        return new DefaultParamValue(rows, sourceRepository);
    }

    public RawParamValueBuilder withNamedLevels(String... levelNames) {
        for (int index = 0; index < levelNames.length; ++index) {
            indexMap.put(levelNames[index], index);
        }
        return this;
    }

    public RawParamValueBuilder returning(Object value) {
        return withRow(value);
    }

    public RawParamValueBuilder withRow(Object... rowValues) {
        rows.add(new DefaultMultiValue(rowValues, indexMap));
        return this;
    }

    public RawParamValueBuilder from(String sourceRepositoryName) {
        this.sourceRepository = RepositoryName.from(sourceRepositoryName);
        return this;
    }
}
