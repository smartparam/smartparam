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
package org.smartparam.engine.config.initialization;

import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class InitializableComponentNotInitialized extends SmartParamException {

    public InitializableComponentNotInitialized(Class<?> componentClazz) {
        super("COMPONENT_NOT_INITIALIZED", "Component " + componentClazz.getSimpleName() + " was not initialized before usage, if you are using it without support"
                + " from any ParamEngine factory, call initialize() after instantiation.");
    }

}
