package org.smartparam.engine.test.builder;

import org.smartparam.engine.model.Function;
import org.smartparam.engine.model.functions.JavaFunction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link Function} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class FunctionMockBuilder {

    private Function function;

    private FunctionMockBuilder() {
        this.function = mock(Function.class);
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
        JavaFunction javaFunction = mock(JavaFunction.class);
        when(javaFunction.getClassName()).thenReturn(clazz.getName());
        when(javaFunction.getMethodName()).thenReturn(functionName);
        when(javaFunction.getTypeCode()).thenReturn("java");

        when(function.getImplementation()).thenReturn(javaFunction);
        return this;
    }

}
