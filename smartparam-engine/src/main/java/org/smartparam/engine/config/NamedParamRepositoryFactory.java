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
package org.smartparam.engine.config;

import java.util.HashMap;
import java.util.Map;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.ParamRepository;

/**
 * Creates {@link NamedParamRepository} objects. Includes generator of valid, unique names based on class simple name
 * and occurrence counter (in case there are many repositories of same class registered).
 *
 * @author Adam Dubiel
 */
public class NamedParamRepositoryFactory {

    private final Map<Class<? extends ParamRepository>, Integer> defaultNameCounter = new HashMap<Class<? extends ParamRepository>, Integer>();

    public NamedParamRepositoryFactory() {
    }

    public NamedParamRepository create(RepositoryName repositoryName, ParamRepository repository) {
        return new NamedParamRepository(repositoryName, repository);
    }

    public NamedParamRepository create(String repositoryName, ParamRepository repository) {
        return new NamedParamRepository(RepositoryName.from(repositoryName), repository);
    }

    public NamedParamRepository create(ParamRepository repository) {
        return new NamedParamRepository(assignName(repository), repository);
    }

    private RepositoryName assignName(ParamRepository repository) {
        Class<? extends ParamRepository> repositoryClass = repository.getClass();

        Integer defaultNameOccurrence = defaultNameCounter.get(repositoryClass);
        if (defaultNameOccurrence == null) {
            defaultNameOccurrence = 0;
        }
        RepositoryName repositoryName = new RepositoryName(repositoryClass.getSimpleName() + (defaultNameOccurrence > 0 ? defaultNameOccurrence : ""));

        defaultNameOccurrence++;
        defaultNameCounter.put(repositoryClass, defaultNameOccurrence);

        return repositoryName;
    }
}
