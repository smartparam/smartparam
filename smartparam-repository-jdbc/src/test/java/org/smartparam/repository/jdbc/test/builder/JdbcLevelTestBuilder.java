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

import org.smartparam.engine.test.builder.AbstractLevelTestBuilder;
import org.smartparam.repository.jdbc.model.JdbcLevel;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcLevelTestBuilder extends AbstractLevelTestBuilder<JdbcLevel, JdbcLevelTestBuilder> {

    private long parameterId;

    private int order;

    public static JdbcLevelTestBuilder jdbcLevel() {
        return new JdbcLevelTestBuilder();
    }

    @Override
    protected JdbcLevel buildLevel() {
        JdbcLevel level = new JdbcLevel(0, parameterId);
        level.setOrderNo(order);
        return level;
    }

    @Override
    protected JdbcLevelTestBuilder self() {
        return this;
    }

    public JdbcLevelTestBuilder withOrder(int order) {
        this.order = order;
        return this;
    }
}
