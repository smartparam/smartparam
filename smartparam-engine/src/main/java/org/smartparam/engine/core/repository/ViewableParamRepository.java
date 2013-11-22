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
package org.smartparam.engine.core.repository;

import java.util.List;
import org.smartparam.engine.editor.ParameterEntriesFilter;
import org.smartparam.engine.editor.ParameterFilter;
import org.smartparam.engine.editor.ViewableRepositoryCapabilities;
import org.smartparam.engine.model.editable.IdentifiableParameter;
import org.smartparam.engine.model.editable.IdentifiableParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public interface ViewableParamRepository extends ParamRepository {

    ViewableRepositoryCapabilities capabilities();

    List<String> listParameters(ParameterFilter filter);

    IdentifiableParameter getParameterMetadata(String parameterName);

    List<IdentifiableParameterEntry> listEntries(String parameterName, ParameterEntriesFilter filter);

}
