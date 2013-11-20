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

import java.util.List;
import org.smartparam.engine.model.editable.IdentifiableParameter;
import org.smartparam.engine.model.editable.IdentifiableParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public interface ParameterViewer {

    DescribedEntity<ViewableRepositoryCapabilities> capabilities(RepositoryName of);

    List<DescribedEntity<ViewableRepositoryCapabilities>> capabilities();

    List<RepositoryName> repositories();

    List<DescribedCollection<String>> listParameters();

    DescribedCollection<String> listParameters(RepositoryName from);

    DescribedEntity<IdentifiableParameter> getParameterMetadata(RepositoryName from, String parameterName);

    DescribedCollection<IdentifiableParameterEntry> listParameterEntries(RepositoryName from, String parameterName, ParameterEntriesFilter filter);

}
