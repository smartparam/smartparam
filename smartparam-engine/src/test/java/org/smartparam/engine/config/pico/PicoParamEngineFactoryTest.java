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
package org.smartparam.engine.config.pico;

import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.config.pico.PicoParamEngineConfig;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.type.Type;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.config.pico.ParamEngineConfigBuilder.paramEngineConfig;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class PicoParamEngineFactoryTest {

    private ParamEngineFactory paramEngineFactory;

    @BeforeMethod
    public void setUp() {
        paramEngineFactory = new PicoParamEngineFactory();
    }

    @Test
    public void shouldCreateParamEngineInstanceWithDefaults() {
        // given
        PicoParamEngineConfig config = paramEngineConfig().build();

        // when
        ParamEngine engine = paramEngineFactory.createParamEngine(config);

        // then
        assertThat(engine.getConfiguration()).hasParamCache().hasFunctionCache();
    }

    @Test
    public void shouldCreateParamEngineInstanceWithTypesInjectedIntoRepositories() {
        // given
        PicoParamEngineConfig config = paramEngineConfig()
                .withFunctionInvoker("test", mock(FunctionInvoker.class))
                .withFunctionRepository("test", 1, mock(FunctionRepository.class))
                .withParameterRepositories(mock(ParamRepository.class))
                .withMatcher("test", mock(Matcher.class))
                .withType("test", mock(Type.class))
                .build();

        // when
        ParamEngine engine = paramEngineFactory.createParamEngine(config);

        // then
        assertThat(engine.getConfiguration()).hasFunctionRepositories().hasInvokers()
                .hasMachers().hasParamRepositories().hasTypes();
    }
}
