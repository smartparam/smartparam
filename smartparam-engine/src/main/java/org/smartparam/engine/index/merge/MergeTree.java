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
package org.smartparam.engine.index.merge;

import java.util.Arrays;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class MergeTree<T> {

    private PreparedParameter parameter;

    private final MergeTreeNode<PreparedEntry> root;

    public MergeTree() {
        this.root = new MergeTreeNode<PreparedEntry>();
    }

    public void add(PreparedEntry entry) {
        root.add(getFirstNLevels(entry, parameter.getLevelCount()), entry, 0);
    }

    private String[] getFirstNLevels(PreparedEntry entry, int levelCount) {
        return Arrays.copyOf(entry.getLevels(), levelCount);
    }
}
