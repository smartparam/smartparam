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

import java.util.Arrays;
import java.util.List;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.metadata.LevelForm;
import org.smartparam.repository.jdbc.integration.DatabaseTest;
import org.smartparam.repository.jdbc.model.JdbcLevel;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.assertThat;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.repository.jdbc.test.builder.JdbcLevelTestBuilder.jdbcLevel;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = "integration")
public class LevelDAOTest extends DatabaseTest {

    public void shouldInsertNewLevelIntoDatabase() {
        // given
        database().withParameter("parameter").build();
        LevelDAO levelDAO = get(LevelDAO.class);
        JdbcLevel level = jdbcLevel().withName("test").withLevelCreator("testCreator")
                .withMatcher("testMatcher").withType("testType").withOrder(0).array().build();
        QueryRunner runner = queryRunner();

        // when
        levelDAO.insert(runner, level, "parameter");
        runner.commit();

        List<JdbcLevel> levels = levelDAO.getJdbcLevels(runner, "parameter");
        runner.close();

        // then
        assertThat(levels).hasSize(1);
        assertThat(levels.get(0)).isNotNull().hasName("test")
                .hasLevelCreator("testCreator").hasMatcher("testMatcher")
                .hasType("testType").isArray();
    }

    @Test
    public void shouldInsertAsLastLevelWhenInsertingSingleLevel() {
        // given
        database().withParameter("parameter").withLevels("parameter", 3).build();
        LevelDAO levelDAO = get(LevelDAO.class);
        Level level = level().withName("level").withType("string").build();
        QueryRunner runner = queryRunner();

        // when
        long levelId = levelDAO.insert(runner, level, "parameter");

        JdbcLevel savedLevel = levelDAO.getLevel(runner, levelId);
        runner.close();

        // then
        assertThat(savedLevel.getOrderNo()).isEqualTo(3);

    }

    @Test
    public void shouldInsertLevelsForParameterOverridingOrder() {
        // given
        database().withParameter("parameter").build();
        LevelDAO levelDAO = get(LevelDAO.class);
        Level level = level().withName("test").withType("string").build();
        QueryRunner runner = queryRunner();

        // when
        levelDAO.insertParameterLevels(runner, Arrays.asList(level), "parameter");
        runner.commit();

        List<JdbcLevel> levels = levelDAO.getJdbcLevels(runner, "parameter");
        runner.close();

        // then
        assertThat(levels).hasSize(1);
        assertThat(levels.get(0).getOrderNo()).isEqualTo(0);
    }

    @Test
    public void shouldDeleteLevelsForParameter() {
        // given
        database().withParameter("parameter").withLevels("parameter", 2).build();
        LevelDAO levelDAO = get(LevelDAO.class);
        QueryRunner runner = queryRunner();

        // when
        levelDAO.deleteParameterLevels(runner, "parameter");
        runner.close();

        // then
        assertDatabase().hasNoLevelsForParameter("parameter").close();
    }

    @Test
    public void shouldDeleteLevelWithGivenId() {
        // given
        database().withParameter("parameter").build();
        LevelDAO levelDAO = get(LevelDAO.class);
        QueryRunner runner = queryRunner();

        long levelToDelete = levelDAO.insert(runner, level().withName("level").withType("string").build(), "parameter");

        // when
        levelDAO.delete(runner, "parameter", levelToDelete);
        runner.close();

        // then
        assertDatabase().hasNoLevelsForParameter("parameter").close();
    }

    @Test
    public void shouldUpdateContentsOfLevel() {
        // given
        database().withParameter("parameter").build();
        LevelDAO levelDAO = get(LevelDAO.class);
        QueryRunner runner = queryRunner();

        long levelToUpdate = levelDAO.insert(runner, level().withName("level").withType("string").build(), "parameter");

        // when
        LevelForm levelForm = new LevelForm().rename("renamedLevel");
        levelDAO.update(runner, levelToUpdate, levelForm);

        Level level = levelDAO.getLevel(runner, levelToUpdate);
        runner.close();

        // then
        assertThat(level).hasName("renamedLevel");
    }

    @Test
    public void shouldReorderLevelsAccordingToIdsOrdering() {
        // given
        database().withParameter("parameter").build();
        LevelDAO levelDAO = get(LevelDAO.class);
        QueryRunner runner = queryRunner();

        long level1Id = levelDAO.insert(runner, level().withName("level1").withType("string").build(), "parameter");
        long level2Id = levelDAO.insert(runner, level().withName("level2").withType("string").build(), "parameter");

        // when
        levelDAO.reorder(runner, new long[] {level2Id, level1Id});

        List<JdbcLevel> levels = levelDAO.getJdbcLevels(runner, "parameter");
        runner.close();

        // then
        assertThat(levels.get(0)).hasName("level2");
    }
}
