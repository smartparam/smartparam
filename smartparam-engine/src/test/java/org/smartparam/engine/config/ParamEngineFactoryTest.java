/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.config;

import org.smartparam.engine.config.initialization.InitializableComponent;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEngineFactoryTest {

    @Test
    public void shouldCreateBasicParamEngine() {
        // given
        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig().build();

        // when
        ParamEngine engine = ParamEngineFactory.paramEngine(config);

        // then
        assertThat(engine).isNotNull();
    }

    @Test
    public void shouldInitializeInitializableParamRepositories() {
        // given
        InitializableParamRepository repository = mock(InitializableParamRepository.class);
        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig()
                .withParameterRepository(repository)
                .build();

        // when
        ParamEngineFactory.paramEngine(config);

        // then
        verify(repository).initialize();
    }

    private static interface InitializableParamRepository extends ParamRepository, InitializableComponent {
    }
}
