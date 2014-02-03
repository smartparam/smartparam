/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.editor.core.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class AbstractEntityKeyTest {

    private FakeEntityKey entityKey;

    @BeforeMethod
    public void setUp() {
        this.entityKey = new FakeEntityKey();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParsingNullString() {
        // when
        catchException(entityKey).exposedParse("fake", null);

        // then
        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParsingEmptyString() {
        // when
        catchException(entityKey).exposedParse("fake", "");

        // then
        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParsingStringWithoutSeparator() {
        // when
        catchException(entityKey).exposedParse("fake", "somethingWithoutSeparator");

        // then
        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParsingStringThatDoesNotStartWithRepositorySymbol() {
        // when
        catchException(entityKey).exposedParse("fake", "something-withoutPrefix");

        // then
        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldParseWellFormattedStringAndReturnArrayOfKeySegmentsWithoutRepositorySymbol() {
        // when
        String[] keySegments = entityKey.exposedParse("fake", "fake-meaningful-data");

        // then
        assertThat(keySegments).hasSize(2).contains("meaningful", "data");
    }

    @Test
    public void shouldConcatenateRepositorySymbolWithKeySegmentsUsingSeparatorWhenFormattingKey() {
        // when
        String key = entityKey.exposedFormat("fake", "hello", "world");

        // then
        assertThat(key).isEqualTo("fake-hello-world");
    }
}
