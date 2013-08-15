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
package org.smartparam.engine.types.string;

import org.smartparam.engine.types.string.StringType;
import org.smartparam.engine.types.string.StringHolder;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 

/**
 * @author Przemek Hertel
 */
public class StringTypeTest {

    private StringType type = new StringType();

    @Test
    public void testEncode() {

        // przypadki testowe
        Object[][] testCases = {
            {new StringHolder("ABC"), "ABC"},
            {new StringHolder(""), ""},
            {new StringHolder(null), null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            StringHolder holder = (StringHolder) testCase[0];
            String expectedResult = (String) testCase[1];

            String result = type.encode(holder);
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"abc", new StringHolder("abc")},
            {"", new StringHolder("")},
            {null, new StringHolder(null)}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            StringHolder expectedHolder = (StringHolder) testCase[1];

            StringHolder holder = type.decode(text);
            assertEquals(expectedHolder.getValue(), holder.getValue());
        }
    }

    @Test
    public void testConvert() {

        // przypadki testowe: [argument][oczekiwana wartosc holdera]
        Object[][] testCases = {
            {new Integer(17), "17"},
            {new Float(1. / 3), "0.33333334"},
            {(byte) 100, "100"},
            {null, null},
            {"123", "123"},};

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            String expectedValue = (String) testCase[1];

            StringHolder holder = type.convert(obj);
            assertEquals(expectedValue, holder.getValue());
        }
    }
}
