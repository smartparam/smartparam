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
package org.smartparam.editor.store;

import org.smartparam.editor.identity.RepositoryName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.parameter.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public class RepositoryStore<T extends ParamRepository> {

    private final List<RepositoryName> storedRepositoriesNames = new ArrayList<RepositoryName>();

    private final Map<RepositoryName, T> storedRepositories = new HashMap<RepositoryName, T>();

    private final ParamRepositoryNaming repositoryNaming;

    public RepositoryStore(List<ParamRepository> allRepositories, ParamRepositoryNaming repositoryNaming, Class<T> storedClass) {
        this.repositoryNaming = repositoryNaming;
        filterOutMatchingRepositories(allRepositories, storedClass);
    }

    private void filterOutMatchingRepositories(List<ParamRepository> allRepositories, Class<T> storedClass) {
        for (ParamRepository repository : allRepositories) {
            if (storedClass.isAssignableFrom(repository.getClass())) {
                injectRepository(repository, storedRepositories);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends ParamRepository> void injectRepository(ParamRepository repository, Map<RepositoryName, T> repositoriesCollection) {
        int repositoryOccurence = 0;
        RepositoryName repositoryName = repositoryNaming.name(repository.getClass());

        while (repositoriesCollection.containsKey(repositoryName)) {
            repositoryOccurence++;
            repositoryName = repositoryNaming.name(repository.getClass(), repositoryOccurence);
        }
        repositoriesCollection.put(repositoryName, (T) repository);
        storedRepositoriesNames.add(repositoryName);
    }

    public T get(RepositoryName from) {
        T repository = storedRepositories.get(from);
        if (repository == null) {
            throw new InvalidSourceRepositoryException(from);
        }
        return repository;
    }

    public List<RepositoryName> storedRepositories() {
        return Collections.unmodifiableList(storedRepositoriesNames);
    }
}
