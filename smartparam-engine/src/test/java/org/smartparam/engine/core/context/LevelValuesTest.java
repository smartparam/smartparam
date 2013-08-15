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
package org.smartparam.engine.core.context;

import java.math.BigDecimal;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * @author Przemek Hertel
 */
public class LevelValuesTest {

    @Test
    public void testConstructor() {

        // przypadki testowe
        // format: 1 kolumna - podane obiekty jako levelValues, 2 kolumna - levelValues zapisane w kontekscie
        Object[][] tests = {
            {"A", "B"}, new String[]{"A", "B"},
            {"A", 7}, new String[]{"A", "7"},
            {"A", 7, "C"}, new String[]{"A", "7", "C"},
            {1, null}, new String[]{"1", null},
            {'a', new BigDecimal("1.2")}, new String[]{"a", "1.2"}
        };

        // wykonanie testow
        for (int i = 0; i < tests.length; i+=2) {
            Object[] givenLevelValues = tests[i];
            String[] expectedLevelValues = (String[]) tests[i+1];

            // stworzenie kontekstu
            ParamContext ctx = new LevelValues(givenLevelValues);

            // weryfikacja tablicy levelValues
            assertArrayEquals(expectedLevelValues, ctx.getLevelValues());
        }
    }
}
