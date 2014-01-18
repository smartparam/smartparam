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

import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.smartparam.editor.core.EditableParamRepository;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.parameter.NamedParamRepositoryBuilder;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.core.parameter.NamedParamRepositoryBuilder.namedRepository;

/**
 *
 * @author Adam Dubiel
 */
public class RepositoryStoreTest {

    @Test
    public void shouldFilterOutRepositoriesAssignableFromStoredClassWhenConstructingStore() {
        // given
        ParamRepository editableRepository = new FakeEditableParamRepository();

        List<NamedParamRepository> repositories = Arrays.asList(
                namedRepository(editableRepository).named("fakeRepo").build(),
                NamedParamRepositoryBuilder.namedRepository(new FakeViewableParamRepository()).build()
        );

        // when
        RepositoryStore<EditableParamRepository> store = new RepositoryStore<EditableParamRepository>(
                repositories, EditableParamRepository.class);

        // then
        assertThat(store.storedRepositories()).containsOnly(new RepositoryName("fakeRepo"));
    }

    @Test
    public void shouldThrowInvalidSourceExceptionWhenTryingToGetUnknownRepository() {
        // given
        List<NamedParamRepository> repositories = Arrays.asList(
                namedRepository(new FakeEditableParamRepository()).build()
        );

        RepositoryStore<EditableParamRepository> store = new RepositoryStore<EditableParamRepository>(repositories, EditableParamRepository.class);

        // when
        try {
            store.get(RepositoryName.from("invalid name"));
            Assertions.fail("Expected InvalidSourceRepositoryException.");
        } catch (InvalidSourceRepositoryException invalidRepoException) {
        }
    }
}
