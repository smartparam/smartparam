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
import org.smartparam.repository.jdbc.model.JdbcLevel;

/**
 *
 * @author Adam Dubiel
 */
public class LevelMapper implements ObjectMapper<JdbcLevel> {

    @Override
    public JdbcLevel createObject(ResultSet resultSet) throws SQLException {
        JdbcLevel level = new JdbcLevel();
        level.setId(resultSet.getLong("id"));
        level.setName(resultSet.getString("name"));
        level.setParameterId(resultSet.getLong("fk_parameter"));
        level.setOrderNo(resultSet.getInt("order_no"));
        level.setType(resultSet.getString("type"));
        level.setMatcher(resultSet.getString("matcher"));
        level.setLevelCreator(resultSet.getString("level_creator"));
        level.setArray(resultSet.getBoolean("array_flag"));
        return level;
    }
}
