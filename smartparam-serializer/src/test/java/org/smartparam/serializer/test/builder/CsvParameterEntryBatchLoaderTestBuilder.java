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
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.entries.CsvParameterEntryBatchLoader;
import org.smartparam.serializer.entries.SimpleBatchReaderWrapper;


/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class CsvParameterEntryBatchLoaderTestBuilder {

    private StringReader stringReader;

    private SerializationConfig config;

    private Class<? extends EditableParameterEntry> instanceClass;

    private CsvParameterEntryBatchLoaderTestBuilder() {
    }

    public static CsvParameterEntryBatchLoaderTestBuilder csvParameterEntryBatchLoader() {
        return new CsvParameterEntryBatchLoaderTestBuilder();
    }

    public CsvParameterEntryBatchLoader build() {
        return new CsvParameterEntryBatchLoader(instanceClass, config, new SimpleBatchReaderWrapper(stringReader));
    }

    public CsvParameterEntryBatchLoaderTestBuilder readingFrom(StringReader stringReader) {
        this.stringReader = stringReader;
        return this;
    }

    public CsvParameterEntryBatchLoaderTestBuilder creatingInstanceOf(Class<? extends EditableParameterEntry> instanceClass) {
        this.instanceClass = instanceClass;
        return this;
    }

    public CsvParameterEntryBatchLoaderTestBuilder withSerializationConfig(SerializationConfig config) {
        this.config = config;
        return this;
    }
}