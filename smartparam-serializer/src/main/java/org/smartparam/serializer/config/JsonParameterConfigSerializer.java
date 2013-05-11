package org.smartparam.serializer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.model.EditableLevel;
import org.smartparam.serializer.model.EditableParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializer implements ParameterConfigSerializer {

    private static final String[] IGNORED_PROPERTIES = new String[]{"entries"};

    private Gson gson;

    public JsonParameterConfigSerializer(Class<? extends EditableLevel> levelInstanceClass) {
        PropertyExclusionStrategy exclusionStrategy = new PropertyExclusionStrategy(IGNORED_PROPERTIES);
        LevelDeserializer levelDeserializer = new LevelDeserializer(levelInstanceClass);

        gson = (new GsonBuilder()).setExclusionStrategies(exclusionStrategy)
                .registerTypeAdapter(Level.class, levelDeserializer).setPrettyPrinting().create();

        levelDeserializer.setGson(gson);
    }

    public String serialize(Parameter parameter) {
        String serializedConfig = gson.toJson(parameter);

        return serializedConfig;
    }

    public <T extends EditableParameter> T deserialize(String configText, Class<T> implementingClass) {
        T configObject = gson.fromJson(configText, implementingClass);

        return configObject;
    }
}
