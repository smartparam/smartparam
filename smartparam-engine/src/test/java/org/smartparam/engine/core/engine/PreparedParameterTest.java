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
package org.smartparam.engine.core.engine;

import static org.testng.AssertJUnit.*;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.number.NumberType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.builder.LevelIndexTestBuilder.levelIndex;

/**
 * @author Przemek Hertel
 */
public class PreparedParameterTest {

    private PreparedParameter pp;

    @BeforeMethod
    public void init() {
        pp = new PreparedParameter();
    }

    @Test
    public void testName() {

        // konfiguracja testu
        String name = "par.name";

        // test
        pp.setName(name);
        String result = pp.getName();

        // sprawdzenie wynikow testu
        assertEquals(name, result);
    }

    @Test
    public void testType() {

        // konfiguracja testu
        Type<?> type = new NumberType();

        // test
        pp.setType(type);
        Type<?> result = pp.getType();

        // sprawdzenie wynikow testu
        assertSame(type, result);
    }

    @Test
    public void testLevels() {

        // zaleznosci
        PreparedLevel l1 = new PreparedLevel("L1", new IntegerType(), false, null, null);
        PreparedLevel l2 = new PreparedLevel("L2", new NumberType(), true, null, null);

        // konfiguracja testu
        PreparedLevel[] levels = {l1, l2};

        // test
        pp.setLevels(levels);
        PreparedLevel[] result = pp.getLevels();

        // sprawdzenie wynikow testu
        assertArrayEquals(result, levels);
    }

    @Test
    public void testIndex() {

        // konfiguracja testu
        LevelIndex<PreparedEntry> index = levelIndex().withLevelCount(3).build();

        // test
        pp.setIndex(index);
        LevelIndex<PreparedEntry> result = pp.getIndex();

        // sprawdzenie wynikow testu
        assertSame(index, result);

    }

    @Test
    public void testInputLevelsCount() {

        // when
        pp.setInputLevelsCount(3);

        // then
        assertEquals(3, pp.getInputLevelsCount());
    }

    @Test
    public void testNullable() {

        // test 1
        assertFalse(pp.isNullable());
        assertTrue(pp.isNotNull());

        // test 2
        pp.setNullable(true);
        assertTrue(pp.isNullable());
        assertFalse(pp.isNotNull());
    }
}
