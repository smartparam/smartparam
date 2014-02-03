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
package org.smartparam.repository.memory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.parameter.entry.ParameterEntryBatchLoader;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryParameterEntryBatchLoader implements ParameterEntryBatchLoader {

    private final Iterator<ParameterEntry> entryIterator;

    InMemoryParameterEntryBatchLoader(Parameter parameter) {
        entryIterator = parameter.getEntries().iterator();
    }

    @Override
    public boolean hasMore() {
        return entryIterator.hasNext();
    }

    @Override
    public Collection<ParameterEntry> nextBatch(int batchSize) {
        Set<ParameterEntry> entriesBatch = new HashSet<ParameterEntry>();
        for (int index = 0; index < batchSize && entryIterator.hasNext(); ++index) {
            entriesBatch.add(entryIterator.next());
        }
        return entriesBatch;
    }

    @Override
    public void close() {
    }

}
