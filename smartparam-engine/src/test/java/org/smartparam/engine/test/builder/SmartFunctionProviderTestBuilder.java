package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.service.BasicFunctionProvider;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartFunctionProviderTestBuilder {

    private BasicFunctionProvider functionProvider;

    private SmartFunctionProviderTestBuilder() {
        functionProvider = new BasicFunctionProvider();
    }

    public static SmartFunctionProviderTestBuilder functionProvider() {
        return new SmartFunctionProviderTestBuilder();
    }

    public BasicFunctionProvider build() {
        return functionProvider;
    }
}
