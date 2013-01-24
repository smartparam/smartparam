package org.smartparam.provider.jpa.model;

import org.smartparam.provider.jpa.model.JpaParameter;
import org.smartparam.provider.jpa.model.JpaLevel;
import org.smartparam.provider.jpa.model.JpaFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * @author Przemek Hertel
 */
public class JpaLevelTest {

    private JpaLevel level;

    @Before
    public void before() {
        level = new JpaLevel();
    }

    public void testConstructor__type() {

        // uzycie konstruktora
        level = new JpaLevel("number");

        // weryfikacja
        assertEquals("number", level.getType());
    }

    @Test
    public void testId() {

        // konfiguracja testu
        int expectedValue = 1234567;

        // test
        level.setId(expectedValue);
        int result = level.getId();

        // sprawdzenie wynikow testu
        assertEquals(expectedValue, result);
    }

    @Test
    public void testParameter() {

        // test
        JpaParameter p = new JpaParameter();
        level.setParameter(p);

        // weryfikacja
        assertSame(p, level.getParameter());
    }

    @Test
    public void testOrderNo() {

        // test
        level.setOrderNo(3);

        // weryfikacja
        assertEquals(3, level.getOrderNo());
    }

    @Test
    public void testLevelCrator() {
        // test
        JpaFunction lc = new JpaFunction();
        level.setLevelCreator(lc);

        // weryfikacja
        assertSame(lc, level.getLevelCreator());
    }

    @Test
    public void testType() {
        // test
        level.setType("integer");

        // weryfikacja
        assertEquals("integer", level.getType());
    }

    @Test
    public void testType__enum() {
        // test
        level.setType(Codes.STRING);

        // weryfikacja
        assertEquals("string", level.getType());
    }

    @Test
    public void testArray() {

        // weryfikacja defaulta
        assertFalse(level.isArray());

        // test
        level.setArray(true);

        // weryfikacja
        assertTrue(level.isArray());
    }

    @Test
    public void testMatcherCode() {
        // test
        level.setMatcherCode("between");

        // weryfikacja
        assertEquals("between", level.getMatcherCode());
    }

    @Test
    public void testValidator() {
        // test
        JpaFunction v = new JpaFunction();
        level.setValidator(v);

        // weryfikacja
        assertSame(v, level.getValidator());
    }

    @Test
    public void testLabel() {
        // test
        level.setLabel("label");

        // weryfikacja
        assertEquals("label", level.getLabel());
    }

    @Test
    public void testLabelKey() {
        // test
        level.setLabelKey("label.key");

        // weryfikacja
        assertEquals("label.key", level.getLabelKey());
    }

    @Test
    public void testToString__empty() {

        // oczekiwany wynik dla pustego obiektu
        String expected = "Level[id=0, cre=null, type=null]";

        // test
        assertEquals(expected, level.toString());
    }

    @Test
    public void testToString() {

        // przygotowanie obiektu
        level.setId(123);
        level.setType("string");
        level.setMatcherCode("between");
        level.setLevelCreator(new JpaFunction());
        level.getLevelCreator().setName("level.creator");
        level.setValidator(new JpaFunction());
        level.getValidator().setName("validator");

        // oczekiwany wynik dla wypelnionego
        String expected = "Level[id=123, cre=level.creator, type=string, matcher=between, validator=validator]";

        // test
        assertEquals(expected, level.toString());
    }

    private enum Codes {

        STRING, INTEGER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
