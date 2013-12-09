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
package org.smartparam.repository.jdbc.test.builder;

import org.smartparam.repository.jdbc.model.JdbcLevel;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcLevelTestBuilder {

    private long id;

    private int order;

    private String name;

    private String type;

    private String matcher;

    private String levelCreator;

    private boolean array;

    public static JdbcLevelTestBuilder jdbcLevel() {
        return new JdbcLevelTestBuilder();
    }

    public JdbcLevel build() {
        JdbcLevel level = new JdbcLevel(id, order);
        level.setName(name);
        level.setType(type);
        level.setMatcher(matcher);
        level.setLevelCreator(levelCreator);
        level.setArray(array);

        return level;
    }

    public JdbcLevelTestBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public JdbcLevelTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public JdbcLevelTestBuilder withMatcher(String matcher) {
        this.matcher = matcher;
        return this;
    }

    public JdbcLevelTestBuilder withLevelCreator(String levelCreator) {
        this.levelCreator = levelCreator;
        return this;
    }

    public JdbcLevelTestBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public JdbcLevelTestBuilder array() {
        this.array = true;
        return this;
    }

    public JdbcLevelTestBuilder withOrder(int order) {
        this.order = order;
        return this;
    }
}
