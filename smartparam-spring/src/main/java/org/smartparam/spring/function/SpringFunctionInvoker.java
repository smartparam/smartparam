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
package org.smartparam.spring.function;

import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.functions.java.JavaMethodInvoker;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class SpringFunctionInvoker implements FunctionInvoker {

    private final ApplicationContext appContext;

    private final JavaMethodInvoker methodInvoker = new JavaMethodInvoker();

    public SpringFunctionInvoker(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public Object invoke(Function function, Object... args) {
        SpringFunction springFunction = (SpringFunction) function;

        Object bean = appContext.getBean(springFunction.getBeanName());

        return methodInvoker.invokeMethod(bean, springFunction.getMethod(), true, args);
    }
}
