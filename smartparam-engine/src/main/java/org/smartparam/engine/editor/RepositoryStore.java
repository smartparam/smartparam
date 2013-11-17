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
package org.smartparam.engine.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.repository.EditableParamRepository;
import org.smartparam.engine.core.repository.ParamRepository;

/**
 *
 * @author Adam Dubiel
 */
public class RepositoryStore {

    private final List<RepositoryName> storedRepositoriesNames = new ArrayList<RepositoryName>();

    private final Map<RepositoryName, EditableParamRepository> editableRepositories = new HashMap<RepositoryName, EditableParamRepository>();

    private final Map<RepositoryName, ParamRepository> readonlyRepositories = new HashMap<RepositoryName, ParamRepository>();

    RepositoryStore(List<ParamRepository> allRepositories) {
        filterOutEditableRepositories(allRepositories);
    }

    private void filterOutEditableRepositories(List<ParamRepository> allRepositories) {
        for (ParamRepository repository : allRepositories) {
            if (repository instanceof EditableParamRepository) {
                injectRepository(repository, editableRepositories);
            } else {
                injectRepository(repository, readonlyRepositories);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends ParamRepository> void injectRepository(ParamRepository repository, Map<RepositoryName, T> repositoriesCollection) {
        String repositoryClassName;
        RepositoryName repositoryName;
        int repositoryOccurence = 0;

        repositoryClassName = repository.getClass().getSimpleName();
        repositoryName = new RepositoryName(repositoryClassName);

        while (repositoriesCollection.containsKey(repositoryName)) {
            repositoryOccurence++;
            repositoryName = new RepositoryName(repositoryClassName + repositoryOccurence);
        }
        repositoriesCollection.put(repositoryName, (T) repository);
        storedRepositoriesNames.add(repositoryName);
    }

    EditableParamRepository getEditable(RepositoryName from) {
        EditableParamRepository repository = editableRepositories.get(from);
        if (repository == null) {
            throw InvalidSourceRepositoryException.noneForWriting(from);
        }
        return repository;
    }

    ParamRepository get(RepositoryName from) {
        ParamRepository repository = readonlyRepositories.get(from);
        if (repository == null) {
            repository = editableRepositories.get(from);
        }
        if (repository == null) {
            throw InvalidSourceRepositoryException.noneForReading(from);
        }
        return repository;
    }

    List<RepositoryName> storedRepositories() {
        return Collections.unmodifiableList(storedRepositoriesNames);
    }
}
