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
package org.smartparam.engine.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class IndexTraversalConfig implements Iterable<IndexLevelDescriptor> {

    private final Map<String, IndexLevelDescriptor> descriptors = new LinkedHashMap<String, IndexLevelDescriptor>();

    private final List<String> levelNames = new ArrayList<String>();

    public IndexTraversalConfig() {
    }

    public void addLevel(IndexLevelDescriptor descriptor) {
        this.levelNames.add(descriptor.name());
        descriptors.put(descriptor.name(), descriptor);
    }

    public IndexLevelDescriptor descriptorFor(String levelName) {
        return descriptors.get(levelName);
    }

    public IndexLevelDescriptor descriptorFor(int levelDepth) {
        return descriptors.get(levelNames.get(levelDepth));
    }

    public boolean greedy(int levelDepth) {
        return descriptorFor(levelDepth).greedy();
    }

    public Matcher effectiveMatcher(int levelDepth) {
        return descriptorFor(levelDepth).effectiveMatcher();
    }

    public Type<?> type(int levelDepth) {
        return descriptorFor(levelDepth).type();
    }

    @Override
    public Iterator<IndexLevelDescriptor> iterator() {
        return descriptors.values().iterator();
    }
}
