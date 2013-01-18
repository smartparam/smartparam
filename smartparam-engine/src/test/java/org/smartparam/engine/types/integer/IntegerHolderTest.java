package org.smartparam.engine.types.integer;

import org.smartparam.engine.types.integer.IntegerHolder;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.ParamUsageException;

/**
 * Test klasy przechowujacej wartosci dla typu IntegerType.
 *
 * @author Przemek Hertel
 */
public class IntegerHolderTest {

    Long long1 = 2147483647L;   // maksymalna wartosc dla int

    Long long2 = 2147483648L;   // wartosc o 1 wieksza od max int

    IntegerHolder h1 = new IntegerHolder(long1);

    IntegerHolder h2 = new IntegerHolder(long2);

    IntegerHolder h3 = new IntegerHolder(null);

    @Test
    public void testGetValue() {

        assertEquals(long1, h1.getValue());
        assertEquals(long2, h2.getValue());
        assertNull(h3.getValue());
    }

    @Test
    public void testIsNull() {

        assertFalse(h1.isNull());
        assertFalse(h2.isNull());
        assertTrue(h3.isNull());
    }

    @Test
    public void testIsNotNull() {

        assertTrue(h1.isNotNull());
        assertTrue(h2.isNotNull());
        assertFalse(h3.isNotNull());
    }

    @Test()
    public void testLongValue() {

        assertEquals(long1.longValue(), h1.longValue());
        assertEquals(long2.longValue(), h2.longValue());
        assertEquals(0L, h3.longValue());
    }

    @Test()
    public void testIntValue() {

        assertTrue(long1.longValue() == (long) h1.intValue());
        assertTrue(long2.longValue() != (long) h2.intValue()); // liczba odczytana przez intValue jest rozna od przechowywanej
        assertEquals(0, h3.intValue());
    }

    @Test(expected = ParamUsageException.class)
    public void testDoubleValue() {
        h1.doubleValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testBooleanValue() {
        h1.doubleValue();
    }

    @Test
    public void testGetLong() {

        assertEquals(long1, h1.getLong());
        assertEquals(long2, h2.getLong());
        assertEquals(null, h3.getLong());
    }

    @Test
    public void testGetInteger() {

        assertEquals(new Integer(long1.intValue()), h1.getInteger());
        assertEquals(new Integer(long2.intValue()), h2.getInteger());
        assertEquals(null, h3.getInteger());
    }

    @Test(expected = ParamUsageException.class)
    public void testGetBoolean() {
        h1.getBoolean();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetDouble() {
        h1.getDouble();
    }

    @Test
    public void testGetBigDecimal() {
        BigDecimal bd1 = new BigDecimal(long1);
        BigDecimal bd2 = new BigDecimal(long2);
        BigDecimal bd3 = null;

        assertEquals(bd1, h1.getBigDecimal());
        assertEquals(bd2, h2.getBigDecimal());
        assertEquals(bd3, h3.getBigDecimal());
    }

    @Test
    public void testGetString() {

        assertEquals("2147483647", h1.getString());
        assertEquals("2147483648", h2.getString());
        assertNull(h3.getString());
    }

    @Test
    public void testEquals() {
        // dane wejsciowe - pary rowne
        IntegerHolder[][] equalCases = {
            {new IntegerHolder(null), new IntegerHolder(null)},
            {new IntegerHolder(long1), new IntegerHolder(long1)},
            {new IntegerHolder(long2), new IntegerHolder(long2)}
        };

        // dane wejsciowe - pary rozne
        IntegerHolder[][] notEqualCases = {
            {new IntegerHolder(null), new IntegerHolder(long1)},
            {new IntegerHolder(long1), new IntegerHolder(long2)},
            {new IntegerHolder(long2), new IntegerHolder(null)}
        };

        // weryfikacja - pary rowne
        for (IntegerHolder[] pair : equalCases) {
            assertTrue(pair[0].equals(pair[1]));
            assertTrue(pair[1].equals(pair[0]));
        }

        // weryfikacja - pary rozne
        for (IntegerHolder[] pair : notEqualCases) {
            assertFalse(pair[0].equals(pair[1]));
            assertFalse(pair[1].equals(pair[0]));
        }
    }

    @Test
    public void testHashCode() {

        // rozne hashcode
        int c1 = h1.hashCode();
        int c2 = h2.hashCode();
        int c3 = h3.hashCode();
        assertTrue(c1 != c2);
        assertTrue(c1 != c3);
        assertTrue(c2 != c3);

        // rowny hashcode
        int c4 = new IntegerHolder(long1).hashCode();
        int c5 = new IntegerHolder(null).hashCode();
        assertEquals(c1, c4);
        assertEquals(c3, c5);
    }

    @Test
    public void testCompareTo() {

        // dane testowe
        IntegerHolder[] array = {
            new IntegerHolder(999L),
            new IntegerHolder(222L),
            new IntegerHolder(null),
            new IntegerHolder(888L),
            new IntegerHolder(null)
        };

        // oczekiwany wynik sortowania
        Long[] expectedResult = {
            null,
            null,
            222L,
            888L,
            999L
        };

        // wykonanie sortowania
        Arrays.sort(array);

        // weryfikacja
        for (int i = 0; i < array.length; i++) {
            Long value = array[i].getValue();
            Long expected = expectedResult[i];
            assertEquals(expected, value);
        }

    }
}
