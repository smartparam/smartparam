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

import org.smartparam.engine.core.index.Star;
import org.smartparam.engine.core.type.ObjectHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.string.StringType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 *
 * @author Adam Dubiel
 */
public class EmptyMatcherDecoderTest {

    private final EmptyMatcherDecoder decoder = new EmptyMatcherDecoder();

    @Test
    public void shouldDecodeValueAsStarWhenEqualsStarSymbol() {
        // when
        Object decoded = decoder.decode("*", null, null);

        // then
        assertThat(decoded).isInstanceOf(Star.class);
    }

    @Test
    public void shouldDecodeValueAsRawStringValueWhenNoTypeDeclared() {
        // when
        Object decoded = decoder.decode("value", null, null);

        // then
        assertThat(decoded).isEqualTo("value");
    }

    @Test
    public void shouldDecodeEmptyStringAsNullWhenNoTypeDeclared() {
        // when
        Object decoded = decoder.decode("", null, null);

        // then
        assertThat(decoded).isNull();
    }

    @Test
    public void shouldDecodeUsingTypeWhenProvided() {
        // given
        String valueToDecode = "value";

        Type<?> type = mock(Type.class);
        when(type.decode(valueToDecode)).thenReturn(new ObjectHolder("hello"));

        // when
        Object decoded = decoder.decode("value", type, null);

        // then
        assertThat(decoded).isEqualTo("hello");
    }

    @Test
    public void shouldEncodeStarObjectAsStar() {
        // when
        String encoded = decoder.encode(Star.star(), null, null);

        // then
        assertThat(encoded).isEqualTo("*");
    }

    @Test
    public void shouldEncodeValueAsEmptyStringWhenNoTypeAndValueIsNull() {
        // when
        String encoded = decoder.encode(null, null, null);

        // then
        assertThat(encoded).isEqualTo("");
    }

    @Test
    public void shouldEncodeValueUsingToStringWhenNoTypeProvied() {
        // when
        String encoded = decoder.encode("value", null, null);

        // then
        assertThat(encoded).isEqualTo("value");
    }

    @Test
    public void shouldEncodeValueusingTypeWhenTypeProvided() {
        // given
        StringType type = new StringType();

        // when
        String encoded = decoder.encode(new SimpleObject(), type, null);

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
