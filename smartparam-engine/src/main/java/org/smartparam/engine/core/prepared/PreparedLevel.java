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
package org.smartparam.engine.core.prepared;

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.function.Function;

/**
 * Compiled parameter level. After compilation, level object contains resolved
 * references to type, matcher and levelCreator instead of their codes which
 * makes the object smaller and easier to use (no need to resolve those fields
 * each time parameter is called.
 *
 * @see org.smartparam.engine.model.Level
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedLevel {

    private String name;

    private Type<?> type;

    private boolean array;

    private Matcher matcher;

    private Function levelCreator;

    /**
     * Creates immutable instance.
     *
     * @param name         level's name
     * @param array        whether this level contains array
     * @param type         level's type code
     * @param matcher      level's matcher code
     * @param levelCreator function resolving actual level value
     */
    public PreparedLevel(String name, boolean array, Type<?> type, Matcher matcher, Function levelCreator) {
        this.name = name;
        this.type = type;
        this.array = array;
        this.matcher = matcher;
        this.levelCreator = levelCreator;
    }

    public String getName() {
        return name;
    }

    public boolean isArray() {
        return array;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public Type<?> getType() {
        return type;
    }

    public Function getLevelCreator() {
        return levelCreator;
    }
}
