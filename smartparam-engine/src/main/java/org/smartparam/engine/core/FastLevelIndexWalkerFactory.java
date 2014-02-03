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
package org.smartparam.engine.core;

import org.smartparam.engine.core.index.FastLevelIndexWalker;
import org.smartparam.engine.core.index.LevelIndexWalker;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
class FastLevelIndexWalkerFactory implements LevelIndexWalkerFactory {

    @Override
    public LevelIndexWalker<PreparedEntry> create(PreparedParameter preparedParameter, String... values) {
        return new FastLevelIndexWalker<PreparedEntry>(preparedParameter.getIndex(), values);
    }

}
