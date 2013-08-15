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
package org.smartparam.engine.test.builder;

import java.lang.reflect.Method;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.model.function.JavaFunction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 * {@link Function} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class FunctionMockBuilder {

    private JavaFunction function;

    private FunctionMockBuilder() {
        this.function = mock(JavaFunction.class);
    }

    public static FunctionMockBuilder function() {
        return new FunctionMockBuilder();
    }

    public Function get() {
        return function;
    }

    public FunctionMockBuilder withType(String type) {
        when(function.getType()).thenReturn(type);
        return this;
    }

    public FunctionMockBuilder withName(String name) {
        when(function.getName()).thenReturn(name);
        return this;
    }

    public FunctionMockBuilder withJavaImplementation(Class<?> clazz, String functionName) {
        when(function.getType()).thenReturn("java");
        when(function.getName()).thenReturn(functionName);

        try {
            Method method = clazz.getMethod(functionName, DefaultContext.class);

            when(function.getMethod()).thenReturn(method);
        }
        catch(NoSuchMethodException e) {
            throw new SmartParamException(e);
        }

        return this;
    }

}
