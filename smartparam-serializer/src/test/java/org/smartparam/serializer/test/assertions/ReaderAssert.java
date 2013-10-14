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
package org.smartparam.serializer.test.assertions;

import java.io.IOException;
import java.io.Reader;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

/**
 *
 * @author Adam Dubiel
 */
public class ReaderAssert extends AbstractAssert<ReaderAssert, Reader> {

    private static final int REST_OUTPUT_SIZE = 100;

    private ReaderAssert(Reader actual) {
        super(actual, ReaderAssert.class);
    }

    public static ReaderAssert assertThat(Reader reader) {
        return new ReaderAssert(reader);
    }

    public ReaderAssert hasTextLeft(String text) {
        try {
            Assertions.assertThat(readAll(actual)).isEqualTo(text);
        } catch (IOException exception) {
            Assertions.fail("faield to read rest of stream", exception);
        }
        return this;
    }

    private String readAll(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder(REST_OUTPUT_SIZE);
        int characterCode = reader.read();
        while (characterCode != -1) {
            builder.append((char) characterCode);
            characterCode = reader.read();
        }

        return builder.toString();
    }
}
