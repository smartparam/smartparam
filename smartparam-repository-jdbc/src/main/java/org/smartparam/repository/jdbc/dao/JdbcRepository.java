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
package org.smartparam.repository.jdbc.dao;

import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.metadata.ParameterMetadata;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public interface JdbcRepository {

    void createParameter(QueryRunner runner, Parameter parameter);

    boolean parameterExists(QueryRunner runner, String parameterName);

    JdbcParameter getParameter(QueryRunner runner, String parameterName);

    JdbcParameter getParameterMetadata(QueryRunner runner, String parameterName);

    Set<String> getParameterNames();

    Set<ParameterEntry> getParameterEntries(QueryRunner runner, String parameterName);

    void writeParameterEntries(QueryRunner runner, String parameterName, Iterable<ParameterEntry> entries);

    void deleteParameter(QueryRunner runner, String parameterName);

    void updateParameter(QueryRunner runner, String parameterName, ParameterMetadata parameter);
}
