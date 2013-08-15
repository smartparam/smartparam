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
package org.smartparam.engine.core.repository;

import org.smartparam.engine.model.function.Function;

/**
 * Repository of functions. ParamEngine instance can have multiple function
 * repositories defined, see {@link org.smartparam.engine.core.service.FunctionProvider}.
 *
 * Each repository is registered under unique name. Function has type, which
 * is used to choose correct {@link org.smartparam.engine.core.invoker.FunctionInvoker},
 * which will run function (invoker identifier has to match function type).
 *
 * @see Function
 * @see org.smartparam.engine.core.invoker.FunctionInvoker
 *
 * @author Adam Dubiel
 * @since 1.0.0
 */
public interface FunctionRepository {

    /**
     * Should return instance of function or null if was not found.
     *
     * @param functionName unique (repository-wide) name of function
     * @return function object
     */
    Function loadFunction(String functionName);
}
