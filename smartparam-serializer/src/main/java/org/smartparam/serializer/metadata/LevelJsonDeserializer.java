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
package org.smartparam.serializer.metadata;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.serializer.model.DeserializedLevel;

/**
 *
 * @author Adam Dubiel
 */
public class LevelJsonDeserializer implements JsonDeserializer<Level> {

    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return context.deserialize(json, DeserializedLevel.class);
    }

}