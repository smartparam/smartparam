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
package org.smartparam.engine.core.prepared;

import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.repository.RepositoryName;

/**
 *
 * @author Adam Dubiel
 */
public class PreparedParameterTestBuilder {

    private RepositoryName from = RepositoryName.from("default-test-repo");

    private Parameter parameter;

    private PreparedLevel[] levels;

    private PreparedParameterTestBuilder() {
    }

    public static PreparedParameterTestBuilder preparedParameter() {
        return new PreparedParameterTestBuilder();
    }

    public PreparedParameter build() {
        return new PreparedParameter(from, parameter, levels);
    }

    public PreparedParameterTestBuilder forParameter(Parameter parameter) {
        this.parameter = parameter;
        return this;
    }

    public PreparedParameterTestBuilder withLevels(PreparedLevel... levels) {
        this.levels = levels;
        return this;
    }

    public PreparedParameterTestBuilder from(String repositoryName) {
        this.from = RepositoryName.from(repositoryName);
        return this;
    }
}
