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
package org.smartparam.engine.core.function;

/**
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class BasicFunctionManager implements FunctionManager {

    private final InvokerRepository invokerRepository;

    private final FunctionProvider functionProvider;

    public BasicFunctionManager(InvokerRepository invokerRepository, FunctionProvider functionProvider) {
        this.invokerRepository = invokerRepository;
        this.functionProvider = functionProvider;
    }

    @Override
    public Object invokeFunction(String name, Object... args) {
        Function function = functionProvider.getFunction(name);
        return invokeFunction(function, args);
    }

    @Override
    public Object invokeFunction(Function function, Object... args) {
        FunctionInvoker invoker = invokerRepository.getInvoker(function);

        if (invoker == null) {
            throw new UnknownFunctionInvokerException(function);
        }

        try {
            return invoker.invoke(function, args);
        } catch (RuntimeException e) {
            throw new FunctionInvocationException(e, function);
        }
    }
}
