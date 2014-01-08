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
import static org.fest.assertions.api.Assertions.assertThat;

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

        // when
        RepositoryStore<EditableParamRepository> store = new RepositoryStore<EditableParamRepository>(
                repositories, EditableParamRepository.class);

        // then
        assertThat(store.storedRepositories()).containsOnly(new RepositoryName("FakeEditableParamRepository"));
    }

    @Test
    public void shouldNameRepositoriesAccordingToTheirClassSimpleName() {
        List<ParamRepository> repositories = new ArrayList<ParamRepository>();
        repositories.add(new FakeEditableParamRepository());
        repositories.add(new FakeViewableParamRepository());

        // when
        RepositoryStore<ViewableParamRepository> store = new RepositoryStore<ViewableParamRepository>(
                repositories, ViewableParamRepository.class);

        // then
        assertThat(store.storedRepositories()).containsOnly(
                new RepositoryName("FakeEditableParamRepository"),
                new RepositoryName("FakeViewableParamRepository"));
    }

    @Test
    public void shouldUseNumberingStartingFromOneForEachNextOccurenceOfRepositoryWithSameClass() {
        List<ParamRepository> repositories = new ArrayList<ParamRepository>();
        repositories.add(new FakeEditableParamRepository());
        repositories.add(new FakeEditableParamRepository());
        repositories.add(new FakeEditableParamRepository());

        // when
        RepositoryStore<EditableParamRepository> store = new RepositoryStore<EditableParamRepository>(
                repositories, EditableParamRepository.class);

        // then
        assertThat(store.storedRepositories()).containsOnly(
                new RepositoryName("FakeEditableParamRepository"),
                new RepositoryName("FakeEditableParamRepository1"),
                new RepositoryName("FakeEditableParamRepository2"));
    }
}
