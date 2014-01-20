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
package org.smartparam.engine.core.output;

import java.math.BigDecimal;
import java.util.Date;
import org.smartparam.engine.core.exception.SmartParamException;
import static org.testng.AssertJUnit.*;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.types.date.DateHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.number.NumberHolder;
import org.smartparam.engine.types.string.StringHolder;
import org.testng.annotations.Test;

/**
 * @author Przemek Hertel
 */
public class DefaultMultiValueTest {

    @Test
    public void testGetValue() {

        // zaleznosci
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h3 = new IntegerHolder(100L);

        // dane testowe
        Object[] values = {h1, h2, h3};

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(values);

        // oczekiwane wartosci
        assertSame(h1, mv.getHolder(0));
        assertSame(h2, mv.getHolder(1));
        assertSame(h3, mv.getHolder(2));
    }

    @Test
    public void testGetValue__exception() {

        // zaleznosci
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);

        // dane testowe
        Object[] values = {new ValueHolder[]{h1, h2}};       // 1 element typu AbstractHolder[]

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(values);

        // indeksy, z ktorych nie mozna pobrac wartosci AbstractHolder
        int[] indices = {0, 1, 2};

        // test
        for (int i = 0; i < indices.length; i++) {
            int k = indices[i];

            try {
                mv.getHolder(k);
                fail();
            } catch (SmartParamException e) {
                // success
            }
        }
    }

    @Test
    public void testGetString() {

        // zaleznosci
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h3 = new IntegerHolder(9L);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2, h3});

        // oczekiwane wartosci
        assertEquals("a", mv.getString(0));
        assertEquals("1", mv.getString(1));
        assertEquals("9", mv.getString(2));
    }

    @Test
    public void testGetBigDecimal() {

        // zaleznosci
        ValueHolder h1 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h2 = new NumberHolder(BigDecimal.TEN);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2});

        // oczekiwane wartosci
        assertEquals(BigDecimal.ONE, mv.getBigDecimal(0));
        assertEquals(BigDecimal.TEN, mv.getBigDecimal(1));
    }

    @Test
    public void testGetDate() {

        // zaleznosci
        Date d1 = new Date();
        Date d2 = new Date();
        ValueHolder h1 = new DateHolder(d1);
        ValueHolder h2 = new DateHolder(d2);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2});

        // oczekiwane wartosci
        assertEquals(d1, mv.getDate(0));
        assertEquals(d2, mv.getDate(1));
    }

    @Test
    public void testGetInteger() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(123L);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1});

        // oczekiwane wartosci
        assertEquals(new Integer(123), mv.getInteger(0));
    }

    @Test
    public void testGetLong() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(123L);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1});

        // oczekiwane wartosci
        assertEquals(new Long(123), mv.getLong(0));
    }

    @Test
    public void testGetEnum() {

        // przypadki testowe
        ValueHolder[] tests = {
            new StringHolder("A3"),
            new StringHolder("A4"),
            new StringHolder(null)
        };

        // oczekiwane wyniki
        LetterType[] expected = {
            LetterType.A3,
            LetterType.A4,
            null
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            ValueHolder h = tests[i];
            LetterType expectedResult = expected[i];

            MultiValue mv = new DefaultMultiValue(new Object[]{h});

            // test
            LetterType result = mv.getEnum(0, LetterType.class);

            // weryfikacja
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testGetEnum__illegalArgument() {

        // przypadki testowe
        ValueHolder[] tests = {
            new StringHolder("A9"),
            new StringHolder("")
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            ValueHolder h = tests[i];
            MultiValue mv = new DefaultMultiValue(new Object[]{h});

            // test
            try {
                mv.getEnum(0, LetterType.class);
                fail();

            } catch (GettingWrongTypeException e) {
                // success
            }
        }
    }

    @Test
    public void testGetArray() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(100L);
        ValueHolder h2 = new IntegerHolder(200L);

        // 1 element
        ValueHolder[] e1 = {h1, h2};

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{e1});       // 1 poziom typu tablicowego

        // oczekiwane wartosci
        assertArrayEquals(new ValueHolder[]{h1, h2}, mv.getArray(0));
    }

    @Test
    public void testGetArray__exception() {

        // zaleznosci
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);

        // dane testowe
        Object[] values = {h1, h2};       // nie ma elementow tablicowych

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(values);

        // indeksy, z ktorych nie mozna pobrac wartosci AbstractHolder
        int[] indices = {0, 1, 2, 3};

        // test
        for (int i = 0; i < indices.length; i++) {
            int k = indices[i];

            try {
                mv.getArray(k);
                fail();
            } catch (SmartParamException e) {
                // success
            }
        }
    }

    @Test
    public void testUnwrap() {

        // zaleznosci
        Date date = new Date();
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h3 = new IntegerHolder(100L);
        ValueHolder h4 = new DateHolder(date);

        Object element1 = h1;
        Object element2 = new ValueHolder[]{h2, h3};
        Object element3 = h4;

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{element1, element2, element3});

        // test
        Object[] unwrapped = mv.unwrap();

        // oczekiwany rezultat
        Object[] expectedResult = {
            "a",
            new Object[]{BigDecimal.ONE, 100L},
            date
        };

        // weryfikacja
        assertArrayEquals(expectedResult, unwrapped);
    }

    @Test
    public void testGetStringArray() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(100L);
        ValueHolder h2 = new IntegerHolder(200L);

        // 1 element tablicowy
        ValueHolder[] e1 = {h1, h2};

        // testowany obiekt - value(1) to tablica
        MultiValue mv = new DefaultMultiValue(new Object[]{e1});

        // oczekiwany wynik
        String[] expectedResult = {"100", "200"};

        // weryfikacja
        assertArrayEquals(expectedResult, mv.getStringArray(0));
    }

    @Test
    public void testGetDateArray() {

        // przykladowe dane
        Date d1 = new Date();
        Date d2 = new Date();

        // zaleznosci
        ValueHolder h1 = new DateHolder(d1);
        ValueHolder h2 = new DateHolder(d2);

        // 1 element tablicowy
        ValueHolder[] e1 = {h1, h2};

        // testowany obiekt - value(1) to tablica
        MultiValue mv = new DefaultMultiValue(new Object[]{e1});

        // oczekiwany wynik
        Date[] expectedResult = {d1, d2};

        // weryfikacja
        assertArrayEquals(expectedResult, mv.getDateArray(0));
    }

    @Test
    public void testGetIntegerArray() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(100L);
        ValueHolder h2 = new IntegerHolder(200L);

        // 1 element tablicowy
        ValueHolder[] e1 = {h1, h2};

        // testowany obiekt - value(1) to tablica
        MultiValue mv = new DefaultMultiValue(new Object[]{e1});

        // oczekiwany wynik
        Integer[] expectedResult = {100, 200};

        // weryfikacja
        assertArrayEquals(expectedResult, mv.getIntegerArray(0));
    }

    @Test
    public void testGetBigDecimalArray() {

        // zaleznosci
        ValueHolder h1 = new NumberHolder(BigDecimal.ZERO);
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);

        // 1 element tablicowy
        ValueHolder[] e1 = {h1, h2};

        // testowany obiekt - value(1) to tablica
        MultiValue mv = new DefaultMultiValue(new Object[]{e1});

        // oczekiwany wynik
        BigDecimal[] expectedResult = {BigDecimal.ZERO, BigDecimal.ONE};

        // weryfikacja
        assertArrayEquals(expectedResult, mv.getBigDecimalArray(0));
    }

    @Test
    public void testAsStrings() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(100L);
        ValueHolder h2 = new IntegerHolder(200L);

        // testowany obiekt - value(1) to tablica
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2});

        // oczekiwany wynik
        String[] expectedResult = {"100", "200"};

        // weryfikacja
        assertArrayEquals(expectedResult, mv.asStrings());
    }

    @Test
    public void testAsBigDecimals() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(100L);
        ValueHolder h2 = new IntegerHolder(200L);

        // testowany obiekt - value(1) to tablica
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2});

        // oczekiwany wynik
        BigDecimal[] expectedResult = {BigDecimal.valueOf(100), BigDecimal.valueOf(200)};

        // weryfikacja
        assertArrayEquals(expectedResult, mv.asBigDecimals());
    }

    @Test
    public void testToStringInline() {

        // zaleznosci
        Object[] values = {
            new StringHolder("AB"),
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new NumberHolder(new BigDecimal("1.23"))
        };

        // konfiguracja
        MultiValue mv = new DefaultMultiValue(values);

        // oczekiwany wynik
        String expectedResult = "[AB, [1, 2, 3], 1.23]";

        // test
        String result = mv.toStringInline();

        // weryfikacja
        assertEquals(expectedResult, result);
    }

    @Test
    public void testNextValue() {

        // zaleznosci
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h3 = new IntegerHolder(100L);

        // dane testowe
        Object[] values = {h1, h2, h3};

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(values);

        // oczekiwane wartosci
        assertSame(h1, mv.nextHolder());
        assertSame(h2, mv.nextHolder());
        assertSame(h3, mv.nextHolder());
    }

    @Test
    public void testNextString() {

        // zaleznosci
        ValueHolder h1 = new StringHolder("a");
        ValueHolder h2 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h3 = new IntegerHolder(9L);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2, h3});

        // oczekiwane wartosci
        assertEquals("a", mv.nextString());
        assertEquals("1", mv.nextString());
        assertEquals("9", mv.nextString());
    }

    @Test
    public void testNextBigDecimal() {

        // zaleznosci
        ValueHolder h1 = new NumberHolder(BigDecimal.ONE);
        ValueHolder h2 = new NumberHolder(BigDecimal.TEN);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2});

        // oczekiwane wartosci
        assertEquals(BigDecimal.ONE, mv.nextBigDecimal());
        assertEquals(BigDecimal.TEN, mv.nextBigDecimal());
    }

    @Test
    public void testNextDate() {

        // zaleznosci
        Date v1 = new Date();
        BigDecimal v2 = BigDecimal.ONE;
        Long v3 = 100L;

        ValueHolder h1 = new DateHolder(v1);
        ValueHolder h2 = new NumberHolder(v2);
        ValueHolder h3 = new IntegerHolder(v3);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2, h3});

        // oczekiwane wartosci
        assertEquals(v1, mv.nextDate());
        assertEquals(v2, mv.nextBigDecimal());
        assertEquals(v3, mv.nextLong());
    }

    @Test
    public void testNextInteger() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(123L);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1});

        // oczekiwane wartosci
        assertEquals(new Integer(123), mv.nextInteger());
    }

    @Test
    public void testNextLong() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(123L);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1});

        // oczekiwane wartosci
        assertEquals(new Long(123), mv.getLong(0));
    }

    @Test
    public void testNextEnum() {

        // przypadki testowe
        ValueHolder[] tests = {
            new StringHolder("A3"),
            new StringHolder("A4"),
            new StringHolder(null)
        };

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(tests);

        // weryfikacja
        assertEquals(LetterType.A3, mv.nextEnum(LetterType.class));
        assertEquals(LetterType.A4, mv.nextEnum(LetterType.class));
        assertEquals(null, mv.nextEnum(LetterType.class));
    }

    @Test
    public void testNextArray() {

        // zaleznosci
        ValueHolder h1 = new IntegerHolder(100L);
        ValueHolder h2 = new IntegerHolder(200L);
        ValueHolder h3 = new IntegerHolder(300L);

        // 2 elementy
        ValueHolder[] e1 = {h1, h2};
        ValueHolder[] e2 = {h3};

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{e1, e2});       // 2 poziomy typu tablicowego

        // oczekiwane wartosci
        assertArrayEquals(e1, mv.nextArray());
        assertArrayEquals(e2, mv.nextArray());
    }

    @Test
    public void testNextStringArray() {

        // zaleznosci
        StringHolder h1 = new StringHolder("A");
        StringHolder h2 = new StringHolder("B");
        StringHolder h3 = new StringHolder("C");

        // 3 elementy
        ValueHolder[] e1 = {h1, h2};
        ValueHolder[] e2 = {h2, h3};

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{e1, e2});       // 2 poziomy typu tablicowego

        // oczekiwane wartosci
        assertArrayEquals(new String[]{"A", "B"}, mv.nextStringArray());
        assertArrayEquals(new String[]{"B", "C"}, mv.nextStringArray());
    }

    @Test
    public void testNextBigDecimalArray() {

        // zaleznosci
        BigDecimal v0 = BigDecimal.ZERO;
        BigDecimal v1 = BigDecimal.ONE;
        BigDecimal v10 = BigDecimal.TEN;

        NumberHolder h1 = new NumberHolder(v0);
        NumberHolder h2 = new NumberHolder(v1);
        NumberHolder h3 = new NumberHolder(v10);

        // 3 elementy
        ValueHolder[] e1 = {h1, h2};
        ValueHolder[] e2 = {h2, h3};

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{e1, e2});       // 2 poziomy typu tablicowego

        // oczekiwane wartosci
        assertArrayEquals(new BigDecimal[]{v0, v1}, mv.nextBigDecimalArray());
        assertArrayEquals(new BigDecimal[]{v1, v10}, mv.nextBigDecimalArray());
    }

    @Test
    public void testNext__mixed() {

        // zaleznosci
        Date d1 = new Date();
        Date d2 = new Date();
        ValueHolder h1 = new DateHolder(d1);
        ValueHolder h2 = new DateHolder(d2);

        // testowany obiekt
        MultiValue mv = new DefaultMultiValue(new Object[]{h1, h2});

        // oczekiwane wartosci
        assertEquals(d1, mv.nextDate());
        assertEquals(d2, mv.nextDate());
    }

    private enum LetterType {

        A3,
        A4,
        A5

    }
}
