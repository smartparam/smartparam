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
package org.smartparam.editor.editor;

import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.store.FakeEditableParamRepository;
import org.smartparam.editor.viewer.ViewableParamRepository;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.smartparam.editor.store.ParamRepositoryNamingBuilder.repositoryNaming;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamEditorTest {

    private static final RepositoryName REPOSITORY_NAME = RepositoryName.from("repository");

    private EditableParamRepository editableRepository;

    private PreparedParamCache cache;

    private BasicParamEditor paramEditor;

    @BeforeMethod
    public void setUp() {
        cache = mock(PreparedParamCache.class);
        editableRepository = mock(EditableParamRepository.class);
        ParamRepository viewableRepository = mock(ViewableParamRepository.class);

        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig()
                .withParameterRepositories(editableRepository, viewableRepository)
                .withParameterCache(cache)
                .withAnnotationScanDisabled().build();
        ParamEngine paramEngine = ParamEngineFactory.paramEngine(config);

        paramEditor = new BasicParamEditor(paramEngine, repositoryNaming().registerAs(editableRepository.getClass(), REPOSITORY_NAME.name()).build());
    }

    @Test
    public void shouldRegisterAllEditableRepositoriesFromParamEngineInOrderWhenCreatingNewEditor() {
        // given
        ParamRepository viewableRepository = mock(ViewableParamRepository.class);
        ParamRepository editableRepository1 = new FakeEditableParamRepository();
        ParamRepository editableRepository2 = new FakeEditableParamRepository();

        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig()
                .withParameterRepositories(editableRepository1, viewableRepository, editableRepository2)
                .withAnnotationScanDisabled().build();
        ParamEngine paramEngine = ParamEngineFactory.paramEngine(config);

        // when
        BasicParamEditor localParamEditor = new BasicParamEditor(paramEngine, repositoryNaming()
                .registerAs(FakeEditableParamRepository.class, "fake1", "fake2").build());

        // then
        assertThat(localParamEditor.repositories()).containsOnly(RepositoryName.from("fake1"), RepositoryName.from("fake2"));
    }

    @Test
    public void shouldCreateNewParameterInGivenRepository() {
        // given
        Parameter parameter = new SimpleParameter();

        // when
        paramEditor.createParameter(REPOSITORY_NAME, parameter);

        // then
        verify(editableRepository).createParameter(parameter);
    }
}
