package org.smartparam.spring;

import org.smartparam.engine.config.SmartParamConfig;
import org.smartparam.engine.config.SmartParamEngineFactory;
import org.smartparam.engine.core.engine.ParamEngine;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SpringParamEngineFactory implements FactoryBean<ParamEngine> {

    private SmartParamConfig config;

    public ParamEngine getObject() throws Exception {
        SmartParamEngineFactory factory = new SmartParamEngineFactory();
        return factory.getParamEngine(config);
    }

    public Class<?> getObjectType() {
        return ParamEngine.class;
    }

    public boolean isSingleton() {
        return false;
    }

    public void setConfig(SmartParamConfig config) {
        this.config = config;
    }
}
