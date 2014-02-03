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
package org.smartparam.engine.report.tree;

import java.util.HashMap;
import java.util.Map;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreeValueDescriptor {

    private final Map<String, Type<?>> types = new HashMap<String, Type<?>>();

    private final Map<String, Integer> nameToIndexMapping = new HashMap<String, Integer>();

    public void add(int levelIndex, String levelName, Type<?> type) {
        this.types.put(levelName, type);
        nameToIndexMapping.put(levelName, levelIndex);
    }

    public int indexOf(String levelName) {
        if(!nameToIndexMapping.containsKey(levelName)) {
            throw new NoIndexMappedToLevelNameException(levelName);
        }
        return nameToIndexMapping.get(levelName);
    }

    public Type<?> type(String outputLevelName) {
        return types.get(outputLevelName);
    }
}
