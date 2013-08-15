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
public class StandardSerializationConfig implements CsvSerializationConfig {

    public static final String DEFAULT_END_OF_LINE = "\n";

    public static final char DEFAULT_CSV_DELIMITER = ';';

    public static final char DEFAULT_CSV_QUOTE = '"';

    public static final char DEFAULT_COMMENT = '#';

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    private char csvQuote;

    private char csvDelimiter;

    private char comment;

    private String endOfLine;

    private Charset charset;

    public StandardSerializationConfig() {
        this(DEFAULT_CSV_QUOTE, DEFAULT_CSV_DELIMITER, DEFAULT_COMMENT, DEFAULT_END_OF_LINE, DEFAULT_CHARSET_NAME);
    }

    public StandardSerializationConfig(char csvQuote, char csvDelimiter, char comment, String endOfLine, String charset) {
        this.csvQuote = csvQuote;
        this.csvDelimiter = csvDelimiter;
        this.comment = comment;
        this.endOfLine = endOfLine;
        this.charset = Charset.forName(charset);
    }

    @Override
    public char getCsvDelimiter() {
        return csvDelimiter;
    }

    @Override
    public char getCsvQuote() {
        return csvQuote;
    }

    @Override
    public String getEndOfLine() {
        return endOfLine;
    }

    @Override
    public char getCommentChar() {
        return comment;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }
}
