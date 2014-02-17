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

import java.util.Arrays;
import org.mockito.Mockito;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.smartparam.engine.core.ParamEngineRuntimeConfigAssert.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class SpringParamEngineFactoryTest {

    private SpringParamEngineFactory springParamEngineFactory;

    @BeforeMethod
    public void initialize() {
        springParamEngineFactory = new SpringParamEngineFactory();
    }

    @Test
    public void shouldReturnParamEngineWithAnnotationScanningEnabled() throws Exception {
        // given
        springParamEngineFactory.setPackagesToScan(Arrays.asList("test"));

        // when
        ParamEngine paramEngine = springParamEngineFactory.getObject();

        // then
        assertThat(paramEngine.runtimeConfiguration()).hasFunctionRepositories().hasInvokers()
                .hasMatchers().hasTypes();
    }

    @Test
    public void shouldCreateNewConfigObjectIfNoneSpecified() throws Exception {
        // when
        ParamEngine paramEngine = springParamEngineFactory.getObject();

        // then
        assertThat(paramEngine.runtimeConfiguration()).hasParamCache().hasFunctionCache();
    }

    @Test
    public void shouldInsertProvidedRepositoryIntoConfig() throws Exception {
        // given
        ParamRepository repository = Mockito.mock(ParamRepository.class);
        springParamEngineFactory.setParamRepository(repository);

        // when
        ParamEngine paramEngine = springParamEngineFactory.getObject();

        // then
        assertThat(paramEngine.runtimeConfiguration()).hasRepository(repository);
    }
}
