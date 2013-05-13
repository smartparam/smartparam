package org.smartparam.serializer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashSet;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.serializer.model.EditableLevel;
import org.smartparam.serializer.model.EditableParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializer implements ParameterConfigSerializer {

    private static final String[] IGNORED_PROPERTIES = new String[]{"entries"};

    private Class<? extends EditableParameter> parameterInstanceClass;

    private Gson gson;

    public JsonParameterConfigSerializer(Class<? extends EditableParameter> parameterInstanceClass, Class<? extends EditableLevel> levelInstanceClass) {
        this.parameterInstanceClass = parameterInstanceClass;

        PropertyExclusionStrategy exclusionStrategy = new PropertyExclusionStrategy(IGNORED_PROPERTIES);
        LevelSerializationAdapter levelAdapter = new LevelSerializationAdapter(levelInstanceClass);

        gson = (new GsonBuilder()).setExclusionStrategies(exclusionStrategy)
                .registerTypeAdapter(Level.class, levelAdapter).setPrettyPrinting().create();

        levelAdapter.setGson(gson);
    }

    @Override
    public String serialize(Parameter parameter) {
        String serializedConfig = gson.toJson(parameter);

        return serializedConfig;
    }

    @Override
    public Parameter deserialize(String configText) {
        EditableParameter parameter = gson.fromJson(configText, parameterInstanceClass);
        parameter.setEntries(new HashSet<ParameterEntry>());
        return parameter;
    }
}
