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
import java.util.List;
import org.smartparam.engine.core.index.LevelNode;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleLevelLeafValuesExtractor<T> implements LevelLeafValuesExtractor<T> {

    @Override
    public List<T> extract(CustomizableLevelIndexWalker<T> indexWalker, List<LevelNode<T>> nodes) {
        List<T> values = new ArrayList<T>();
        for (LevelNode<T> node : nodes) {
            values.addAll(node.getLeafList());
        }
        return values;
    }

}
