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

import org.smartparam.editor.core.matcher.MatcherAwareEncoder;
import org.smartparam.editor.annotated.ParamMatcherEncoder;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.matchers.decoder.Range;

/**
 *
 * @author Adam Dubiel
 */
@ParamMatcherEncoder(value = "", values = {"between/ie", "between/ei", "between/ii", "between/ee"})
public class BetweenMatcherEncoder implements MatcherAwareEncoder<Range> {

    private final EmptyMatcherEncoder simpleConverter = new EmptyMatcherEncoder();

    @Override
    public String encode(Range object, Type<?> type, Matcher matcher) {
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
