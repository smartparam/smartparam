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
package org.smartparam.engine.core.type;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.string.StringHolder;

/**
 * @author Przemek Hertel
 */
public class AbstractValueHolderTest {

    @Test
    public void testCompareTo() {

        // konfiguracja
        ValueHolder holder1 = new NonComparableHolder();
        ValueHolder holder2 = new StringHolder("abc");

        // test
        int result = holder1.compareTo(holder2);

        // weryfikacja
        assertEquals(0, result);
    }

    @Test
    public void testEquals() {

        // przypadki testowe
        Object[][] pairs = {
            {new StringHolder("ab"), new StringHolder("ab"), true},
            {new StringHolder("ab"), new StringHolder("ac"), false},
            {new IntegerHolder(1L), new IntegerHolder(1L), true},
            {new IntegerHolder(1L), new StringHolder("1"), false},
            {new StringHolder("ab"), null, false}
        };

        // testy
        for (int i = 0; i < pairs.length; i++) {
            Object[] test = pairs[i];
            ValueHolder h1 = (ValueHolder) test[0];
            ValueHolder h2 = (ValueHolder) test[1];
            boolean expectedResult = (Boolean) test[2];

            // test 1
            boolean result = h1.equals(h2);
            assertEquals(expectedResult, result);

            // test 2
            if (h2 != null) {
                result = h2.equals(h1);
                assertEquals(expectedResult, result);
            }
        }

    }

    private class NonComparableHolder extends AbstractValueHolder {

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean isComparable() {
            return false;
        }
    }
}
