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
package org.smartparam.engine.core.parameter;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.repository.ListRepository;
import org.smartparam.engine.core.repository.RepositoryName;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParameterProvider implements ParameterProvider {

    private final ListRepository<NamedParamRepository> innerRepository = new ListRepository<NamedParamRepository>(NamedParamRepository.class);

    @Override
    public ParameterFromRepository load(String parameterName) {
        Parameter parameter;
        RepositoryName name;
        for (NamedParamRepository repository : innerRepository.getItems()) {
            parameter = repository.repository().load(parameterName);
            name = repository.name();
            if (parameter != null) {
                return new ParameterFromRepository(parameter, name);
            }
        }
        return null;
    }

    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        Set<ParameterEntry> entries = null;
        for (NamedParamRepository repository : innerRepository.getItems()) {
            entries = repository.repository().findEntries(parameterName, levelValues);
            if (entries != null) {
                break;
            }
        }
        return entries;
    }

    @Override
    public void register(NamedParamRepository repository) {
        innerRepository.register(repository);
    }

    @Override
    public List<NamedParamRepository> registeredItems() {
        return innerRepository.getItems();
    }

    @Override
    public void registerAll(List<NamedParamRepository> repositories) {
        innerRepository.registerAll(repositories);
    }

}
