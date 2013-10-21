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

import java.util.List;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;

/**
 *
 * @author Adam Dubiel
 */
public final class PicoContainerUtil {

    private PicoContainerUtil() {
    }

    public static MutablePicoContainer createContainer() {
        return new DefaultPicoContainer(new Caching());
    }

    public static void injectImplementations(MutablePicoContainer container, Object... implementations) {
        for (Object object : implementations) {
            container.addComponent(object);
        }
    }

    public static void injectImplementations(MutablePicoContainer container, List<Object> implementations) {
        for (Object object : implementations) {
            container.addComponent(object);
        }
    }
}
