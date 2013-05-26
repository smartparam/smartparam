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
import org.smartparam.mgmt.model.EditableLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
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
