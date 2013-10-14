/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.serializer.util;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

import static org.smartparam.serializer.test.assertions.SerializerAssertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class StreamPartReaderTest {

    @Test
    public void shouldReadPartFromBeginningCharacterToEndCharacterSkippingUnnecessaryPrefix() throws IOException {
        // given
        String text = "hello world!{ this is important }";
        StringReader reader = new StringReader(text);

        // when
        String textPart = StreamPartReader.readPart(reader, '{', '}');

        // then
        assertThat(textPart).isEqualTo("{ this is important }");
    }

    @Test
    public void shouldReadPartToEndCharacterLeavingSufixUnchangedInStream() throws IOException {
        // given
        String text = "{ this is important } leave me alone please";
        StringReader reader = new StringReader(text);

        // when
        String textPart = StreamPartReader.readPart(reader, '{', '}');

        // then
        assertThat(textPart).isEqualTo("{ this is important }");
        assertThat(reader).hasTextLeft(" leave me alone please");
    }

    @Test
    public void shouldLookForOutermostCloseCharToPairWithOpenChar() throws IOException {
        // given
        String text = "{ { nested } this is important }";
        StringReader reader = new StringReader(text);

        // when
        String textPart = StreamPartReader.readPart(reader, '{', '}');

        // then
        assertThat(textPart).isEqualTo("{ { nested } this is important }");
    }

    @Test
    public void shouldThrowExceptionWhenDanglingOpeningCharacterDetected() throws IOException {
        // given
        String text = "{ this has danglign opening character";
        StringReader reader = new StringReader(text);

        // when
        try {
            StreamPartReader.readPart(reader, '{', '}');
            fail("expected IllegalState exception but none thrown");
        } catch (IllegalStateException exception) {
            // then
            assertThat(exception).isInstanceOf(IllegalStateException.class);
        }

    }
}