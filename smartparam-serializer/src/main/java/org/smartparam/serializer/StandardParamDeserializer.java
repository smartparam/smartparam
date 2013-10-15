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

import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.engine.model.editable.EditableLevel;
import org.smartparam.engine.model.editable.EditableParameter;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.serializer.metadata.JsonParameterMetadataDeserializer;
import org.smartparam.serializer.entries.CsvParameterEntryDeserializer;

/**
 *
 * @author Adam Dubiel
 */
public class StandardParamDeserializer extends RawSmartParamDeserializer {

    public StandardParamDeserializer(
            SerializationConfig serializationConfig,
            Class<? extends EditableParameter> parameterInstanceClass,
            Class<? extends EditableLevel> levelInstanceClass,
            Class<? extends EditableParameterEntry> parameterEntryInstanceClass) {

        super(serializationConfig,
              new JsonParameterMetadataDeserializer(parameterInstanceClass, levelInstanceClass),
              new CsvParameterEntryDeserializer(parameterEntryInstanceClass));
    }
}
