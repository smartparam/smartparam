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

package org.smartparam.repository.jdbc.dao;

import java.util.Comparator;
import java.util.List;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryIdSequenceComparator implements Comparator<ParameterEntry> {

    private final List<Long> sequence;

    public ParameterEntryIdSequenceComparator(List<Long> sequence) {
        this.sequence = sequence;
    }

    @Override
    public int compare(ParameterEntry o1, ParameterEntry o2) {
        long id1 = ((JdbcParameterEntry) o1).getId();
        long id2 = ((JdbcParameterEntry) o2).getId();

        return sequence.indexOf(id1) - sequence.indexOf(id2);
    }


}
