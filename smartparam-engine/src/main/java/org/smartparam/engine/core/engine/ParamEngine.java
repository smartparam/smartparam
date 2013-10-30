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
package org.smartparam.engine.core.engine;

import java.util.Collection;
import org.smartparam.engine.config.ParamEngineRuntimeConfig;

import org.smartparam.engine.core.context.ParamContext;

/**
 * Single point of entry to get all parameters and call functions managed by
 * parameter engine.
 *
 * @author Adam Dubiel
 */
public interface ParamEngine {

    /**
     * Return submatrix of parameter rows that match values from context.
     * Remember, that to use dynamic context levels need to have defined
     * levelCreators.
     *
     * @param parameterName name of parameter to search
     * @param context       evaluation context
     * @return resulting submatrix
     */
    ParamValue get(String parameterName, ParamContext context);

    /**
     * Return submatrix of parameter rows that match provided query values.
     * Input levels array should have length equal to parameters input levels count.
     *
     * @param parameterName name of parameter to search
     * @param inputLevels   values to match against each input level
     * @return resulting submatrix
     */
    ParamValue get(String paramName, Object... inputLevels);

    /**
     * Assemble object of given class out of value returned by parameter. Always
     * takes values from first row.
     *
     * @see org.smartparam.engine.core.assembler.AssemblyStrategy;
     */
    <T> T getObject(String paramName, Class<T> outputClass, ParamContext context);

    /**
     * Assemble object of given class out of value returned by parameter using
     * explicitly provided values.
     */
    <T> T getObject(String paramName, Class<T> outputClass, Object... inputLevels);

    /**
     * Returns collection of objects assembled from matrix returned by parameter.
     */
    <T> Collection<T> getObjects(String paramName, Class<T> outputClass, ParamContext context);

    /**
     * Returns collection of objects assembled from matrix returned by parameter
     * using explicitly provided values.
     */
    <T> Collection<T> getObjects(String paramName, Class<T> outputClass, Object... inputLevels);

    /**
     * Use function engine to call function registered under provided name,
     * passing provided invocation arguments.
     *
     * @param functionName name of function to run
     * @param args         function invocation arguments
     * @return invocation result, null for void functions
     */
    Object callFunction(String functionName, Object... args);

    /**
     * Return runtime configuration of this instance of parameter engine.
     *
     * @return runtime config
     */
    ParamEngineRuntimeConfig getConfiguration();
}
