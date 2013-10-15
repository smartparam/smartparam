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
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public interface ParameterEntryDeserializer {

    public ParameterEntryBatchLoader deserialize(SerializationConfig config, BufferedReader reader) throws SmartParamSerializationException;
}
