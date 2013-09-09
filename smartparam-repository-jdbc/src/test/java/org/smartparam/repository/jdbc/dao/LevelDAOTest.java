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

import org.smartparam.engine.model.Level;
import org.smartparam.engine.test.builder.LevelTestBuilder;
import org.smartparam.repository.jdbc.core.transaction.Transaction;
import org.smartparam.repository.jdbc.core.transaction.TransactionManager;
import org.smartparam.repository.jdbc.integration.DatabaseTest;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class LevelDAOTest extends DatabaseTest {

    public void shouldInsertNewLevelIntoDatabase() {
        // given
        Transaction transaction = get(TransactionManager.class).openTransaction();
        LevelDAO levelDAO = get(LevelDAO.class);
        Level level = LevelTestBuilder.level().withName("test").withLevelCreator("testCreator")
                .withMatcher("testMatcher").withType("testType").array().build();

        // when
        levelDAO.insert(transaction, level);
        transaction.commit();
        transaction.closeWithArtifacts();
        Level resultingLevel = levelDAO.getLevel(1);

        // then
        assertThat(resultingLevel).isNotNull().hasName("test")
                .hasLevelCreator("testCreator").hasMatcher("testMatcher")
                .hasType("testType").isArray();
    }

}
