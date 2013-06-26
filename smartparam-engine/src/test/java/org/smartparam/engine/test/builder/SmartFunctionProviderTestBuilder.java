package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.service.SmartFunctionProvider;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartFunctionProviderTestBuilder {

    private SmartFunctionProvider functionProvider;

    private SmartFunctionProviderTestBuilder() {
        functionProvider = new SmartFunctionProvider();
    }

    public static SmartFunctionProviderTestBuilder functionProvider() {
        return new SmartFunctionProviderTestBuilder();
    }

    public SmartFunctionProvider build() {
        return functionProvider;
    }

    public SmartFunctionProviderTestBuilder withAnnotationScan() {
        functionProvider.setScanAnnotations(true);
        return this;
    }
}
