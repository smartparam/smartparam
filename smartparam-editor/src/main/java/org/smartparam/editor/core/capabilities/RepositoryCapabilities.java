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
package org.smartparam.editor.core.capabilities;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class RepositoryCapabilities {

    private final Set<String> capabilities = new HashSet<String>();

    public RepositoryCapabilities(Object... capabilities) {
        for (Object capability : capabilities) {
            this.capabilities.add(capability.toString());
        }
    }

    public boolean capableOf(String capability) {
        return capabilities.contains(capability);
    }
}
