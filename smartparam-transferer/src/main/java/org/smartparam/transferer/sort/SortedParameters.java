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
package org.smartparam.transferer.sort;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import org.smartparam.transferer.TransferOperationType;

/**
 *
 * @author Adam Dubiel
 */
public class SortedParameters {

    private EnumMap<TransferOperationType, Set<String>> buckets = new EnumMap<TransferOperationType, Set<String>>(TransferOperationType.class);

    public SortedParameters() {
        for (TransferOperationType operationType : TransferOperationType.values()) {
            buckets.put(operationType, new HashSet<String>());
        }
    }

    public void add(TransferOperationType operationType, String parameterName) {
        buckets.get(operationType).add(parameterName);
    }

    public Set<String> getParameterNames(TransferOperationType operationType) {
        Set<String> parameters = buckets.get(operationType);
        if (parameters == null) {
            parameters = new HashSet<String>();
        }

        return parameters;
    }
}
