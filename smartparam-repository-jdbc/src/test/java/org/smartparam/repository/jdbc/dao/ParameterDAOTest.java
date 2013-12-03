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

import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.editor.viewer.ParameterFilter;
import org.smartparam.editor.viewer.SortDirection;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.repository.jdbc.DatabaseTest;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.testng.annotations.Test;

import static org.smartparam.engine.test.assertions.Assertions.assertThat;
import static org.smartparam.engine.model.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class ParameterDAOTest extends DatabaseTest {

    public void shouldInsertNewParameterIntoDatabase() {
        // given
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        QueryRunner runner = queryRunner();
        Parameter parameter = parameter().withName("test").withInputLevels(5)
                .nullable().noncacheable().withArraySeparator('*').build();

        // when
        parameterDAO.insert(runner, parameter);
        JdbcParameter resultingParameter = parameterDAO.getParameter(runner, "test");
        runner.close();

        // then
        assertThat(resultingParameter).isNotNull().hasName("test")
                .hasInputLevels(5).hasArraySeparator('*').isNullable().isNotCacheable();
    }

    public void shouldDeleteParameterFromDatabase() {
        // given
        database().withParameter("toDelete").build();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        QueryRunner runner = queryRunner();

        // when
        parameterDAO.delete(runner, "toDelete");
        runner.close();

        // then
        assertDatabase().hasNoParameter("toDelete").close();
    }

    public void shouldReturnSetOfParameterNames() {
        // given
        database().withParameters(10).build();
        ParameterDAO parameterDAO = get(ParameterDAO.class);

        // when
        Set<String> parameters = parameterDAO.getParameterNames();

        // then
        assertThat(parameters).isNotEmpty().hasSize(10);
    }

    @Test
    public void shouldReturnListOfSortedParameterNames() {
        // given
        database().withParameters(5).build();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        ParameterFilter filter = new ParameterFilter(SortDirection.DESC);

        // when
        List<String> parameters = parameterDAO.getParameterNames(filter);

        // then
        assertThat(parameters).containsExactly("parameter4", "parameter3", "parameter2",
                "parameter1", "parameter0");
    }

    @Test
    public void shouldReturnFilteredListOfParameterNames() {
        // given
        database().withParameters(5).build();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        ParameterFilter filter = new ParameterFilter("*4");

        // when
        List<String> parameters = parameterDAO.getParameterNames(filter);

        // then
        assertThat(parameters).containsExactly("parameter4");
    }

    public void shouldReturnTrueIfParameterExists() {
        // given
        database().withParameter("test").build();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        QueryRunner runner = queryRunner();

        // when
        boolean exists = parameterDAO.parameterExists("test");
        runner.close();

        // then
        assertThat(exists).isTrue();
    }

    public void shouldReturnFalseIfParameterDoesNotExist() {
        // given
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        QueryRunner runner = queryRunner();

        // when
        boolean exists = parameterDAO.parameterExists("test");
        runner.close();

        // then
        assertThat(exists).isFalse();
    }

    @Test
    public void shouldUpdateContentsOfParameter() {
        // given
        database().withParameter("test").build();

        ParameterDAO parameterDAO = get(ParameterDAO.class);
        QueryRunner runner = queryRunner();

        Parameter updatedParameterData = parameter().withName("updatedTest").build();

        // when
        parameterDAO.update(runner, "test", updatedParameterData);
        runner.close();

        // then
        assertDatabase().hasNoParameter("test").hasParameter("updatedTest");
    }
}
