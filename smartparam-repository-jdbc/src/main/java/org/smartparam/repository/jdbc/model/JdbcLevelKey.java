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

import org.smartparam.engine.core.parameter.level.LevelKey;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcLevelKey implements LevelKey {

    private final long levelId;

    public JdbcLevelKey(long levelId) {
        this.levelId = levelId;
    }

    public JdbcLevelKey(LevelKey levelKey) {
        levelId = Long.parseLong(levelKey.value());
    }

    @Override
    public String value() {
        return Long.toString(levelId);
    }

    public long levelId() {
        return levelId;
    }
}
