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
package org.smartparam.serializer;

import java.nio.charset.Charset;

/**
 *
 * @author Adam Dubiel
 */
public class DefaultSerializationConfig implements CsvSerializationConfig {

    public static final String DEFAULT_END_OF_LINE = "\n";

    public static final char DEFAULT_CSV_DELIMITER = ';';

    public static final char DEFAULT_CSV_QUOTE = '"';

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    private char csvQuote = DEFAULT_CSV_QUOTE;

    private char csvDelimiter = DEFAULT_CSV_DELIMITER;

    private String endOfLine = DEFAULT_END_OF_LINE;

    private Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);

    @Override
    public char getCsvDelimiter() {
        return csvDelimiter;
    }

    void setCsvDelimiter(char csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
    }

    @Override
    public char getCsvQuote() {
        return csvQuote;
    }

    void setCsvQuote(char csvQuote) {
        this.csvQuote = csvQuote;
    }

    @Override
    public String getEndOfLine() {
        return endOfLine;
    }

    void setEndOfLine(String endOfLine) {
        this.endOfLine = endOfLine;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    void setCharset(String charsetCode) {
        this.charset = Charset.forName(charsetCode);
    }
}
