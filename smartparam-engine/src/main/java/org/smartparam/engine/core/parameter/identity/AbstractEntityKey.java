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
package org.smartparam.engine.core.parameter.identity;

import java.util.Arrays;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractEntityKey {

    private static final String SEPARATOR = "-";

    private static final int KEY_LENGTH = 50;

    protected final String[] parse(String repositorySymbol, String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key can't be null nor empty.");
        }

        String[] keySegments = key.split(SEPARATOR);
        if (keySegments.length < 2) {
            throw new IllegalArgumentException("Couldn't parse key " + key + ". Looks like it's segments are not separated using standard separator.");
        }
        if (!repositorySymbol.equals(keySegments[0])) {
            throw new IllegalArgumentException("Key " + key + " can't be interpreted as valid key for " + repositorySymbol + " repository.");
        }

        return Arrays.copyOfRange(keySegments, 1, keySegments.length);
    }

    protected final String format(String repositorySymbol, String... keySegments) {
        StringBuilder keyBuilder = new StringBuilder(KEY_LENGTH);
        keyBuilder.append(repositorySymbol).append(SEPARATOR);
        for (String segment : keySegments) {
            keyBuilder.append(segment).append(SEPARATOR);
        }
        keyBuilder.deleteCharAt(keyBuilder.length() - 1);

        return keyBuilder.toString();
    }
}
