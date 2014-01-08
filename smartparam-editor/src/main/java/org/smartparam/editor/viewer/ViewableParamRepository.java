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

import java.util.List;
import org.smartparam.editor.capabilities.RepositoryCapabilities;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public interface ViewableParamRepository extends ParamRepository {

    RepositoryCapabilities capabilities();

    List<String> listParameters(ParameterFilter filter);

    Parameter getParameterMetadata(String parameterName);

    List<ParameterEntry> getParameterEntries(String parameterName, Iterable<ParameterEntryKey> parameterEntryKeys);

    List<ParameterEntry> listEntries(String parameterName, ParameterEntriesFilter filter);

}
