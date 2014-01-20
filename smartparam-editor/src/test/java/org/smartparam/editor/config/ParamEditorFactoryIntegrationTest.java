/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.editor.config;

import org.smartparam.editor.core.ParamEditor;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEditorFactoryIntegrationTest {

    @Test
    public void shouldProduceParamEditorAndParamViewerWithDefaultMatcherConverterRegistered() {
        // given
        ParamEngineConfig engineConfig = ParamEngineConfigBuilder.paramEngineConfig().build();
        ParamEngine engine = ParamEngineFactory.paramEngine(engineConfig);

        ParamEditorConfig editorConfig = ParamEditorConfigBuilder.paramEditorConfig(engine).build();
        ParamEditorFactory factory = new ParamEditorFactory(editorConfig);

        // when
        ParamEditor paramEditor = factory.editor();
        ParamViewer paramViewer = factory.viewer();

        // then
        assertThat(paramEditor).isNotNull();
        assertThat(paramViewer).isNotNull();
        assertThat(paramEditor.runtimeConfig().matcherEncoders()).hasSize(4);
        assertThat(paramViewer.runtimeConfig().matcherEncoders()).hasSize(4);
    }

}
