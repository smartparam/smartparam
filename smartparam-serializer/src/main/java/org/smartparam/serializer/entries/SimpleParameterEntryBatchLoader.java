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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleParameterEntryBatchLoader implements ParameterEntryBatchLoader {

    private List<ParameterEntry> allEntriesList;

    private boolean hasMore = true;

    private int lastReadPosition = 0;

    public SimpleParameterEntryBatchLoader(Parameter parameter) {
        if (parameter.getEntries() == null || parameter.getEntries().isEmpty()) {
            hasMore = false;
        } else {
            this.allEntriesList = new ArrayList<ParameterEntry>(parameter.getEntries());
        }
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public Collection<ParameterEntry> nextBatch(int batchSize) {
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>(batchSize);

        if (hasMore) {
            int sublistEndIndex = lastReadPosition + batchSize;
            if (sublistEndIndex > allEntriesList.size()) {
                sublistEndIndex = allEntriesList.size();
                hasMore = false;
            }

            entries = allEntriesList.subList(lastReadPosition, sublistEndIndex);
        }

        return entries;
    }

    @Override
    public void close() {
    }
}
