package org.smartparam.engine.types.bool;

import java.util.Arrays;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import org.smartparam.engine.core.exception.SmartParamUsageException;

/**
 * @author Przemek Hertel
 */
public class BooleanHolderTest {

    Boolean b1 = false;

    Boolean b2 = true;

    Boolean b3 = null;

    BooleanHolder h1 = new BooleanHolder(b1);

    BooleanHolder h2 = new BooleanHolder(b2);

    BooleanHolder h3 = new BooleanHolder(b3);

    @Test
    public void testGetValue() {

        assertEquals(b1, h1.getValue());
        assertEquals(b2, h2.getValue());
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

//    @Test()
//    public void testLongValue() {
//
//        assertEquals(long1.longValue(), h1.longValue());
//        assertEquals(long2.longValue(), h2.longValue());
//        assertEquals(0L, h3.longValue());
//    }
//
//    @Test()
//    public void testIntValue() {
//
//        assertTrue(long1.longValue() == (long) h1.intValue());
//        assertTrue(long2.longValue() != (long) h2.intValue()); // liczba odczytana przez intValue jest rozna od przechowywanej
//        assertEquals(0, h3.intValue());
//    }


    @Test
    public void testGetBoolean() {
        assertEquals(Boolean.FALSE, h1.getBoolean());
        assertEquals(Boolean.TRUE, h2.getBoolean());
        assertEquals(null, h3.getBoolean());
    }

    @Test
    public void testBooleanValue() {
        assertFalse(h1.booleanValue());
        assertTrue(h2.booleanValue());
        assertFalse(h3.booleanValue());
    }

//    @Test
//    public void testGetLong() {
//
//        assertEquals(long1, h1.getLong());
//        assertEquals(long2, h2.getLong());
//        assertEquals(null, h3.getLong());
//    }
//
//    @Test
//    public void testGetInteger() {
//
//        assertEquals(new Integer(long1.intValue()), h1.getInteger());
//        assertEquals(new Integer(long2.intValue()), h2.getInteger());
//        assertEquals(null, h3.getInteger());
//    }


    @Test(expectedExceptions = SmartParamUsageException.class)
    public void testGetDouble() {
        h1.getDouble();
    }

    @Test(expectedExceptions = SmartParamUsageException.class)
    public void testDoubleValue() {
        h1.doubleValue();
    }

//    @Test
//    public void testGetBigDecimal() {
//        BigDecimal bd1 = new BigDecimal(long1);
//        BigDecimal bd2 = new BigDecimal(long2);
//        BigDecimal bd3 = null;
//
//        assertEquals(bd1, h1.getBigDecimal());
//        assertEquals(bd2, h2.getBigDecimal());
//        assertEquals(bd3, h3.getBigDecimal());
//    }

    @Test
    public void testGetString() {

        assertEquals("false", h1.getString());
        assertEquals("true", h2.getString());
        assertEquals(null, h3.getString());
    }

    @Test
    public void testEquals() {
        // dane wejsciowe - pary rowne
        BooleanHolder[][] equalCases = {
            {new BooleanHolder(null), new BooleanHolder(null)},
            {new BooleanHolder(b1), new BooleanHolder(b1)},
            {new BooleanHolder(b2), new BooleanHolder(b2)}
        };

        // dane wejsciowe - pary rozne
        BooleanHolder[][] notEqualCases = {
            {new BooleanHolder(null), new BooleanHolder(b1)},
            {new BooleanHolder(b1), new BooleanHolder(b2)},
            {new BooleanHolder(b2), new BooleanHolder(null)}
        };

        // weryfikacja - pary rowne
        for (BooleanHolder[] pair : equalCases) {
            assertTrue(pair[0].equals(pair[1]));
            assertTrue(pair[1].equals(pair[0]));
        }

        // weryfikacja - pary rozne
        for (BooleanHolder[] pair : notEqualCases) {
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
        int c4 = new BooleanHolder(b1).hashCode();
        int c5 = new BooleanHolder(null).hashCode();
        assertEquals(c1, c4);
        assertEquals(c3, c5);
    }

    @Test
    public void testCompareTo() {

        // given
        BooleanHolder[] array = {
            new BooleanHolder(true),
            new BooleanHolder(null),
            new BooleanHolder(false)
        };

        // when
        Arrays.sort(array);

        // then - expected sorted array
        assertEquals(null, array[0].getValue());
        assertEquals(false, (boolean) array[1].getValue());
        assertEquals(true, (boolean) array[2].getValue());
    }

}
