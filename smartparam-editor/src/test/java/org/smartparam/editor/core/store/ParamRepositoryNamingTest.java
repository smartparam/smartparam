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
package org.smartparam.editor.core.store;

import org.smartparam.editor.core.store.ParamRepositoryNamingBuilder;
import org.smartparam.editor.core.store.ParamRepositoryNaming;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParamRepositoryNamingTest {

    @Test
    public void shouldReturnFirstRegisteredNameWhenAskingForNameWithoutOccurenceIndex() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming()
                .registerAs(ParamRepository.class, "first", "second").build();

        // when
        RepositoryName name = naming.name(ParamRepository.class);

        // then
        assertThat(name).isEqualTo(RepositoryName.from("first"));
    }

    @Test
    public void shouldReturnTrueIfCustomNameRegisteredForFirstOccurenceOfRepository() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming()
                .registerAs(ParamRepository.class, "first", "second").build();

        // when
        boolean hasCustomName = naming.hasCustomNameFor(ParamRepository.class);

        // then
        assertThat(hasCustomName).isTrue();
    }

    @Test
    public void shouldGenerateNameBasedOnSimpleClassNameWhenNoCustomNameRegisteredForRepository() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming().build();

        // when
        RepositoryName name = naming.name(ParamRepository.class);

        // then
        assertThat(name).isEqualTo(RepositoryName.from("ParamRepository"));
    }

    @Test
    public void shouldReturnFalseIfNoCustomNameRegisteredForFirstOccurenceOfRepository() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming().build();

        // when
        boolean hasCustomName = naming.hasCustomNameFor(ParamRepository.class);

        // then
        assertThat(hasCustomName).isFalse();
    }

    @Test
    public void shouldReturnRegisteredNameWhenAskingForNameAtOccurenceIndex() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming()
                .registerAs(ParamRepository.class, "first", "second").build();

        // when
        RepositoryName name = naming.name(ParamRepository.class, 1);

        // then
        assertThat(name).isEqualTo(RepositoryName.from("second"));
    }

    @Test
    public void shouldReturnTrueIfCustomNameRegisteredForGivenOccurenceOfRepository() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming()
                .registerAs(ParamRepository.class, "first", "second").build();

        // when
        boolean hasCustomName = naming.hasCustomNameFor(ParamRepository.class, 1);

        // then
        assertThat(hasCustomName).isTrue();
    }

    @Test
    public void shouldGenerateNameBasedOnSimpleClassAndOccurenceNameWhenNoCustomNameRegisteredForGivenOccurenceOfRepository() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming().build();

        // when
        RepositoryName name = naming.name(ParamRepository.class, 4);

        // then
        assertThat(name).isEqualTo(RepositoryName.from("ParamRepository4"));
    }

    @Test
    public void shouldReturnFalseIfNoCustomNameRegisteredForGivenOccurenceOfRepository() {
        // given
        ParamRepositoryNaming naming = ParamRepositoryNamingBuilder.repositoryNaming().build();

        // when
        boolean hasCustomName = naming.hasCustomNameFor(ParamRepository.class, 3);

        // then
        assertThat(hasCustomName).isFalse();
    }
}
