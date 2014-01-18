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

import org.smartparam.engine.core.repository.RepositoryName;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterFromRepositoryBuilder {

    private final Parameter parameter;

    private RepositoryName name = RepositoryName.from("default-test-name");

    private ParameterFromRepositoryBuilder(Parameter parameter) {
        this.parameter = parameter;
    }

    public static ParameterFromRepositoryBuilder repositoryParameter(Parameter parameter) {
        return new ParameterFromRepositoryBuilder(parameter);
    }

    public ParameterFromRepository build() {
        return new ParameterFromRepository(parameter, name);
    }

    public ParameterFromRepositoryBuilder from(String name) {
        this.name = RepositoryName.from(name);
        return this;
    }
}
