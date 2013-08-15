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
package org.smartparam.engine.types.integer;

import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.integer.IntegerHolder;
import java.math.BigDecimal;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 

/**
 * @author Przemek Hertel
 */
public class IntegerTypeTest {

    private IntegerType type = new IntegerType();

    @Test
    public void testEncode() {

        // przypadki testowe
        Object[][] testCases = {
            {-100L, "-100"},
            {0L, "0"},
            {999L, "999"},
            {2147483648L, "2147483648"},
            {null, null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Long number = (Long) testCase[0];
            String expectedResult = (String) testCase[1];

            String result = type.encode(new IntegerHolder(number));
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"0", 0L},
            {"-99", -99L},
            {"2147483648", 2147483648L},
            {null, null},
            {"", null},
            {"\t \n", null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            Long expectedValue = (Long) testCase[1];

            IntegerHolder holder = type.decode(text);
            assertEquals(expectedValue, holder.getValue());
        }
    }

    @Test
    public void testDecode__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac zdekodowane do IntegerHoldera
        String[] illegals = {"1.1", "9aa"};

        // wykonanie testow, oczekujemy wyjatku
        for (String text : illegals) {
            try {
                type.decode(text);
                fail();
            } catch (NumberFormatException nfe) {
                //ok
            }
        }
    }

    @Test
    public void testConvert() {

        // przypadki testowe: [argument][oczekiwana wartosc holdera]
        Object[][] testCases = {
            {new Long(17), 17L},
            {new Integer(50), 50L},
            {(byte) 100, 100L},
            {(short) 2000, 2000L},
            {null, null},
            {"", null},
            {" ", null},
            {"123", 123L},
            {String.valueOf(Long.MIN_VALUE), Long.MIN_VALUE},
            {String.valueOf(Long.MAX_VALUE), Long.MAX_VALUE}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            Long expectedValue = (Long) testCase[1];

            IntegerHolder holder = type.convert(obj);
            assertEquals(expectedValue, holder.getValue());
        }
    }

    @Test
    public void testConvert__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac skonwertowane do IntegerHoldera
        Object[] illegals = {"1.1", "9aa", 0.1d, 0.1f, BigDecimal.ZERO};

        // wykonanie testow, oczekujemy wyjatku
        for (Object obj : illegals) {
            try {
                type.convert(obj);
                fail();
            } catch (RuntimeException e) {
                assertTrue(e instanceof IllegalArgumentException || e instanceof NumberFormatException);
            }
        }
    }
}
