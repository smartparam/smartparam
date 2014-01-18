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
import org.smartparam.engine.core.parameter.entry.ParameterEntryBatchLoader;
import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.supercsv.io.CsvListReader;

/**
 *
 * @author Adam Dubiel
 */
public class CsvParameterEntryDeserializer implements ParameterEntryDeserializer {

    private final SerializationConfig serializationConfig;

    public CsvParameterEntryDeserializer(SerializationConfig serializationConfig) {
        this.serializationConfig = serializationConfig;
    }

    @Override
    public ParameterEntryBatchLoader deserialize(BufferedReader reader) throws ParamSerializationException {
        CsvListReader csvListReader = new CsvListReader(reader, CsvPreferenceBuilder.csvPreference(serializationConfig));
        try {
            // drop header
            csvListReader.read();
            return new CsvParameterEntryBatchLoader(csvListReader);
        } catch (IOException exception) {
            throw new EntriesCSVSerializationException(exception);
        }
    }
}
