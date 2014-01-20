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
package org.smartparam.editor.core.matcher;

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 * Converts object representation of matched value to string. Complex matchers, i.e. between matcher have
 * in fact a hidden data model. This model resides in format of pattern (for between matcher: *from* - *to*). Matcher
 * converters help in discovering and naming this model for editing purposes. This way it is not necessary to concatenate
 * strings to create a pattern, user can pass an object understood by converter which will be converted to string internally.
 *
 * For between matcher instead of "*from* - *to*" user can pass Range object.
 *
 * Matcher converters are matched with matchers by their name. Converter methods should produce reversable results, meaning:
 * <pre>
 * decode(encode(obj)) == obj
 * </pre>
 *
 * @see org.smartparam.editor.matcher.BetweenMatcherEncoder
 *
 * @author Adam Dubiel
 */
public interface MatcherAwareEncoder<T> {

    /**
     * Encode object into string recognized by matcher.
     */
    String encode(T object, Type<?> type, Matcher matcher);
}
