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
package org.smartparam.engine.config.pico;

/**
 *
 * @author Adam Dubiel
 */
public class ComponentDefinition {

    private final Class<?> interfaceType;

    private final Object classOrImplementation;

    public static ComponentDefinition component(Class<?> interfaceType, Object classOrImplementation) {
        return new ComponentDefinition(interfaceType, classOrImplementation);
    }

    public ComponentDefinition(Class<?> interfaceType, Object classOrImplementation) {
        this.interfaceType = interfaceType;
        this.classOrImplementation = classOrImplementation;
    }

    public Class<?> interfaceType() {
        return interfaceType;
    }

    public Object classOrImplementation() {
        return classOrImplementation;
    }

    @Override
    public int hashCode() {
        return this.interfaceType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComponentDefinition other = (ComponentDefinition) obj;
        return this.interfaceType == other.interfaceType || (this.interfaceType != null && this.interfaceType.equals(other.interfaceType));
    }

    @Override
    public String toString() {
        return String.format("[ComponentDefinition interface: %s implementation: %s]", interfaceType.getSimpleName(), classOrImplementation);
    }

}
