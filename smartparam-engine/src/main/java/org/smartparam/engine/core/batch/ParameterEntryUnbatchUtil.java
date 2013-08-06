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
package org.smartparam.engine.core.batch;

import org.smartparam.engine.core.exception.ParamBatchLoadingException;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterEntryUnbatchUtil {

    public static void loadEntriesIntoParameter(Parameter parameter, ParameterEntryBatchLoader entryBatchLoader, int batchSize) {
        try {
            while (entryBatchLoader.hasMore()) {
                parameter.getEntries().addAll(entryBatchLoader.nextBatch(batchSize));
            }
        } catch (ParamBatchLoadingException batchException) {
            throw new SmartParamException(batchException);
        }
    }
}
