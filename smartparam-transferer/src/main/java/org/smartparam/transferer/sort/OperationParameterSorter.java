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

import java.util.Set;
import org.smartparam.transferer.operation.TransferOperationType;

/**
 *
 * @author Adam Dubiel
 */
public class OperationParameterSorter implements ParameterSorter {

    public SortedParameters sort(Set<String> sourceRepoParameters, Set<String> targetRepoParameters) {
        SortedParameters sorted = new SortedParameters();

        for(String sourceParameter : sourceRepoParameters) {
            if(targetRepoParameters.contains(sourceParameter)) {
                sorted.add(TransferOperationType.OVERRIDE, sourceParameter);
            }
            else {
                sorted.add(TransferOperationType.CREATE, sourceParameter);
            }
        }

        for(String targetParameter : targetRepoParameters) {
            if(!sourceRepoParameters.contains(targetParameter)) {
                sorted.add(TransferOperationType.DELETE, targetParameter);
            }
        }

        return sorted;
    }

}
