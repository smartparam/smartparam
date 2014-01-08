/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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

import java.util.List;
import java.util.Set;
import org.smartparam.editor.capabilities.RepositoryCapabilities;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.editor.viewer.ParameterEntriesFilter;
import org.smartparam.editor.viewer.ParameterFilter;
import org.smartparam.editor.viewer.ViewableParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
class FakeViewableParamRepository implements ViewableParamRepository {

    public RepositoryCapabilities capabilities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> listParameters(ParameterFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Parameter getParameterMetadata(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ParameterEntry> getParameterEntries(String parameterName, Iterable<ParameterEntryKey> parameterEntryKeys) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ParameterEntry> listEntries(String parameterName, ParameterEntriesFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Parameter load(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ParameterBatchLoader batchLoad(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<String> listParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
