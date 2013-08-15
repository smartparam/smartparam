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

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.editable.EditableLevel;

/**
 *
 * @author Adam Dubiel
 */
public class LevelSerializationAdapter implements JsonDeserializer<Level>, JsonSerializer<Level> {

    private Gson gson;

    private Class<? extends EditableLevel> levelInstanceClass;

    public LevelSerializationAdapter(Class<? extends EditableLevel> levelInstanceClass) {
        this.levelInstanceClass = levelInstanceClass;
    }

    @Override
    public JsonElement serialize(Level src, Type typeOfSrc, JsonSerializationContext context) {
        return gson.toJsonTree(src);
    }

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return gson.fromJson(json, levelInstanceClass);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
