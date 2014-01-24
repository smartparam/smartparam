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
package org.smartparam.engine.matchers.decoder;

import org.smartparam.engine.annotated.annotations.ParamMatcherDecoder;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherAwareDecoder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.util.EngineUtil;

/**
 *
 * @author Adam Dubiel
 */
@ParamMatcherDecoder(value = "", values = {"between/ie", "between/ei", "between/ii", "between/ee"})
public class BetweenMatcherDecoder implements MatcherAwareDecoder<Range<?>> {

    private final EmptyMatcherDecoder simpleConverter = new EmptyMatcherDecoder();

    @Override
    public Range<?> decode(String value, Type<?> type, Matcher matcher) {
        BetweenMatcher betweenMatcher = (BetweenMatcher) matcher;
        char separator = findSeparator(value, betweenMatcher.separators());

        String[] values = EngineUtil.split2(value, separator);
        String from = values[0].trim();
        String to = values[1].trim();

        return new Range(decodeValue(from, type), decodeValue(to, type));
    }

    private char findSeparator(String pattern, char[] separators) {
        for (char ch : separators) {
            if (pattern.indexOf(ch) >= 0) {
                return ch;
            }
        }
        return separators[0];
    }

    private Object decodeValue(String value, Type<?> type) {
        return simpleConverter.decode(value, type, null);
    }

    @Override
    public String encode(Range<?> object, Type<?> type, Matcher matcher) {
        BetweenMatcher betweenMatcher = (BetweenMatcher) matcher;
        String from = encodeValue(object.from(), type);
        String to = encodeValue(object.to(), type);

        char separator = findSeparator(from, to, betweenMatcher.separators());

        return from + separator + to;
    }

    private char findSeparator(String encodedFrom, String encodedTo, char[] separators) {
        for (char ch : separators) {
            if (encodedFrom.indexOf(ch) == -1 && encodedTo.indexOf(ch) == -1) {
                return ch;
            }
        }
        return separators[0];
    }

    private String encodeValue(Object value, Type<?> type) {
        return simpleConverter.encode(value, type, null);
    }
}
