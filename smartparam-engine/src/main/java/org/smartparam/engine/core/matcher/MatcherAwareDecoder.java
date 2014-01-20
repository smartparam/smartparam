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
package org.smartparam.engine.core.matcher;

import org.smartparam.engine.core.type.Type;

/**
 * Converts text representation of matched value to object and vice versa. Complex matchers, i.e. between matcher have
 * in fact a hidden data model. This model resides in format of pattern (for between matcher: *from* - *to*). Matcher
 * converters help in discovering and naming this model for editing purposes. This way it is not necessary to split
 * strings to - user receives Java object instead of plain string.
 *
 * @see org.smartparam.engine.matchers.decoder.BetweenMatcherDecoder
 *
 * @author Adam Dubiel
 */
public interface MatcherAwareDecoder<T> {

    /**
     * Decode string value of given type for given matcher into Java object. Matcher is here so it is possible to
     * access it's runtime properties (like registered separators etc).
     */
    T decode(String value, Type<?> type, Matcher matcher);
}
