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
package org.smartparam.engine.core.parameter;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.core.parameter.NamedParamRepositoryBuilder.namedRepository;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParameterProviderTest {

    @Test
    public void shouldIterateRegisteredReposAndReturnParameterFromFirstRepositoryThatReturnsNotNullResult() {
        // given
        BasicParameterProvider provider = new BasicParameterProvider();

        ParamRepository repositoryWithoutParam = mock(ParamRepository.class);
        when(repositoryWithoutParam.load("parameter")).thenReturn(null);

        ParamRepository repositoryWithParam = mock(ParamRepository.class);
        when(repositoryWithParam.load("parameter")).thenReturn(parameter().build());

        provider.register(namedRepository(repositoryWithoutParam).named("without").build());
        provider.register(namedRepository(repositoryWithParam).named("with").build());

        // when
        ParameterFromRepository param = provider.load("parameter");

        // then
        assertThat(param.repositoryName().value()).isEqualTo("with");
    }

    @Test
    public void shouldReturnNullWhenParameterNotFoundInAnyRepository() {
        // given
        BasicParameterProvider provider = new BasicParameterProvider();

        ParamRepository repositoryWithoutParam = mock(ParamRepository.class);
        when(repositoryWithoutParam.load("parameter")).thenReturn(null);

        provider.register(namedRepository(repositoryWithoutParam).build());

        // when
        ParameterFromRepository parameter = provider.load("parameter");

        // then
        assertThat(parameter).isNull();
    }
}
