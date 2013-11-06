/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, VeresultSetion 2.0 (the "License");
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
package org.smartparam.repository.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.polyjdbc.core.query.mapper.ObjectMapper;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.util.JdbcConverter;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterMapper implements ObjectMapper<JdbcParameter> {

    @Override
    public JdbcParameter createObject(ResultSet resultSet) throws SQLException {
        JdbcParameter parameter = new JdbcParameter(resultSet.getString("name"),
                resultSet.getInt("input_levels"));

        parameter.setCacheable(resultSet.getBoolean("cacheable"));
        parameter.setNullable(resultSet.getBoolean("nullable"));
        parameter.setArraySeparator(JdbcConverter.toChar(resultSet.getString("array_separator")));

        return parameter;
    }
}
