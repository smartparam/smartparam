/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.serializer.test.builder;

import java.io.StringReader;

/**
 *
 * @author Adam Dubiel
 */
public class CsvEntriesReaderTestBuilder {

    private static final int PROBABLE_ENTRY_VALUE_LENGTH = 10;

    private final StringBuilder stringBuilder;

    private String delimiter = ";";

    private int totalEntries = 0;

    private CsvEntriesReaderTestBuilder(int probableCapacity) {
        stringBuilder = new StringBuilder(probableCapacity);
    }

    public static CsvEntriesReaderTestBuilder csvEntriesReader(int probableCapacity) {
        return new CsvEntriesReaderTestBuilder(probableCapacity);
    }

    public StringReader build() {
        return new StringReader(stringBuilder.toString());
    }

    public CsvEntriesReaderTestBuilder usingDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public CsvEntriesReaderTestBuilder withEntries(int repeatedTimes, String... entryValues) {
        StringBuilder entryBuilder = new StringBuilder(entryValues.length * PROBABLE_ENTRY_VALUE_LENGTH);
        for (String entryValue : entryValues) {
            entryBuilder.append(entryValue).append(delimiter);
        }
        String entry = entryBuilder.deleteCharAt(entryBuilder.length() - 1).toString();

        for (int i = 0; i < repeatedTimes; ++i) {
            stringBuilder.append(String.format(entry, totalEntries)).append("\n");
            totalEntries++;
        }

        return this;
    }
}
