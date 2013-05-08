package org.smartparam.engine.config;

import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.SmartParamEngine;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamEngineFactory {

    private SmartParamConfigPreparer preparer = new SmartParamConfigPreparer();

    public ParamEngine getParamEngine(SmartParamConfig referenceConfig) {
        SmartParamConfig config = preparer.getPreparedConfig(referenceConfig);
        ConfigInjector configInjector = new ConfigInjector(config);

        ParamEngine paramEngine = new SmartParamEngine();
        configInjector.injectConfig(paramEngine);

        return paramEngine;
    }
}
