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
import org.smartparam.engine.core.index.Star;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
@ParamMatcher(TypeMatcher.TYPE)
public class TypeMatcher implements Matcher {

    public static final String TYPE = "equals/type";

    @Override
    public <T extends ValueHolder> boolean matches(String value, String pattern, Type<T> type) {
        if (Star.SYMBOL.equals(pattern)) {
            return true;
        }

        T patternObject = type.decode(pattern);
        T valueObject = type.decode(value);

        if (patternObject.isComparable()) {
            return patternObject.compareTo(valueObject) == 0;
        }
        return patternObject.getValue().equals(valueObject.getValue());
    }

}
