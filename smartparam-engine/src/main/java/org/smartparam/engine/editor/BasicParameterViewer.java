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
import java.util.List;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.ViewableParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParameterViewer implements ParameterViewer {

    private final RepositoryStore<ViewableParamRepository> repositories;

    public BasicParameterViewer(ParamEngine paramEngine) {
        List<ParamRepository> registeredRepositories = paramEngine.getConfiguration().getParamRepositories();
        repositories = new RepositoryStore<ViewableParamRepository>(registeredRepositories, ViewableParamRepository.class);
    }

    @Override
    public DescribedEntity<ViewableRepositoryCapabilities> capabilities(RepositoryName of) {
        ViewableParamRepository repository = repositories.get(of);
        return new DescribedEntity<ViewableRepositoryCapabilities>(of, repository.capabilities());
    }

    @Override
    public List<DescribedEntity<ViewableRepositoryCapabilities>> capabilities() {
        List<DescribedEntity<ViewableRepositoryCapabilities>> capabilities = new ArrayList<DescribedEntity<ViewableRepositoryCapabilities>>();
        for (RepositoryName repositoryName : repositories.storedRepositories()) {
            capabilities.add(capabilities(repositoryName));
        }
        return capabilities;
    }

    @Override
    public List<RepositoryName> repositories() {
        return repositories.storedRepositories();
    }

    @Override
    public List<DescribedCollection<String>> listParameters() {
        List<DescribedCollection<String>> parameters = new ArrayList<DescribedCollection<String>>();
        for (RepositoryName repositoryName : repositories.storedRepositories()) {
            parameters.add(listParameters(repositoryName));
        }
        return parameters;
    }

    @Override
    public List<DescribedCollection<String>> listParameters(ParameterFilter filter) {
        List<DescribedCollection<String>> parameters = new ArrayList<DescribedCollection<String>>();
        for (RepositoryName repositoryName : repositories.storedRepositories()) {
            parameters.add(listParameters(repositoryName, filter));
        }
        return parameters;
    }

    @Override
    public DescribedCollection<String> listParameters(RepositoryName from) {
        ViewableParamRepository repository = repositories.get(from);
        return new DescribedCollection<String>(from, repository.listParameters());
    }

    @Override
    public DescribedCollection<String> listParameters(RepositoryName from, ParameterFilter filter) {
        ViewableParamRepository repository = repositories.get(from);
        return new DescribedCollection<String>(from, repository.listParameters(filter));
    }

    @Override
    public DescribedEntity<Parameter> getParameterMetadata(RepositoryName from, String parameterName) {
        ViewableParamRepository repository = repositories.get(from);
        return new DescribedEntity<Parameter>(from, repository.getParameterMetadata(parameterName));
    }

    @Override
    public DescribedCollection<ParameterEntry> listParameterEntries(RepositoryName from, String parameterName, ParameterEntriesFilter filter) {
        ViewableParamRepository repository = repositories.get(from);
        return new DescribedCollection<ParameterEntry>(from, repository.listEntries(parameterName, filter));
    }

}
