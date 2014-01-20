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
package org.smartparam.editor.matcher;

import org.smartparam.engine.core.index.Star;
import org.smartparam.editor.core.matcher.MatcherAwareEncoder;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class EmptyMatcherEncoder implements MatcherAwareEncoder<Object> {

    @Override
    @SuppressWarnings("unchecked")
    public String encode(Object object, Type type, Matcher matcher) {
        if (object instanceof Star) {
            return "*";
        }

        if (type != null) {
            return type.encode(type.convert(object));
        }
        return object == null ? "" : object.toString();
    }

}
