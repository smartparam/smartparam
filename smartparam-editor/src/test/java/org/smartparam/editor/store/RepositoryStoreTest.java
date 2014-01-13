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
package org.smartparam.editor.store;

import java.util.ArrayList;
import java.util.List;
import org.smartparam.editor.editor.EditableParamRepository;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.viewer.ViewableParamRepository;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.editor.store.ParamRepositoryNamingBuilder.repositoryNaming;

/**
 *
 * @author Adam Dubiel
 */
public class RepositoryStoreTest {

    @Test
    public void shouldFilterOutRepositoriesAssignableFromStoredClassWhenConstructingStore() {
        // given
        ParamRepository editableRepository = new FakeEditableParamRepository();

        List<ParamRepository> repositories = new ArrayList<ParamRepository>();
        repositories.add(editableRepository);
        repositories.add(new FakeViewableParamRepository());

        ParamRepositoryNaming naming = repositoryNaming().registerAs(FakeEditableParamRepository.class, "fakeRepo").build();

        // when
        RepositoryStore<EditableParamRepository> store = new RepositoryStore<EditableParamRepository>(
                repositories, naming, EditableParamRepository.class);

        // then
        assertThat(store.storedRepositories()).containsOnly(new RepositoryName("fakeRepo"));
    }

    @Test
    public void shouldNameRepositoriesAccordingToNamingStrategy() {
        List<ParamRepository> repositories = new ArrayList<ParamRepository>();
        repositories.add(new FakeEditableParamRepository());
        repositories.add(new FakeViewableParamRepository());
        repositories.add(new FakeViewableParamRepository());

        ParamRepositoryNaming naming = repositoryNaming()
                .registerAs(FakeEditableParamRepository.class, "fakeEditableRepo")
                .build();

        // when
        RepositoryStore<ViewableParamRepository> store = new RepositoryStore<ViewableParamRepository>(
                repositories, naming, ViewableParamRepository.class);

        // then
        assertThat(store.storedRepositories()).containsOnly(
                new RepositoryName("fakeEditableRepo"),
                new RepositoryName("FakeViewableParamRepository"),
                new RepositoryName("FakeViewableParamRepository1"));
    }

    @Test
    public void shouldThrowInvalidSourceExceptionWhenTryingToGetUnknownRepository() {
        // given
        List<ParamRepository> repositories = new ArrayList<ParamRepository>();
        repositories.add(new FakeEditableParamRepository());

        ParamRepositoryNaming naming = ParamRepositoryNaming.empty();

        RepositoryStore<EditableParamRepository> store = new RepositoryStore<EditableParamRepository>(repositories, naming, EditableParamRepository.class);

        // when
        catchException(store).get(RepositoryName.from("invalid name"));

        // then
        assertThat(caughtException()).isInstanceOf(InvalidSourceRepositoryException.class);
    }
}
