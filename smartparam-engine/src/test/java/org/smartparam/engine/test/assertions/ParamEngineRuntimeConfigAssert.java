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
package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.parameter.ParamRepository;

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
        Assertions.assertThat(actual.getFunctionCache()).isNotNull();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasParamCache() {
        Assertions.assertThat(actual.getParamCache()).isNotNull();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasFunctionRepositories() {
        Assertions.assertThat(actual.getFunctionRepositories()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasParamRepositories() {
        Assertions.assertThat(actual.getParamRepositories()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasInvokers() {
        Assertions.assertThat(actual.getInvokers()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasTypes() {
        Assertions.assertThat(actual.getTypes()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasMachers() {
        Assertions.assertThat(actual.getMatchers()).isNotEmpty();
        return this;
    }

    public ParamEngineRuntimeConfigAssert hasRepository(ParamRepository repository) {
        Assertions.assertThat(actual.getParamRepositories()).contains(repository);
        return this;
    }
}
