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
package org.smartparam.engine.core.parameter.level;

/**
 * Depth of parameter.
 * In tabular view: how many columns there are.
 * In "if"-tree: indentation depth.
 *
 * Level MIGHT contain unique key, but this depends on repository implementation, might
 * be useful for auditing purposes.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface Level {

    /**
     * Returns optional repository-scope unique identifier of Level.
     */
    LevelKey getKey();

    /**
     * Returns name of level, internally used to load value by output parameter
     * name.
     *
     * @return level name
     */
    String getName();

    /**
     * Returns function for evaluating value of level using current context.
     *
     * @return function registered in function repository
     */
    String getLevelCreator();

    /**
     * Get type of values stored in level.
     *
     * @return level value type
     */
    String getType();

    /**
     * Is level an array of values.
     *
     * @return is array of values
     */
    boolean isArray();

    /**
     * Get code of matcher, that is used to match level value against the pattern.
     *
     * @return matcher code
     */
    String getMatcher();
}
