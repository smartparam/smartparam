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
package org.smartparam.engine.core.prepared;

import org.testng.annotations.Test;

import static org.smartparam.engine.core.parameter.entry.ParameterEntryTestBuilder.parameterEntry;
import static org.testng.AssertJUnit.*;

/**
 * @author Przemek Hertel
 */
public class PreparedEntryTest {

    @Test
    public void testSetLevels() {

        // przypadki testowe
        String[][] tests = {
            {"A", "B", "C"},
            {"A", "B", null},
            {"A", null, null},
            {null, null, null},
            {},
            null
        };

        // oczekiwany rezultat
        String[][] expected = {
            {"A", "B", "C"},
            {"A", "B"},
            {"A"},
            {},
            {},
            {}
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            String[] levels = tests[i];
            String[] getlevels = expected[i];

            PreparedEntry entry = new PreparedEntry(parameterEntry().withLevels(levels).build());

            // weryfikacja
            assertArrayEquals(getlevels, entry.getLevels());
        }
    }

    @Test
    public void testGetLevel() {
        // inicjalizacja
        PreparedEntry entry = new PreparedEntry(parameterEntry().withLevels("A", "B").build());

        // testy
        assertEquals("A", entry.getLevel(1));
        assertEquals("B", entry.getLevel(2));
        assertEquals(null, entry.getLevel(3));
        assertEquals(null, entry.getLevel(0));
    }
}
