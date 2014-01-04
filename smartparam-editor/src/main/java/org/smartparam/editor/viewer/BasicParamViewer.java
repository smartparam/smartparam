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
package org.smartparam.editor.viewer;

import org.smartparam.editor.store.RepositoryStore;
import org.smartparam.editor.identity.DescribedEntity;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.identity.DescribedCollection;
import java.util.ArrayList;
import java.util.List;
import org.smartparam.editor.capabilities.RepositoryCapabilities;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamViewer implements ParamViewer {

    private final RepositoryStore<ViewableParamRepository> repositories;

    public BasicParamViewer(ParamEngine paramEngine) {
        List<ParamRepository> registeredRepositories = paramEngine.runtimeConfiguration().getParamRepositories();
        repositories = new RepositoryStore<ViewableParamRepository>(registeredRepositories, ViewableParamRepository.class);
    }

    @Override
    public DescribedEntity<RepositoryCapabilities> capabilities(RepositoryName of) {
        ViewableParamRepository repository = repositories.get(of);
        return new DescribedEntity<RepositoryCapabilities>(of, repository.capabilities());
    }

    @Override
    public List<DescribedEntity<RepositoryCapabilities>> capabilities() {
        List<DescribedEntity<RepositoryCapabilities>> capabilities = new ArrayList<DescribedEntity<RepositoryCapabilities>>();
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
    public boolean parameterExists(String parameterName) {
        return !listParameters(new ParameterFilter(parameterName)).isEmpty();
    }

    @Override
    public List<DescribedCollection<String>> listParameters() {
        List<DescribedCollection<String>> parameters = new ArrayList<DescribedCollection<String>>();
        DescribedCollection<String> repositoryParameters;
        for (RepositoryName repositoryName : repositories.storedRepositories()) {
            repositoryParameters = listParameters(repositoryName);
            if (!repositoryParameters.isEmpty()) {
                parameters.add(repositoryParameters);
            }
        }
        return parameters;
    }

    @Override
    public List<DescribedCollection<String>> listParameters(ParameterFilter filter) {
        List<DescribedCollection<String>> parameters = new ArrayList<DescribedCollection<String>>();
        DescribedCollection<String> repositoryParameters;
        for (RepositoryName repositoryName : repositories.storedRepositories()) {
            repositoryParameters = listParameters(repositoryName, filter);
            if (!repositoryParameters.isEmpty()) {
                parameters.add(repositoryParameters);
            }
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
    public DescribedCollection<ParameterEntry> getParameterEntries(RepositoryName from, Iterable<ParameterEntryKey> parameterEntryKeys) {
        ViewableParamRepository repository = repositories.get(from);
        return new DescribedCollection<ParameterEntry>(from, repository.getParameterEntries(parameterEntryKeys));
    }

    @Override
    public DescribedCollection<ParameterEntry> listParameterEntries(RepositoryName from, String parameterName, ParameterEntriesFilter filter) {
        ViewableParamRepository repository = repositories.get(from);
        return new DescribedCollection<ParameterEntry>(from, repository.listEntries(parameterName, filter));
    }

}
