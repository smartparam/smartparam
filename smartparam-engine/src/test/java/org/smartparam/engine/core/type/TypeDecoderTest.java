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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.testng.AssertJUnit.*;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.integer.IntegerType;
import org.testng.annotations.Test;

/**
 * @author Przemek Hertel
 */
public class TypeDecoderTest {

    private Type<?> type = new IntegerType();

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"0", new IntegerHolder(0L)},
            {" -99", new IntegerHolder(-99L)},
            {null, new IntegerHolder(null)},
            {"\t \n", new IntegerHolder(null)}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            AbstractHolder expectedHolder = (AbstractHolder) testCase[1];

            AbstractHolder holder = TypeDecoder.decode(type, text);
            assertEquals(expectedHolder.getValue(), holder.getValue());
        }
    }

    @Test
    public void testDecode__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac zdekodowane do IntegerHoldera
        String[] illegals = {"1.1", "9aa", "."};

        // wykonanie testow, oczekujemy wyjatku
        for (String text : illegals) {
            try {
                TypeDecoder.decode(type, text);
                fail();
            } catch (TypeDecodingException e) {
                // success
            }
        }
    }

    @Test
    public void testConvert() {
        // przypadki testowe: [argument][oczekiwana wartosc holdera]
        Object[][] testCases = {
            {new Long(17), 17L},
            {new Integer(50), 50L},
            {null, null},
            {"", null},
            {" ", null},
            {"123", 123L}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            Long expectedValue = (Long) testCase[1];

            AbstractHolder holder = TypeDecoder.convert(type, obj);
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
                TypeDecoder.convert(type, obj);
                fail();
            } catch (TypeConversionException e) {
                // success
            }
        }
    }

    @Test
    public void testConvert__array() {

        // dane wejsciowe
        Object[][] arrays = {
            {1L, 2L, 3L},
            {}
        };

        // oczekiwane rezultaty
        Object[][] expectations = {
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new IntegerHolder[]{}
        };

        // wykonanie testow
        for (int i = 0; i < arrays.length; i++) {
            Object[] array = arrays[i];
            IntegerHolder[] expected = (IntegerHolder[]) expectations[i];

            AbstractHolder[] result = TypeDecoder.convert(type, array);
            checkArrays(expected, result);
        }
    }

    @Test
    public void testConvert__collection() {

        // dane wejsciowe
        Object[] arrays = {
            Arrays.asList(1L, 2L, 3L),
            new ArrayList<Long>()
        };

        // oczekiwane rezultaty
        Object[][] expectations = {
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new IntegerHolder[]{}
        };

        // wykonanie testow
        for (int i = 0; i < arrays.length; i++) {
            Collection<?> coll = (Collection<?>) arrays[i];
            IntegerHolder[] expected = (IntegerHolder[]) expectations[i];

            AbstractHolder[] result = new NonAbstractParamHelper().convert(type, coll);
            checkArrays(expected, result);
        }
    }

    private void checkArrays(IntegerHolder[] expected, AbstractHolder[] result) {
        assertTrue(result instanceof IntegerHolder[]);
        assertArrayEquals(expected, result);
    }

    private class NonAbstractParamHelper extends TypeDecoder {
    }
}
