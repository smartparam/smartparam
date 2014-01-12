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
package org.smartparam.engine.core;

import org.assertj.core.api.AbstractAssert;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.test.ParamEngineAssertions;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEngineRuntimeConfigAssert extends AbstractAssert<ParamEngineRuntimeConfigAssert, ParamEngineRuntimeConfig> {

    private ParamEngineRuntimeConfigAssert(ParamEngineRuntimeConfig actual) {
        super(actual, ParamEngineRuntimeConfigAssert.class);
    }

    public static ParamEngineRuntimeConfigAssert assertThat(ParamEngineRuntimeConfig actual) {
        return new ParamEngineRuntimeConfigAssert(actual);
    }

    public ParamEngineRuntimeConfigAssert hasFunctionCache() {
        ParamEngineAssertions.assertThat(actual.getFunctionCache()).isNotNull();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasFunctionCache(Class<?> ofClass) {
        ParamEngineAssertions.assertThat(actual.getFunctionCache()).isInstanceOf(ofClass);
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasParamCache() {
        ParamEngineAssertions.assertThat(actual.getParamCache()).isNotNull();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasFunctionRepositories() {
        ParamEngineAssertions.assertThat(actual.getFunctionRepositories()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasParamRepositories() {
        ParamEngineAssertions.assertThat(actual.getParamRepositories()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasInvokers() {
        ParamEngineAssertions.assertThat(actual.getInvokers()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasTypes() {
        ParamEngineAssertions.assertThat(actual.getTypes()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasMachers() {
        ParamEngineAssertions.assertThat(actual.getMatchers()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasRepository(ParamRepository repository) {
        ParamEngineAssertions.assertThat(actual.getParamRepositories()).contains(repository);
        return this;
    }
}
