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

import java.util.Comparator;
import org.smartparam.engine.core.output.entry.MapEntry;

/**
 * Detailed parameter output allows on accessing all parameter values - not only output. This way it is possible
 * to display exactly what values were matched by query. Detailed information is returned in friendly form via
 * {@link MapEntry}.
 *
 * @author Adam Dubiel
 */
public interface DetailedParamValue extends ParamValue {

    /**
     * Return row with detailed information from matrix.
     */
    DetailedMultiValue detailedRow(int rowNo);

    /**
     * Returns first row with detailed information.
     */
    DetailedMultiValue detailedRow();

    /**
     * Iterate over all detailed rows.
     */
    Iterable<DetailedMultiValue> detailedRows();

    /**
     * Iterate over all detailed entries.
     */
    Iterable<MapEntry> detailedEntries();

    /**
     * Returns detailed entry from first matrix row.
     */
    MapEntry detailedEntry();

    /**
     * Apply sorting to matrix.
     */
    void sort(Comparator<MapEntry> comparator);

}
