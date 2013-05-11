package org.smartparam.serializer.config;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.serializer.model.EditableLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class LevelDeserializer implements JsonDeserializer<Level> {

    private Gson gson;

    private Class<? extends EditableLevel> levelInstanceClass;

    public LevelDeserializer(Class<? extends EditableLevel> levelInstanceClass) {
        this.levelInstanceClass = levelInstanceClass;
    }

    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return gson.fromJson(json, levelInstanceClass);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
