package org.smartparam.serializer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializer implements ParameterConfigSerializer {

    private static final String[] IGNORED_PROPERTIES = new String[]{"entries"};

    private Gson gson;

    public JsonParameterConfigSerializer() {
        PropertyExclusionStrategy exclusionStrategy = new PropertyExclusionStrategy(IGNORED_PROPERTIES);

        gson = (new GsonBuilder()).setExclusionStrategies(exclusionStrategy).setPrettyPrinting().create();
    }

    @Override
    public String serialize(Parameter parameter) {
        String serializedConfig = gson.toJson(parameter);

        return serializedConfig;
    }
}
