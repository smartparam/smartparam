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

import org.smartparam.editor.core.identity.DescribedEntity;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.editor.core.identity.DescribedCollection;
import java.util.List;
import org.smartparam.editor.core.capabilities.RepositoryCapabilities;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.editor.core.filters.ParameterEntriesFilter;
import org.smartparam.editor.core.filters.ParameterFilter;
import org.smartparam.engine.core.parameter.Parameter;

/**
 *
 * @author Adam Dubiel
 */
public interface ParamViewer {

    DescribedEntity<RepositoryCapabilities> capabilities(RepositoryName of);

    List<DescribedEntity<RepositoryCapabilities>> capabilities();

    List<RepositoryName> repositories();

    boolean parameterExists(String parameterName);

    boolean parameterExists(RepositoryName in, String parameterName);

    List<DescribedCollection<String>> listParameters();

    List<DescribedCollection<String>> listParameters(ParameterFilter filter);

    DescribedCollection<String> listParameters(RepositoryName from);

    DescribedCollection<String> listParameters(RepositoryName from, ParameterFilter filter);

    DescribedEntity<Parameter> getParameterMetadata(RepositoryName from, String parameterName);

    DescribedCollection<ParameterEntryMap> getParameterEntries(RepositoryName from, String parameterName, Iterable<ParameterEntryKey> parameterEntryKeys);

    DescribedCollection<ParameterEntryMap> listParameterEntries(RepositoryName from, String parameterName, ParameterEntriesFilter filter);

    ParamEditorRuntimeConfig runtimeConfig();
}
