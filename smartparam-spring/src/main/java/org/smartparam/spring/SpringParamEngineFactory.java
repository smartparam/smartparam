package org.smartparam.spring;

import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.engine.ParamEngine;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SpringParamEngineFactory implements FactoryBean<ParamEngine> {

    private ParamEngineConfig config;

    @Override
    public ParamEngine getObject() throws Exception {
        ParamEngineFactory factory = new ParamEngineFactory();
        return factory.createParamEngine(config);
    }

    @Override
    public Class<?> getObjectType() {
        return ParamEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setConfig(ParamEngineConfig config) {
        this.config = config;
    }
}
