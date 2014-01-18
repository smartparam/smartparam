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
public class NamedParamRepository {

    private final RepositoryName name;

    private final ParamRepository repository;

    public NamedParamRepository(RepositoryName name, ParamRepository repository) {
        this.name = name;
        this.repository = repository;
    }

    public RepositoryName name() {
        return name;
    }

    public ParamRepository repository() {
        return repository;
    }

}
