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
package org.smartparam.engine.core.parameter;

import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 * Writable repository is a repository without fine-grained control of what is
 * being saved into it. When writing a parameter, repository should drop any
 * parameter with same name (if exists) and write provided parameter as a new one.
 *
 * @author Adam Dubiel
 */
public interface WritableParamRepository extends ParamRepository {

    /**
     * Deletes any existing instance of parameter with same name and
     * writes (persists) provided parameter.
     */
    void write(Parameter parameter);

    /**
     * Deletes any existing instance of parameter and writes new
     * one by reading batches from batch loader.
     */
    void write(ParameterBatchLoader batchLoader);

    /**
     * Performs multiple write in single session/batch. Implementations
     * should allocate resources once and free them after writing whole
     * parameters.
     */
    void writeAll(Iterable<Parameter> parameters);

    /**
     * Append additional entries to given parameter.
     */
    void writeParameterEntries(String parameterName, Iterable<ParameterEntry> parameterEntries);

    /**
     * Delete parameter from repository.
     */
    void delete(String parameterName);
}
