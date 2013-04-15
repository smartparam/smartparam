package org.smartparam.spring.mockBuilders;

import org.smartparam.engine.model.function.Function;
import org.smartparam.spring.function.SpringFunction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class SpringFunctionMockBuilder {

    private SpringFunction springFunction;

    private SpringFunctionMockBuilder() {
        this.springFunction = mock(SpringFunction.class);
    }

    public static SpringFunctionMockBuilder function() {
        return new SpringFunctionMockBuilder();
    }

    public static SpringFunction function(String beanName, String methodName) {
        return new SpringFunctionMockBuilder().withBeanName(beanName).withMethodName(methodName).get();
    }

    public SpringFunction get() {
        return springFunction;
    }

    public SpringFunctionMockBuilder withBeanName(String beanName) {
        when(springFunction.getBeanName()).thenReturn(beanName);
        return this;
    }

    public SpringFunctionMockBuilder withMethodName(String methodName) {
        when(springFunction.getMethodName()).thenReturn(methodName);
        return this;
    }

    public SpringFunctionMockBuilder asImplementationFor(Function function) {
        when(function.getImplementation()).thenReturn(springFunction);
        return this;
    }
}
