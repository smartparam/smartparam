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
package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.batch.ParameterBatchLoader;

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
        Assertions.assertThat(actual.getMetadata().getName()).isEqualTo(parameterName);
        return this;
    }

    public ParameterBatchLoaderAssert hasMetadataWithLevels(int levelCount) {
        Assertions.assertThat(actual.getMetadata().getLevels()).hasSize(levelCount);
        return this;
    }

    public ParameterBatchLoaderAssert hasEntryLoader() {
        Assertions.assertThat(actual.getEntryLoader()).isNotNull();
        return this;
    }
}
