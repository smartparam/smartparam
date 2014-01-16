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
package org.smartparam.editor.core.store;

import java.util.List;
import java.util.Set;
import org.smartparam.editor.core.capabilities.RepositoryCapabilities;
import org.smartparam.editor.core.EditableParamRepository;
import org.smartparam.engine.core.parameter.LevelKey;
import org.smartparam.engine.core.parameter.ParameterEntryKey;
import org.smartparam.engine.core.parameter.ParameterKey;
import org.smartparam.editor.core.filters.ParameterEntriesFilter;
import org.smartparam.editor.core.filters.ParameterFilter;
import org.smartparam.editor.core.ViewableParamRepository;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class FakeEditableParamRepository implements EditableParamRepository, ViewableParamRepository {

    @Override
    public ParameterKey createParameter(Parameter parameter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateParameter(String parameterName, Parameter parameter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteParameter(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LevelKey addLevel(String parameterName, Level level) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reorderLevels(String parameterName, List<LevelKey> orderedLevels) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateLevel(String parameterName, LevelKey levelKey, Level level) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteLevel(String parameterName, LevelKey levelKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParameterEntryKey addEntry(String parameterName, ParameterEntry entry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ParameterEntryKey> addEntries(String parameterName, Iterable<ParameterEntry> entries) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateEntry(String parameterName, ParameterEntryKey entryKey, ParameterEntry entry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteEntry(String parameterName, ParameterEntryKey entryKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteEntries(String parameterName, Iterable<ParameterEntryKey> entryKeys) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Parameter load(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> listParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RepositoryCapabilities capabilities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> listParameters(ParameterFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Parameter getParameterMetadata(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ParameterEntry> getParameterEntries(String parameterName, Iterable<ParameterEntryKey> parameterEntryKeys) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ParameterEntry> listEntries(String parameterName, ParameterEntriesFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteEntries(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
