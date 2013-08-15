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
package org.smartparam.engine.core.index;

import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 * Matcher role is to check if provided value matches pattern. Each evaluation
 * takes place in type context, so same matcher can handle values differently
 * based on type.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface Matcher {

    /**
     * Check if value matches the pattern. Type can be used to parse value or
     * to differentiate matchers behavior depending on value type. Type is
     * defined per parameter {@link org.smartparam.engine.model.Level}.
     *
     * @param <T> level type
     * @param value value form query (provided by user)
     * @param pattern pattern from parameter matrix (can be '*')
     * @param type type of value, might be null if none was set
     * @return true if value matched, false otherwise
     */
    <T extends AbstractHolder> boolean matches(String value, String pattern, Type<T> type);
}
