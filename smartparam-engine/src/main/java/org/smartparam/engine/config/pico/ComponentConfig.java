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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public abstract class ComponentConfig {

    private final List<Object> userComponents = new ArrayList<Object>();

    private final List<Object> defaultComponents = new ArrayList<Object>();

    private List<Object> components;

    protected abstract void injectDefaults(List<Object> components);

    private List<Object> createFinalComponents() {
        List<Object> finalComponents = new ArrayList<Object>(defaultComponents);
        finalComponents.addAll(userComponents);
        return finalComponents;
    }

    public List<Object> getComponents() {
        if (components == null) {
            injectDefaults(defaultComponents);
            components = createFinalComponents();
        }

        return Collections.unmodifiableList(components);
    }

    public void addComponent(Object component) {
        this.userComponents.add(component);
    }

    public void setComponents(List<Object> components) {
        this.userComponents.clear();
        this.userComponents.addAll(components);
    }
}
