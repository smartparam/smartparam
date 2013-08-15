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
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.repository.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEngineAssert extends AbstractAssert<ParamEngineAssert, ParamEngine> {

    private ParamEngineAssert(ParamEngine actual) {
        super(actual, ParamEngineAssert.class);
    }

    public static ParamEngineAssert assertThat(ParamEngine actual) {
        return new ParamEngineAssert(actual);
    }

    public ParamEngineAssert hasInitializedTree() {
        Assertions.assertThat(actual.getParamPreparer()).isNotNull();
        Assertions.assertThat(actual.getParamPreparer().getMatcherRepository()).isNotNull();
        Assertions.assertThat(actual.getParamPreparer().getTypeRepository()).isNotNull();
        Assertions.assertThat(actual.getParamPreparer().getParamCache()).isNotNull();

        Assertions.assertThat(actual.getFunctionManager()).isNotNull();
        Assertions.assertThat(actual.getFunctionManager().getFunctionProvider()).isNotNull();
        Assertions.assertThat(actual.getFunctionManager().getFunctionProvider().getFunctionCache()).isNotNull();
        Assertions.assertThat(actual.getFunctionManager().getInvokerRepository()).isNotNull();

        return this;
    }

    public ParamEngineAssert hasInitializedTreeWithScannedItems() {
        hasInitializedTree();

        Assertions.assertThat(actual.getFunctionManager().getInvokerRepository()).hasItems();
        Assertions.assertThat(actual.getFunctionManager().getFunctionProvider()).hasItems();
        Assertions.assertThat(actual.getParamPreparer().getTypeRepository()).hasItems();
        Assertions.assertThat(actual.getParamPreparer().getMatcherRepository()).hasItems();

        return this;
    }

    public ParamEngineAssert hasRepository(ParamRepository repository) {
        Assertions.assertThat(actual.getParamPreparer().getParameterProvider().registeredItems()).containsValue(repository);
        return this;
    }
}
