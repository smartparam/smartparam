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
package org.smartparam.engine.core.parameter.batch;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.parameter.batch.ParameterBatchLoader;
import org.smartparam.engine.test.ParamEngineAssertions;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterBatchLoaderAssert extends AbstractAssert<ParameterBatchLoaderAssert, ParameterBatchLoader> {

    private ParameterBatchLoaderAssert(ParameterBatchLoader actual) {
        super(actual, ParameterBatchLoaderAssert.class);
    }

    public static ParameterBatchLoaderAssert assertThat(ParameterBatchLoader actual) {
        return new ParameterBatchLoaderAssert(actual);
    }

    public ParameterBatchLoaderAssert hasMetadataFor(String parameterName) {
        ParamEngineAssertions.assertThat(actual.getMetadata().getName()).isEqualTo(parameterName);
        return this;
    }

    public ParameterBatchLoaderAssert hasMetadataWithLevels(int levelCount) {
        ParamEngineAssertions.assertThat(actual.getMetadata().getLevels()).hasSize(levelCount);
        return this;
    }

    public ParameterBatchLoaderAssert hasEntryLoader() {
        ParamEngineAssertions.assertThat(actual.getEntryLoader()).isNotNull();
        return this;
    }
}
