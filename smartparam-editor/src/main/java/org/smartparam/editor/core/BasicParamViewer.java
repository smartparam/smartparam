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
package org.smartparam.editor.core;

import org.smartparam.editor.core.filters.ParameterEntriesFilter;
import org.smartparam.editor.core.filters.ParameterFilter;
import org.smartparam.editor.core.store.RepositoryStore;
import org.smartparam.editor.core.identity.DescribedEntity;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.editor.core.identity.DescribedCollection;
import java.util.ArrayList;
import java.util.List;
import org.smartparam.editor.core.capabilities.RepositoryCapabilities;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMapConverter;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamViewer implements ParamViewer {

    private final RepositoryStore<ViewableParamRepository> repositories;

    private final ParameterEntryMapConverter converter;

    public BasicParamViewer(ParamEngine paramEngine,
            ParameterEntryMapConverter entryMapConverter) {
        List<NamedParamRepository> registeredRepositories = paramEngine.runtimeConfiguration().getParamRepositories();

        repositories = new RepositoryStore<ViewableParamRepository>(registeredRepositories, ViewableParamRepository.class);
        converter = entryMapConverter;
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
    public boolean parameterExists(RepositoryName in, String parameterName) {
        return !listParameters(in, new ParameterFilter(parameterName)).isEmpty();
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
    public DescribedCollection<MapEntry> getParameterEntries(RepositoryName from, String parameterName, Iterable<ParameterEntryKey> parameterEntryKeys) {
        ViewableParamRepository repository = repositories.get(from);
        Parameter metadata = repository.getParameterMetadata(parameterName);
        return convert(from, metadata, repository.getParameterEntries(parameterName, parameterEntryKeys));
    }

    @Override
    public DescribedCollection<MapEntry> listParameterEntries(RepositoryName from, String parameterName, ParameterEntriesFilter filter) {
        ViewableParamRepository repository = repositories.get(from);
        Parameter metadata = repository.getParameterMetadata(parameterName);
        return convert(from, metadata, repository.listEntries(parameterName, filter));
    }

    private DescribedCollection<MapEntry> convert(RepositoryName from, Parameter metadata, List<ParameterEntry> entries) {
        List<MapEntry> describedEntries = new ArrayList<MapEntry>();
        for (ParameterEntry entry : entries) {
            describedEntries.add(converter.asMap(metadata, entry));
        }
        return new DescribedCollection<MapEntry>(from, describedEntries);
    }
}
