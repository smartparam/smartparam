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

import org.smartparam.engine.annotated.annotations.ParamFunctionRepository;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineModule;
import org.smartparam.spring.function.SpringFunctionInvoker;
import org.smartparam.spring.function.SpringFunctionRepository;
import org.smartparam.spring.type.SpringBeanType;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Adam Dubiel
 */
public class SpringModule implements ParamEngineModule {

    private final ApplicationContext applicationContext;

    public SpringModule(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void registerSelf(ParamEngineConfigBuilder configBuilder) {
        configBuilder
                .withFunctionInvoker(SpringFunctionRepository.FUNCTION_TYPE, new SpringFunctionInvoker(applicationContext))
                .withFunctionRepository(SpringFunctionRepository.FUNCTION_TYPE, ParamFunctionRepository.DEFAULT_ORDER, new SpringFunctionRepository())
                .withType(SpringBeanType.BEAN_TYPE, new SpringBeanType(applicationContext));
    }

}
