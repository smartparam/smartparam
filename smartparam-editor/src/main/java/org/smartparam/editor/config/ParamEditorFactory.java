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

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.editor.core.ParamEditor;
import org.smartparam.editor.core.ParamViewer;
import org.smartparam.engine.config.initialization.BasicComponentInitializerRunner;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.engine.core.ParamEngine;

import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamEditorFactory {

    private final PicoContainer container;

    public ParamEditorFactory(ParamEditorConfig config) {
        this.container = createContainer(config);
    }

    public ParamEditor editor() {
        return container.getComponent(ParamEditor.class);
    }

    public ParamViewer viewer() {
        return container.getComponent(ParamViewer.class);
    }

    /**
     * Create instance of ParamEngine, use {@link ParamEditorConfigBuilder}
     * to construct configuration object.
     */
    public PicoContainer createContainer(ParamEditorConfig config) {
        ComponentInitializerRunner initializerRunner = prepareInitializerRunner(config);

        config.addComponent(component(ParamEngine.class, config.paramEngine()));

        MutablePicoContainer picoContainer = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(picoContainer, config.getComponents());

        initializerRunner.runInitializersOnList(picoContainer.getComponents());

        return picoContainer;
    }

    private ComponentInitializerRunner prepareInitializerRunner(ParamEditorConfig config) {
        if (config.getInitializationRunner() == null) {
            ComponentInitializerRunner initializerRunner = new BasicComponentInitializerRunner();
            initializerRunner.registerInitializers(config.getComponentInitializers());
            config.setInitializationRunner(initializerRunner);
        }
        return config.getInitializationRunner();
    }
}
