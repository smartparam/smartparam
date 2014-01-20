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

import java.util.Arrays;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 * PreparedEntry compiled form, without unnecessary information and with
 * normalized levels array (never null, no trailing null values).
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedEntry {

    private static final String[] EMPTY_ARRAY = {};

    private final String[] levels;

    public PreparedEntry(ParameterEntry parameterEntry) {
        this.levels = normalizeLevels(parameterEntry.getLevels());
    }

    public String[] getLevels() {
        return levels;
    }

    private String[] normalizeLevels(String[] rawLevels) {
        String[] normalizedLevels = trimRight(rawLevels);
        internalizeLevelValues(normalizedLevels);
        return normalizedLevels;
    }

    private String[] trimRight(String[] array) {
        if (array == null) {
            return EMPTY_ARRAY;
        }

        int len = array.length;
        while (len > 0 && array[len - 1] == null) {
            --len;
        }

        return len < array.length ? Arrays.copyOf(array, len) : array;
    }

    private void internalizeLevelValues(String[] levels) {
        for (int i = 0; i < levels.length; i++) {
            if (levels[i] != null) {
                levels[i] = levels[i].intern();
            }
        }
    }

    public String getLevel(int k) {
        return (k >= 0 && k < levels.length) ? levels[k] : null;
    }
}
