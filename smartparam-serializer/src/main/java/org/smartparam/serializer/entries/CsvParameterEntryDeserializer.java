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
package org.smartparam.serializer.entries;

import java.io.BufferedReader;
import java.io.IOException;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import org.supercsv.io.CsvListReader;

/**
 *
 * @author Adam Dubiel
 */
public class CsvParameterEntryDeserializer implements ParameterEntryDeserializer {

    private Class<? extends EditableParameterEntry> instanceClass;

    public CsvParameterEntryDeserializer(Class<? extends EditableParameterEntry> instanceClass) {
        this.instanceClass = instanceClass;
    }

    @Override
    public ParameterEntryBatchLoader deserialize(SerializationConfig config, BufferedReader reader) throws SmartParamSerializationException {
        CsvListReader csvListReader = new CsvListReader(reader, CsvPreferenceBuilder.csvPreference(config));
        try {
            // drop header
            csvListReader.read();
            return new CsvParameterEntryBatchLoader(instanceClass, csvListReader);
        } catch (IOException exception) {
            throw new SmartParamSerializationException("failed to read header from parameter CSV stream", exception);
        }
    }
}
