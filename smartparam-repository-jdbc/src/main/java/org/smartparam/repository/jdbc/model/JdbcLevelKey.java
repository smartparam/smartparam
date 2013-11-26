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
package org.smartparam.repository.jdbc.model;

import org.smartparam.editor.model.AbstractEntityKey;
import org.smartparam.editor.model.LevelKey;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcLevelKey extends AbstractEntityKey implements LevelKey {

    static final String SYMBOL = "jdbc";

    private final String value;

    private final String parameterName;

    private final long levelId;

    public JdbcLevelKey(String parameterName, long levelId) {
        this.value = format(SYMBOL, parameterName, Long.toString(levelId));
        this.parameterName = parameterName;
        this.levelId = levelId;
    }

    public JdbcLevelKey(LevelKey parameterKey) {
        String[] segments = parse(SYMBOL, parameterKey.value());
        value = parameterKey.value();
        parameterName = segments[0];
        levelId = Long.parseLong(segments[1]);
    }

    @Override
    public String value() {
        return value;
    }

    public String parameterName() {
        return parameterName;
    }

    public long levelId() {
        return levelId;
    }
}
