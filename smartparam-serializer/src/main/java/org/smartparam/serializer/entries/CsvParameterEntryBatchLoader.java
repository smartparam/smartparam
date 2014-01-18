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

import org.smartparam.engine.core.parameter.entry.ParameterEntryBatchLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.smartparam.engine.core.parameter.ParamBatchLoadingException;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.serializer.model.DeserializedParameterEntry;
import org.smartparam.serializer.util.StreamCloser;
import org.supercsv.io.CsvListReader;

/**
 *
 * @author Adam Dubiel
 */
public class CsvParameterEntryBatchLoader implements ParameterEntryBatchLoader {

    private final CsvListReader reader;

    private boolean hasMore = true;

    public CsvParameterEntryBatchLoader(CsvListReader reader) {
        this.reader = reader;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public Collection<ParameterEntry> nextBatch(int batchSize) {
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>(batchSize);

        try {
            List<String> line;
            int entriesRead;
            for (entriesRead = 0; entriesRead < batchSize; ++entriesRead) {
                line = reader.read();
                if (line == null) {
                    break;
                }
                entries.add(new DeserializedParameterEntry(line));
            }

            if (entriesRead < batchSize) {
                hasMore = false;
            }
        } catch (IOException exception) {
            throw new ParamBatchLoadingException("deserialization error", exception);
        }

        return entries;
    }

    @Override
    public void close() {
        StreamCloser.closeStream(reader);
    }
}
