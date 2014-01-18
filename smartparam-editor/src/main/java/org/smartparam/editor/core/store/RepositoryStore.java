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
package org.smartparam.editor.core.store;

import java.util.ArrayList;
import org.smartparam.engine.core.repository.RepositoryName;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.parameter.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public final class RepositoryStore<T extends ParamRepository> {

    private final Map<RepositoryName, T> storedRepositories = new LinkedHashMap<RepositoryName, T>();

    public RepositoryStore(List<NamedParamRepository> allRepositories, Class<T> storedClass) {
        filterOutMatchingRepositories(allRepositories, storedClass);
    }

    @SuppressWarnings("unchecked")
    private void filterOutMatchingRepositories(List<NamedParamRepository> allRepositories, Class<T> storedClass) {
        ParamRepository repository;
        for (NamedParamRepository namedRepository : allRepositories) {
            repository = namedRepository.repository();
            if (storedClass.isAssignableFrom(repository.getClass())) {
                storedRepositories.put(namedRepository.name(), (T) namedRepository.repository());
            }
        }
    }

    public T get(RepositoryName from) {
        T repository = storedRepositories.get(from);
        if (repository == null) {
            throw new InvalidSourceRepositoryException(from);
        }
        return repository;
    }

    public List<RepositoryName> storedRepositories() {
        return Collections.unmodifiableList(new ArrayList<RepositoryName>(storedRepositories.keySet()));
    }
}
