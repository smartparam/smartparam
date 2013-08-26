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

import java.util.List;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.config.initialization.MethodScannerInitializer;
import org.smartparam.engine.config.pico.PicoParamEngineConfig;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.config.initialization.TypeScannerInitializer;
import org.smartparam.engine.config.pico.PicoParamEngineFactory;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.repository.ParamRepository;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author Adam Dubiel
 */
public class SpringParamEngineFactory implements FactoryBean<ParamEngine> {

    private PicoParamEngineConfig config;

    private ParamRepository paramRepository;

    private boolean scanAnnotations;

    private List<String> packagesToScan;

    @Override
    public ParamEngine getObject() throws Exception {
        if (config == null) {
            config = new PicoParamEngineConfig();
        }
        if (paramRepository != null) {
            config.getParameterRepositories().add(paramRepository);
        }

        if (scanAnnotations) {
            injectComponentInitializers();
        }

        ParamEngineFactory factory = new PicoParamEngineFactory();
        return factory.createParamEngine(config);
    }

    private void injectComponentInitializers() {
        PackageList packageList = new PackageList();
        packageList.setPackages(packagesToScan);

        config.getComponentInitializers().add(new TypeScannerInitializer(packageList));
        config.getComponentInitializers().add(new MethodScannerInitializer(packageList));
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

    public void setConfig(PicoParamEngineConfig config) {
        this.config = config;
    }

    public void setScanAnnotations(boolean scanAnnotations) {
        this.scanAnnotations = scanAnnotations;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
}
