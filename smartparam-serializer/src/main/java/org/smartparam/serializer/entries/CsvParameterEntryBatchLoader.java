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
package org.smartparam.serializer.entries;

import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.smartparam.engine.core.exception.ParamBatchLoadingException;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.supercsv.io.CsvListReader;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntryBatchLoader implements ParameterEntryBatchLoader {

    private CsvListReader reader;

    private Class<? extends EditableParameterEntry> instanceClass;

    private boolean hasMore = true;

    public CsvParameterEntryBatchLoader(Class<? extends EditableParameterEntry> instanceClass, CsvListReader reader) {
        this.instanceClass = instanceClass;
        this.reader = reader;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public Collection<ParameterEntry> nextBatch(int batchSize) throws ParamBatchLoadingException {
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>(batchSize);

        try {
            List<String> line;
            int entriesRead;
            for (entriesRead = 0; entriesRead < batchSize; ++entriesRead) {
                line = reader.read();
                if (line == null) {
                    break;
                }
                entries.add(createParameterEntry(line));
            }

            if (entriesRead < batchSize) {
                hasMore = false;
            }
        } catch (IOException exception) {
            throw new ParamBatchLoadingException("deserialization error", exception);
        } catch (IllegalAccessException illegalAccessException) {
            throw new ParamBatchLoadingException("error creating instance of " + instanceClass.getName() + ", maybe it has no default constructor?", illegalAccessException);
        } catch (InstantiationException instantiationException) {
            throw new ParamBatchLoadingException("error creating instance of " + instanceClass.getName() + ", maybe it has no default constructor?", instantiationException);
        }

        return entries;
    }

    private ParameterEntry createParameterEntry(List<String> levelValues) throws IllegalAccessException, InstantiationException {
        EditableParameterEntry parameterEntry = instanceClass.newInstance();
        parameterEntry.setLevels(levelValues.toArray(new String[levelValues.size()]));

        return parameterEntry;
    }

    @Override
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException exception) {
            throw new SmartParamException("exception while closing stream", exception);
        }
    }


}
