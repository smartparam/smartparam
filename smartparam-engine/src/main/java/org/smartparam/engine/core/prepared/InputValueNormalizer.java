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
package org.smartparam.engine.core.prepared;

import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.string.StringType;

/**
 *
 * @author Adam Dubiel
 */
public final class InputValueNormalizer {

    private InputValueNormalizer() {
    }

    public static <T extends AbstractHolder> String normalize(Type<T> type, String levelValue) {
        if ("*".equals(levelValue)) {
            return levelValue;

        }

        if (type.getClass() == StringType.class) {
            return levelValue;

        }

        try {
            // if level value can be properly decoded - return normalized value
            T decoded = type.decode(levelValue);
            return type.encode(decoded);

        } catch (Exception e) {

            // if level value is corrupted and cannot be decoded - leave it untouched
            return levelValue;
        }
    }

    public static <T extends AbstractHolder> String normalize(Type<T> type, Object levelObject) {
        if (levelObject instanceof String) {
            return normalize(type, (String) levelObject);
        }

        T decoded = type.convert(levelObject);
        return type.encode(decoded);
    }

    public static String[] normalize(PreparedParameter param, Object[] levelValues) {

        int size = Math.min(levelValues.length, param.getInputLevelsCount());
        String[] normalized = new String[size];

        for (int i = 0; i < size; i++) {
            PreparedLevel level = param.getLevels()[i];
            Type<?> type = level.getType();
            normalized[i] = normalize(type, levelValues[i]);
        }

        return normalized;
    }
}
