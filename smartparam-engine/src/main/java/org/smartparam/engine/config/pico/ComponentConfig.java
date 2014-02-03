/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.config.pico;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public abstract class ComponentConfig {

    private final Set<ComponentDefinition> userComponents = new HashSet<ComponentDefinition>();

    private final Set<ComponentDefinition> defaultComponents = new HashSet<ComponentDefinition>();

    private Set<ComponentDefinition> components;

    protected abstract void injectDefaults(Set<ComponentDefinition> components);

    private Set<ComponentDefinition> createFinalComponents() {
        Set<ComponentDefinition> finalComponents = new HashSet<ComponentDefinition>(userComponents);
        finalComponents.addAll(defaultComponents);
        return finalComponents;
    }

    public Set<ComponentDefinition> getComponents() {
        if (components == null) {
            injectDefaults(defaultComponents);
            components = createFinalComponents();
        }

        return Collections.unmodifiableSet(components);
    }

    public void addComponent(ComponentDefinition component) {
        this.userComponents.add(component);
    }

    public void addAllComponents(Set<ComponentDefinition> components) {
        this.userComponents.addAll(components);
    }
}
