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
import org.smartparam.engine.model.Parameter;
import org.smartparam.repository.jdbc.core.transaction.Transaction;
import org.smartparam.repository.jdbc.core.transaction.TransactionManager;
import org.smartparam.repository.jdbc.integration.DatabaseTest;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class ParameterDAOTest extends DatabaseTest {

    public void shouldInsertNewParameterIntoDatabase() {
        // given
        Transaction transaction = get(TransactionManager.class).openTransaction();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        Parameter parameter = parameter().withName("test").withInputLevels(5)
                .nullable().noncacheable().withArraySeparator('*').build();

        // when
        parameterDAO.insert(transaction, parameter);
        transaction.commit();
        transaction.closeWithArtifacts();
        Parameter resultingParameter = parameterDAO.getParameter("test");

        // then
        assertThat(resultingParameter).isNotNull().hasName("test")
                .hasInputLevels(5).hasArraySeparator('*').isNullable().isNotCacheable();
    }

    public void shouldDeleteParameterFromDatabase() {
        // given
        Transaction transaction = get(TransactionManager.class).openTransaction();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        parameterDAO.insert(transaction, parameter().withName("test").build());
        transaction.commit();
        transaction.closeWithArtifacts();

        // when
        Transaction deleteTransaction = get(TransactionManager.class).openTransaction();
        parameterDAO.delete(deleteTransaction, "test");
        deleteTransaction.commit();
        deleteTransaction.closeWithArtifacts();

        // then
        assertThat(parameterDAO.getParameter("test")).isNull();
    }

    public void shouldReturnListOfParameterNamesStoredInDB() {
        // given
        Transaction transaction = get(TransactionManager.class).openTransaction();
        ParameterDAO parameterDAO = get(ParameterDAO.class);
        parameterDAO.insert(transaction, parameter().withName("test1").build());
        parameterDAO.insert(transaction, parameter().withName("test2").build());
        transaction.commit();
        transaction.closeWithArtifacts();

        // when
        Set<String> parameters = parameterDAO.getParameterNames();

        // then
        assertThat(parameters).isNotEmpty().hasSize(2).containsOnly("test1", "test2");
    }
}
