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

import org.smartparam.engine.core.parameter.identity.AbstractEntityKey;
import org.smartparam.engine.core.parameter.level.LevelKey;
import static org.smartparam.repository.jdbc.model.JdbcParameterKey.SYMBOL;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcLevelKey extends AbstractEntityKey implements LevelKey {

    private final String value;

    private final long levelId;

    public JdbcLevelKey(long levelId) {
        this.value = format(SYMBOL, Long.toString(levelId));
        this.levelId = levelId;
    }

    public JdbcLevelKey(LevelKey levelKey) {
        String[] segments = parse(SYMBOL, levelKey.value());
        value = levelKey.value();
        levelId = Long.parseLong(segments[0]);
    }

    @Override
    public String value() {
        return value;
    }

    public long levelId() {
        return levelId;
    }
}
