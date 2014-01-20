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

import org.smartparam.editor.matcher.EmptyMatcherEncoder;
import org.smartparam.engine.core.index.Star;
import org.smartparam.engine.types.string.StringType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class EmptyMatcherConverterTest {

    private final EmptyMatcherEncoder converter = new EmptyMatcherEncoder();

    @Test
    public void shouldEncodeStarObjectAsStar() {
        // when
        String encoded = converter.encode(Star.star(), null, null);

        // then
        assertThat(encoded).isEqualTo("*");
    }

    @Test
    public void shouldEncodeValueAsEmptyStringWhenNoTypeAndValueIsNull() {
        // when
        String encoded = converter.encode(null, null, null);

        // then
        assertThat(encoded).isEqualTo("");
    }

    @Test
    public void shouldEncodeValueUsingToStringWhenNoTypeProvied() {
        // when
        String encoded = converter.encode("value", null, null);

        // then
        assertThat(encoded).isEqualTo("value");
    }

    @Test
    public void shouldEncodeValueusingTypeWhenTypeProvided() {
        // given
        StringType type = new StringType();

        // when
        String encoded = converter.encode(new SimpleObject(), type, null);

        // then
        assertThat(encoded).isEqualTo("simpleObject");
    }

    private static class SimpleObject {

        @Override
        public String toString() {
            return "simpleObject";
        }

    }
}
