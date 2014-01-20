/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.engine.core.output;

import java.util.Iterator;
import java.util.List;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.repository.RepositoryName;

/**
 *
 * @author Adam Dubiel
 */
public class DetailedParamValueImpl extends AbstractParamValue implements DetailedParamValue {

    private final List<DetailedMultiValue> rows;

    public DetailedParamValueImpl(List<DetailedMultiValue> rows, RepositoryName sourceRepository) {
        super(sourceRepository);
        this.rows = rows;
    }

    @Override
    protected MultiValue rawRowAt(int rowNo) {
        return rows.get(rowNo);
    }

    @Override
    protected List<? extends MultiValue> rawRows() {
        return rows;
    }

    @Override
    public Iterator<MultiValue> iterator() {
        return new MultiValueIterator();
    }

    @Override
    public DetailedMultiValue detailedRow() {
        return detailedRow(0);
    }

    @Override
    public DetailedMultiValue detailedRow(int rowNo) {
        if (rowNo >= 0 && rowNo < size()) {
            return rows.get(rowNo);
        }
        throw new InvalidRowIndexException(rowNo, rows.size());
    }

    @Override
    public Iterable<DetailedMultiValue> detailedRows() {
        return new Iterable<DetailedMultiValue>() {
            @Override
            public Iterator<DetailedMultiValue> iterator() {
                return rows.iterator();
            }
        };
    }

    @Override
    public Iterable<MapEntry> detailedEntries() {
        return new Iterable<MapEntry>() {
            @Override
            public Iterator<MapEntry> iterator() {
                return new MapEntryIterator();
            }
        };
    }

    private class MapEntryIterator implements Iterator<MapEntry> {

        private final Iterator<DetailedMultiValue> detailedValuesIterator = rows.iterator();

        @Override
        public boolean hasNext() {
            return detailedValuesIterator.hasNext();
        }

        @Override
        public MapEntry next() {
            return detailedValuesIterator.next().entry();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Removing from ParamValue is prohibited.");
        }

    }

    private class MultiValueIterator implements Iterator<MultiValue> {

        private final Iterator<DetailedMultiValue> detailedValuesIterator = rows.iterator();

        @Override
        public boolean hasNext() {
            return detailedValuesIterator.hasNext();
        }

        @Override
        public MultiValue next() {
            return detailedValuesIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Removing from ParamValue is prohibited.");
        }

    }
}
