package org.smartparam.serializer.config;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import org.smartparam.engine.model.Level;
import org.smartparam.serializer.exception.SmartParamSerializerException;
import org.smartparam.serializer.model.EditableLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class LevelInstanceCreator implements InstanceCreator<Level> {

    private Class<? extends EditableLevel> levelInstanceClass;

    public LevelInstanceCreator(Class<? extends EditableLevel> levelInstanceClass) {
        this.levelInstanceClass = levelInstanceClass;
    }

    public Level createInstance(Type type) {
        try {
            return levelInstanceClass.newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamSerializerException("failed to instantiate level of class " + levelInstanceClass.getName() + " "
                    + "maybe it lacks default constructor?", exception);
        }
    }
}
