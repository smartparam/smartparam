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
package org.smartparam.serializer.config;

import org.smartparam.engine.model.editable.EditableLevel;
import org.smartparam.engine.model.editable.EditableParameter;
import org.smartparam.engine.model.editable.EditableParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class StandardSerializationConfigBuilder {

    private StandardSerializationConfig config = new StandardSerializationConfig();

    private StandardSerializationConfigBuilder() {
    }

    public static StandardSerializationConfigBuilder standardSerializationConfig() {
        return new StandardSerializationConfigBuilder();
    }

    public StandardSerializationConfig build() {
        return config;
    }

    public StandardSerializationConfigBuilder producesParameter(Class<? extends EditableParameter> parameterClass) {
        config.setParameterInstanceClass(parameterClass);
        return this;
    }

    public StandardSerializationConfigBuilder producesParameterEntry(Class<? extends EditableParameterEntry> parameterEntryClass) {
        config.setParameterEntryInstanceClass(parameterEntryClass);
        return this;
    }

    public StandardSerializationConfigBuilder producesLevel(Class<? extends EditableLevel> levelClass) {
        config.setLevelInstanceClass(levelClass);
        return this;
    }

    public StandardSerializationConfigBuilder withCharset(String charset) {
        config.setCharset(charset);
        return this;
    }

    public StandardSerializationConfigBuilder withEndOfLine(String endOfLine) {
        config.setEndOfLine(endOfLine);
        return this;
    }

    public StandardSerializationConfigBuilder withCsvQuote(char quoteChar) {
        config.setCsvQuote(quoteChar);
        return this;
    }

    public StandardSerializationConfigBuilder withCsvDelimiter(char delimiter) {
        config.setCsvDelimiter(delimiter);
        return this;
    }
}
