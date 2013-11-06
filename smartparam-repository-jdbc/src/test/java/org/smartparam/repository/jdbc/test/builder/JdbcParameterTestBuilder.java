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

import org.smartparam.engine.test.builder.AbstractParameterTestBuilder;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterTestBuilder extends AbstractParameterTestBuilder<JdbcParameter, JdbcParameterTestBuilder> {

    public static JdbcParameterTestBuilder jdbcParameter() {
        return new JdbcParameterTestBuilder();
    }

    @Override
    protected JdbcParameter buildParameter() {
        JdbcParameter parameter = new JdbcParameter("dummy", 0);
        return parameter;
    }

    @Override
    protected JdbcParameterTestBuilder self() {
        return this;
    }
}
