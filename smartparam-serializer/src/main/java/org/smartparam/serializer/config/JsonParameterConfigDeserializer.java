package org.smartparam.serializer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashSet;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.mgmt.model.EditableLevel;
import org.smartparam.mgmt.model.EditableParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigDeserializer implements ParameterConfigDeserializer {

    private Class<? extends EditableParameter> parameterInstanceClass;

    private Gson gson;

    public JsonParameterConfigDeserializer(Class<? extends EditableParameter> parameterInstanceClass, Class<? extends EditableLevel> levelInstanceClass) {
        this.parameterInstanceClass = parameterInstanceClass;

        LevelSerializationAdapter levelAdapter = new LevelSerializationAdapter(levelInstanceClass);

        gson = (new GsonBuilder()).registerTypeAdapter(Level.class, levelAdapter).create();

        levelAdapter.setGson(gson);
    }

    @Override
    public Parameter deserialize(String configText) {
        EditableParameter parameter = gson.fromJson(configText, parameterInstanceClass);
        parameter.setEntries(new HashSet<ParameterEntry>());
        return parameter;
    }
}
