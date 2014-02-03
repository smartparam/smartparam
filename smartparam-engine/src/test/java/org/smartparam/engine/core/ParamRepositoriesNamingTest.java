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
package org.smartparam.engine.core;

import java.util.Arrays;
import java.util.List;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.repository.RepositoryName;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.smartparam.engine.core.parameter.NamedParamRepositoryBuilder.namedRepository;

/**
 *
 * @author Adam Dubiel
 */
public class ParamRepositoriesNamingTest {

//    @Test
//    public void shouldCreateNamingOutOfListOfNamedParameters() {
//        // given
//        ParamRepository repository1 = mock(ParamRepository.class);
//        ParamRepository repository2 = mock(ParamRepository.class);
//        List<NamedParamRepository> namedRepositories = Arrays.asList(
//                namedRepository(repository1).named("repository-one").build(),
//                namedRepository(repository2).named("repository-two").build()
//        );
//
//        // when
//        ParamRepositoriesNaming repositoriesNaming = new ParamRepositoriesNaming(namedRepositories);
//
//        // then
//        assertThat(repositoriesNaming.find("repository-one")).isSameAs(repository1);
//        assertThat(repositoriesNaming.find(RepositoryName.from("repository-two"))).isSameAs(repository2);
//    }

}
