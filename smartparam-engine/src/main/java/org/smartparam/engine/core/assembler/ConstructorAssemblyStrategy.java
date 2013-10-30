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
package org.smartparam.engine.core.assembler;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.engine.MultiValue;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.util.reflection.ReflectionsConstructorUtil;

/**
 * Assembly strategy that uses constructor to construct new value object.
 * There has to be a constructor with number of arguments equal to number of
 * output levels and each argument should be annotated with {@link FromParamLevel}
 * to indicate which level value should be injected. Constructor does not have
 * to be public.
 *
 * @author Adam Dubiel
 */
public class ConstructorAssemblyStrategy implements AssemblyStrategy {

    private Map<Class<?>, ConstructorMetadata> construtorMetadataCache = new HashMap<Class<?>, ConstructorMetadata>();

    @Override
    public <T> T assemble(Class<T> valueObjectClass, MultiValue values) {
        ConstructorMetadata constructorMetadata = getConstructorMetadata(valueObjectClass, values.size());
        return assemble(constructorMetadata, values);
    }

    private ConstructorMetadata getConstructorMetadata(Class<?> valueObjectClass, int argumentCount) {
        ConstructorMetadata constructorMetadata = construtorMetadataCache.get(valueObjectClass);
        if (constructorMetadata == null) {
            Constructor<?> constructor = ReflectionsConstructorUtil.findNonSyntheticConstructor(valueObjectClass, argumentCount);
            String[] orderedLevels = readLevelOrderFromParameterAnnotations(constructor);

            constructorMetadata = new ConstructorMetadata(constructor, orderedLevels);
            construtorMetadataCache.put(valueObjectClass, constructorMetadata);
        }

        return constructorMetadata;
    }

    private <T> T assemble(ConstructorMetadata constructorMetadata, MultiValue values) {
        Constructor<?> constructor = constructorMetadata.getConstructor();
        Object[] constructorArgs = extractLevelValues(constructorMetadata.getLevelNames(), constructor.getParameterTypes(), values);
        return ReflectionsConstructorUtil.createObject(constructor, constructorArgs);
    }

    private Object[] extractLevelValues(String[] levelOrder, Class<?>[] expectedClasses, MultiValue values) {
        Object[] objects = new Object[levelOrder.length];
        for (int index = 0; index < levelOrder.length; ++index) {
            objects[index] = getValue(values.getValue(levelOrder[index]), expectedClasses[index]);
        }

        return objects;
    }

    private Object getValue(AbstractHolder holder, Class<?> targetClass) {
        if (holder instanceof IntegerHolder && isInt(targetClass)) {
            return holder.getInteger();
        }
        return holder.getValue();
    }

    private boolean isInt(Class<?> clazz) {
        return clazz == Integer.class || clazz == int.class;
    }

    private String[] readLevelOrderFromParameterAnnotations(Constructor<?> constructor) {
        String[] levelNames = new String[constructor.getParameterAnnotations().length];

        int index = 0;
        List<FromParamLevel> annotations = ReflectionsConstructorUtil.findParametersAnnotation(constructor, FromParamLevel.class);
        for (FromParamLevel annotation : annotations) {
            levelNames[index] = annotation.value();
            index++;
        }
        return levelNames;
    }
}
