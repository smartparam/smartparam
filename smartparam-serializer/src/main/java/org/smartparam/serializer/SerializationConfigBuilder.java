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
package org.smartparam.serializer;

/**
 *
 * @author Adam Dubiel
 */
public final class SerializationConfigBuilder {

    private final DefaultSerializationConfig config = new DefaultSerializationConfig();

    private SerializationConfigBuilder() {
    }

    public static SerializationConfigBuilder serializationConfig() {
        return new SerializationConfigBuilder();
    }

    public DefaultSerializationConfig build() {
        return config;
    }

    public SerializationConfigBuilder withCharset(String charset) {
        config.setCharset(charset);
        return this;
    }

    public SerializationConfigBuilder withEndOfLine(String endOfLine) {
        config.setEndOfLine(endOfLine);
        return this;
    }

    public SerializationConfigBuilder withCsvQuote(char quoteChar) {
        config.setCsvQuote(quoteChar);
        return this;
    }

    public SerializationConfigBuilder withCsvDelimiter(char delimiter) {
        config.setCsvDelimiter(delimiter);
        return this;
    }
}
