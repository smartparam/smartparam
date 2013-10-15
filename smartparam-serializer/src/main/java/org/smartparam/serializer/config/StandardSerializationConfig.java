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
package org.smartparam.serializer.config;

import java.nio.charset.Charset;
import java.util.List;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.model.editable.EditableLevel;
import org.smartparam.engine.model.editable.EditableParameter;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.StandardParamDeserializer;
import org.smartparam.serializer.StandardParamSerializer;
import org.smartparam.serializer.entries.CsvParameterEntryDeserializer;
import org.smartparam.serializer.entries.CsvParameterEntrySerializer;
import org.smartparam.serializer.metadata.JsonParameterMetadataDeserializer;
import org.smartparam.serializer.metadata.JsonParameterMetadataSerializer;

/**
 *
 * @author Adam Dubiel
 */
public class StandardSerializationConfig extends ComponentConfig implements CsvSerializationConfig {

    public static final String DEFAULT_END_OF_LINE = "\n";

    public static final char DEFAULT_CSV_DELIMITER = ';';

    public static final char DEFAULT_CSV_QUOTE = '"';

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    private Class<? extends EditableParameter> parameterInstanceClass;

    private Class<? extends EditableLevel> levelInstanceClass;

    private Class<? extends EditableParameterEntry> parameterEntryInstanceClass;

    private char csvQuote = DEFAULT_CSV_QUOTE;

    private char csvDelimiter = DEFAULT_CSV_DELIMITER;

    private String endOfLine = DEFAULT_END_OF_LINE;

    private Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);

    @Override
    protected void injectDefaults(List<Object> components) {
        components.add(JsonParameterMetadataDeserializer.class);
        components.add(JsonParameterMetadataSerializer.class);
        components.add(CsvParameterEntryDeserializer.class);
        components.add(CsvParameterEntrySerializer.class);
        components.add(StandardParamSerializer.class);
        components.add(StandardParamDeserializer.class);
    }

    @Override
    public Class<? extends EditableParameter> parameterInstanceClass() {
        return parameterInstanceClass;
    }

    void setParameterInstanceClass(Class<? extends EditableParameter> parameterInstanceClass) {
        this.parameterInstanceClass = parameterInstanceClass;
    }

    @Override
    public Class<? extends EditableLevel> levelInstanceClass() {
        return levelInstanceClass;
    }

    void setLevelInstanceClass(Class<? extends EditableLevel> levelInstanceClass) {
        this.levelInstanceClass = levelInstanceClass;
    }

    @Override
    public Class<? extends EditableParameterEntry> parameterEntryInstanceClass() {
        return parameterEntryInstanceClass;
    }

    void setParameterEntryInstanceClass(Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {
        this.parameterEntryInstanceClass = parameterEntryInstanceClass;
    }

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
