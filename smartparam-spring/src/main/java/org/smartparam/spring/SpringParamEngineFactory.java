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
package org.smartparam.spring;

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Adam Dubiel
 */
public class SpringParamEngineFactory implements FactoryBean<ParamEngine>, ApplicationContextAware {

    private ApplicationContext appContext;

    private ParamRepository paramRepository;

    private boolean scanAnnotations = true;

    private final List<String> packagesToScan = new ArrayList<String>();

    @Override
    public ParamEngine getObject() {
        ParamEngineConfigBuilder configBuilder = ParamEngineConfigBuilder.paramEngineConfig();
        if (paramRepository != null) {
            configBuilder.withParameterRepositories(paramRepository);
        }

        configBuilder.withPackagesToScan(new PackageList(packagesToScan));
        if (!scanAnnotations) {
            configBuilder.withAnnotationScanDisabled();
        }

        configBuilder.registerModule(new SpringModule(appContext));

        return new ParamEngineFactory().createParamEngine(configBuilder.build());
    }

    @Override
    public Class<?> getObjectType() {
        return ParamEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    public void setScanAnnotations(boolean scanAnnotations) {
        this.scanAnnotations = scanAnnotations;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan.clear();
        this.packagesToScan.addAll(packagesToScan);
    }

    public void setApplicationContext(ApplicationContext appContext) {
        this.appContext = appContext;
    }
}
