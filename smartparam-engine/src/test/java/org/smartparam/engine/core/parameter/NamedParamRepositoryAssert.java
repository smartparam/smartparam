/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.parameter;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/**
 *
 * @author Adam Dubiel
 */
public class NamedParamRepositoryAssert extends AbstractAssert<NamedParamRepositoryAssert, NamedParamRepository> {

    private NamedParamRepositoryAssert(NamedParamRepository actual) {
        super(actual, NamedParamRepositoryAssert.class);
    }

    public static NamedParamRepositoryAssert assertThat(NamedParamRepository repository) {
        return new NamedParamRepositoryAssert(repository);
    }

    public NamedParamRepositoryAssert containsRepository(ParamRepository repository) {
        Assertions.assertThat(actual.repository()).isSameAs(repository);
        return this;
    }

    public NamedParamRepositoryAssert hasName(String name) {
        Assertions.assertThat(actual.name().value()).isEqualTo(name);
        return this;
    }
}
