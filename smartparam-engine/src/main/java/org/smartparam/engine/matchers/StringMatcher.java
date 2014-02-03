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
package org.smartparam.engine.matchers;

import org.smartparam.engine.annotated.annotations.ParamMatcher;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.core.type.Type;

/**
 * Basic equality matcher to compare two strings. It is possible to toggle
 * on case sensitivity.
 *
 * @author Przemek Hertel
 */
@ParamMatcher(StringMatcher.STRING)
public class StringMatcher implements Matcher {

    public static final String STRING = "equals/string";

    private boolean caseSensitive = false;

    public StringMatcher() {
    }

    public StringMatcher(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public <T extends ValueHolder> boolean matches(String value, String pattern, Type<T> type) {
        if (value != null) {
            return caseSensitive ? value.equals(pattern) : value.equalsIgnoreCase(pattern);
        }
        return false;
    }

}
