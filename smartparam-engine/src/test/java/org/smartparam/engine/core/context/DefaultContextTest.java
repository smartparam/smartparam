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
import java.util.Date;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import static org.smartparam.engine.test.assertions.Assertions.*;

/**
 * @author Przemek Hertel
 */
public class DefaultContextTest {

    @Test
    public void shouldUseStringArrayAsLevelValuesWhenConstructingContext() {
        // given
        String[] levelValues = new String[]{"A", "B"};

        // when
        DefaultContext context = new DefaultContext((Object) levelValues);

        // then
        assertThat(context).hasLevelValues("A", "B");
    }

    @Test
    public void shouldUseObjectArrayAsLevelValuesWhenConstructingContext() {
        // given
        Object[] levelValues = new Object[] { 1, "A" };

        // when
        DefaultContext context = new DefaultContext("DUMMY", 1, levelValues);

        // then
        assertThat(context).hasLevelValues("1", "A");
    }

    @Test
    public void shouldUseStringAsKeyForNextValueWhenConstructingContext() {
        // given
        // when
        DefaultContext context = new DefaultContext("TEST1", 1, "TEST2", "2");

        // then
        assertThat(context).hasValue("TEST1", 1).hasValue("TEST2", "2");
    }

    @Test
    public void shouldPutValueIntoTheContextUnderGivenName() {
        // given
        DefaultContext context = new DefaultContext();

        // when
        context.set("TEST", "A");

        // then
        assertThat(context).hasValue("TEST", "A");
    }

    @Test
    public void shouldThrowExceptionIfhereIsAlreadyValueRegisteredUnderTheSameKey() {
        // given
        DefaultContext context = new DefaultContext();
        context.set("TEST", "B");

        // when
        try {
            context.set("TEST", "A");
            fail();
        }
        catch(SmartParamUsageException exception) {
            // then
            assertThat(exception).hasErrorCode(SmartParamErrorCode.ERROR_FILLING_CONTEXT);
        }
    }

    @Test
    public void shouldAllowOnOverwritingValuesInContextWhenCallingMethodWithOverwriteFlag() {
        // given
        DefaultContext context = new DefaultContext();
        context.set("TEST", "B");

        // when
        context.set("TEST", "A", true);

        // then
        assertThat(context).hasValue("TEST", "A");
    }

    @Test
    public void testConstructor() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext("numberValue", 17, new String[]{"A", "B"}, 19, 20L);

        // weryfikacja zawartosci kontekstu
        assertEquals(new Integer(17), ctx.get("numberValue"));
        assertEquals(new Integer(17), ctx.get("numberVALUE"));
        assertArrayEquals(new String[]{"A", "B"}, ctx.getLevelValues());
        assertEquals(new Integer(19), ctx.get("integer"));
        assertEquals(new Integer(19), ctx.get(Integer.class));
        assertEquals(new Long(20L), ctx.get("LONG"));
        assertEquals(new Long(20L), ctx.get(Long.class));
    }

    @Test
    public void testConstructor__onlyObjectArray() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext(new Object[]{"A", 'B', 9});

        /*
         * uwaga: wywolanie jest tozsame z:
         * DefaultContext ctx = new DefaultContext("A", 'B', 9);
         */

        // weryfikacja zawartosci kontekstu
        assertEquals(new Character('B'), ctx.get("A"));
        assertEquals(new Character('B'), ctx.get(Character.class));
        assertEquals(new Integer(9), ctx.get(Integer.class));
    }

    @Test
    public void testConstructor__levelValuesAsObjectArray() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext("test", "noise", new Object[]{"A", 'B', 9});

        // weryfikacja zawartosci kontekstu
        assertArrayEquals(new String[]{"A", "B", "9"}, ctx.getLevelValues());
        assertNull(ctx.get("A"));
        assertNull(ctx.get("integer"));
    }

    /**
     * Application specific context, ktory dziedziczy po DefaultContext
     */
    @Test
    public void testConstructor__specificContext() {

        // przykladowe dane
        Date date = new Date();
        SomeResult sr = new SomeResult();

        // inicjalizacja kontekstu
        MyContext ctx = new MyContext(date, "someResult", sr);

        // weryfikacja zawartosci kontekstu
        assertSame(date, ctx.getDate());
        assertSame(sr, ctx.get("someresult"));
    }

    /**
     * Application specific context, ktory dziedziczy po DefaultContext
     */
    @Test
    public void testConstructor__specificContext_setterCache() {

        // przykladowe dane
        Date date = new Date();

        /*
         * 1. MyContex ma setter(date)
         */
        MyContext ctx = new MyContext(date);
        assertSame(date, ctx.getDate());

        /*
         * 2. MyContext2 nie ma settera(date)
         */
        MyContext2 ctx2 = new MyContext2(date);
        assertSame(date, ctx2.get(Date.class));
    }

    @Test
    public void testConstructor__nullArg() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext("numberValue", 17, null);

        // weryfikacja zawartosci kontekstu
        assertEquals(new Integer(17), ctx.get("numberValue"));
        assertEquals(new Integer(17), ctx.get("numberVALUE"));
        assertEquals(1, ctx.getUserContext().size());
    }

    /**
     * Niepoprawnie wypelniany kontekst.
     */
    @Test
    public void testConstructor__illegalArgument() {

        try {
            new DefaultContext("key.1", 17, "key.2", 18, "key.3");
            fail();

        } catch (SmartParamUsageException e) {
            assertEquals(SmartParamErrorCode.ERROR_FILLING_CONTEXT, e.getErrorCode());
        }
    }

    @Test
    public void testGet__byClass() {

        // przykladowe dane
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext().set(sqlDate);

        // weryfikacja
        assertSame(sqlDate, ctx.get(java.sql.Date.class));  // ta sama klasa
        assertSame(sqlDate, ctx.get(Date.class));           // klasa bazowa
        assertSame(sqlDate, ctx.get("date"));               // po kluczu
    }

    @Test
    public void testGet__byClass_2() {

        // przykladowe dane
        Date date = new Date();

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext().set(date);

        // weryfikacja
        assertSame(date, ctx.get(Date.class));           // ta sama klasa
        assertSame(date, ctx.get("date"));               // po kluczu
        assertNull(ctx.get(java.sql.Date.class));        // klasa w kontekscie (Date) nie jest przypisywalna do java.sql.Date
    }

    @Test
    public void testGet__byClass_notInitialized() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext();

        // weryfikacja
        assertNull(ctx.get(Integer.class));
    }

    @Test
    public void testSet__duplicateAlloweOverwriteTrue() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext().set("key", 11).set("key", 22, true);

        // weryfikacja zawartosci kontekstu
        assertEquals(new Integer(22), ctx.get("key"));
    }

    @Test(expectedExceptions = SmartParamUsageException.class)
    public void testSet__duplicateAlloweOverwriteFalse() {

        // inicjalizacja kontekstu
        new DefaultContext().set("key", 11).set("key", 22);
    }

    @Test(expectedExceptions = SmartParamUsageException.class)
    public void testSetArg__setterException() {

        // inicjalizacja kontekstu - setter(BigDecimal) rzuca wyjatek
        new MyContext(BigDecimal.valueOf(7));
    }

    @Test
    public void testWithLevelValues() {

        // oczekiwany wynik
        String[] expected = {"A", "7", "C"};

        assertArrayEquals(expected, new MyContext().withLevelValues("A", "7", "C").getLevelValues());
        assertArrayEquals(expected, new MyContext().withLevelValues("A", 7, 'C').getLevelValues());
    }

    @Test
    public void testUserContext() {

        // inicjalizacja kontekstu
        DefaultContext ctx = new DefaultContext();

        // test 1
        assertNull(ctx.getUserContext());

        // test 2
        ctx.set("k1", new Integer(1));
        assertEquals(new Integer(1), ctx.getUserContext().get("k1"));
    }

    private class SomeResult {
    }

    private class MyContext extends DefaultContext {

        private Date date;

        private SomeResult someResult;

        private MyContext(Object... args) {
            super(args);
        }

        public Date getDate() {
            return date;
        }

        // setter prywatny - zostanie udostepniony przez refleksje
        private void setDate(Date date) {
            this.date = date;
        }

        public SomeResult getSomeResult() {
            return someResult;
        }

        // setter publiczny - kontekst nie musi zmieniac dostepnosci tej metody
        public void setSomeResult(SomeResult someResult) {
            this.someResult = someResult;
        }

        public void failingSetter(BigDecimal number) {
            number.divide(BigDecimal.ZERO);
        }
    }

    private class MyContext2 extends DefaultContext {

        private MyContext2(Object... args) {
            super(args);
        }
    }
}
