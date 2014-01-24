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

import java.util.*;
import org.smartparam.editor.core.*;
import org.smartparam.editor.core.entry.ParameterEntryMapConverter;
import org.smartparam.engine.config.initialization.ComponentInitializer;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.ParamEngine;

import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public class ParamEditorConfig extends ComponentConfig {

    private final ParamEngine paramEngine;

    private ComponentInitializerRunner initializationRunner;

    private final List<ComponentInitializer> componentInitializers = new ArrayList<ComponentInitializer>();

    public ParamEditorConfig(ParamEngine paramEngine) {
        this.paramEngine = paramEngine;
    }

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(ParamEditor.class, BasicParamEditor.class));
        components.add(component(ParamViewer.class, BasicParamViewer.class));
        components.add(component(ParameterEntryMapConverter.class, ParameterEntryMapConverter.class));
    }

    ParamEngine paramEngine() {
        return paramEngine;
    }

    protected void addComponentInitializers(List<ComponentInitializer> componentInitializers) {
        this.componentInitializers.addAll(componentInitializers);
    }

    public List<ComponentInitializer> getComponentInitializers() {
        return Collections.unmodifiableList(componentInitializers);
    }

    public ComponentInitializerRunner getInitializationRunner() {
        return initializationRunner;
    }

    public void setInitializationRunner(ComponentInitializerRunner initializationRunner) {
        this.initializationRunner = initializationRunner;
    }
}
