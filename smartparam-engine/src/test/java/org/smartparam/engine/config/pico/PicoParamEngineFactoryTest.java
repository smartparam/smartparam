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

import java.util.Map;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.function.FunctionCache;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.type.Type;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.test.ParamEngineAssertions.*;
import static org.smartparam.engine.config.ParamEngineConfigBuilder.paramEngineConfig;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class PicoParamEngineFactoryTest {

    private ParamEngineFactory paramEngineFactory;

    @BeforeMethod
    public void setUp() {
        paramEngineFactory = new ParamEngineFactory();
    }

    @Test
    public void shouldCreateParamEngineInstanceWithDefaults() {
        // given
        ParamEngineConfig config = paramEngineConfig().build();

        // when
        ParamEngine engine = paramEngineFactory.createParamEngine(config);

        // then
        assertThat(engine.runtimeConfiguration()).hasParamCache().hasFunctionCache();
    }

    @Test
    public void shouldCreateParamEngineInstanceWithTypesInjectedIntoRepositories() {
        // given
        ParamEngineConfig config = paramEngineConfig()
                .withFunctionInvoker("test", mock(FunctionInvoker.class))
                .withFunctionRepository("test", 1, mock(FunctionRepository.class))
                .withParameterRepositories(mock(ParamRepository.class))
                .withMatcher("test", mock(Matcher.class))
                .withType("test", mock(Type.class))
                .build();

        // when
        ParamEngine engine = paramEngineFactory.createParamEngine(config);

        // then
        assertThat(engine.runtimeConfiguration()).hasFunctionRepositories().hasInvokers()
                .hasMachers().hasParamRepositories().hasTypes();
    }

    @Test
    public void shouldAllowOnOverwritingDefaultImplementationsWithCustomBeans() {
        // given
        ParamEngineConfig config = paramEngineConfig().withFunctionCache(new DummyFunctionCache()).build();

        // when
        ParamEngine engine = paramEngineFactory.createParamEngine(config);

        // then
        assertThat(engine.runtimeConfiguration()).hasFunctionCache(DummyFunctionCache.class);
    }

    private class DummyFunctionCache implements FunctionCache {

        public void put(String functionName, Function function) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void putAll(Map<String, Function> functions) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Function get(String functionName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void invalidate(String functionName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void invalidate() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
