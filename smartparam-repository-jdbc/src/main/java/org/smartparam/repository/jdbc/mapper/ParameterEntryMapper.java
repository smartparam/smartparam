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
package org.smartparam.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryMapper implements ObjectMapper<ParameterEntry> {

    private int parameterId;

    public ParameterEntryMapper(int parameterId) {
        this.parameterId = parameterId;
    }

    @Override
    public ParameterEntry createObject(ResultSet resultSet) throws SQLException {
        JdbcParameterEntry entry = new JdbcParameterEntry();
        entry.setId(resultSet.getInt("id"));
        entry.setParameterId(parameterId);
        entry.setLevels(new String[]{
            resultSet.getString("level1"),
            resultSet.getString("level2"),
            resultSet.getString("level3"),
            resultSet.getString("level4"),
            resultSet.getString("level5"),
            resultSet.getString("level6"),
            resultSet.getString("level7"),
            resultSet.getString("level8")
        });
        return entry;
    }
}
