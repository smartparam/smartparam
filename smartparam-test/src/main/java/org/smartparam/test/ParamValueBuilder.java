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
package org.smartparam.test;

import java.util.*;

import org.smartparam.engine.core.output.MultiValue;
import org.smartparam.engine.core.output.ParamValue;
import org.smartparam.engine.core.output.DefaultParamValue;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.type.ValueHolder;
import static org.smartparam.test.MultiValueBuilder.multiValue;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamValueBuilder {

    private final List<MultiValue> rows = new ArrayList<MultiValue>();

    private final Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private RepositoryName sourceRepository = RepositoryName.from("default-test-repo");

    private ParamValueBuilder() {
    }

    public static ParamValueBuilder paramValue() {
        return new ParamValueBuilder();
    }

    public static ParamValue paramValue(Object value) {
        return new ParamValueBuilder().returning(value).build();
    }

    public static ParamValue paramValue(ValueHolder value) {
        return new ParamValueBuilder().returning(value).build();
    }

    public static ParamValue paramValue(Object... singleRowValues) {
        return new ParamValueBuilder().withRow(singleRowValues).build();
    }

    public ParamValue build() {
        return new DefaultParamValue(rows, sourceRepository);
    }

    public ParamValueBuilder withNamedLevels(String... levelNames) {
        for (int index = 0; index < levelNames.length; ++index) {
            indexMap.put(levelNames[index], index);
        }
        return this;
    }

    public ParamValueBuilder from(String repositoryName) {
        this.sourceRepository = RepositoryName.from(repositoryName);
        return this;
    }

    public ParamValueBuilder returning(Object value) {
        return withRow(value);
    }

    public ParamValueBuilder returning(ValueHolder holder) {
        return withRow(holder);
    }

    public ParamValueBuilder withRow(Object... rowValues) {
        rows.add(multiValue().withNamedLevels(indexMap).withValues(rowValues).build());
        return this;
    }

    public ParamValueBuilder withRow(ValueHolder... rowHolders) {
        rows.add(multiValue().withNamedLevels(indexMap).withValues(rowHolders).build());
        return this;
    }
}
