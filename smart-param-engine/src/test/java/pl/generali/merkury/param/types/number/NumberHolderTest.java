package pl.generali.merkury.param.types.number;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.exception.ParamUsageException;

/**
 * Test klasy przechowujacej wartosci dla typu NumberType.
 *
 * @author Przemek Hertel
 */
public class NumberHolderTest {

    BigDecimal d1 = new BigDecimal(21474836470999L);

    BigDecimal d2 = new BigDecimal("0.01234567890123456789012345678901234567890123456789");

    NumberHolder h1 = new NumberHolder(d1);

    NumberHolder h2 = new NumberHolder(d2);

    NumberHolder h3 = new NumberHolder(null);

    @Test
    public void testGetValue() {

        assertEquals(d1, h1.getValue());
        assertEquals(d2, h2.getValue());
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

        assertEquals(d1.longValue(), h1.longValue());
        assertEquals(d2.longValue(), h2.longValue());
        assertEquals(0L, h3.longValue());
    }

    @Test()
    public void testIntValue() {

        assertTrue(d1.intValue() == h1.intValue());
        assertTrue(d2.intValue() == h2.intValue());
        assertEquals(0, h3.intValue());
    }

    @Test()
    public void testDoubleValue() {

        assertEquals(d1.doubleValue(), h1.doubleValue(), 0.0);
        assertEquals(d2.doubleValue(), h2.doubleValue(), 0.0);
        assertEquals(0, h3.doubleValue(), 0.0);
    }

    @Test
    public void testGetInteger() {

        assertEquals(new Integer(d1.intValue()), h1.getInteger());
        assertEquals(new Integer(d2.intValue()), h2.getInteger());
        assertEquals(null, h3.getInteger());
    }

    @Test
    public void testGetLong() {

        assertEquals(new Long(d1.longValue()), h1.getLong());
        assertEquals(new Long(d2.longValue()), h2.getLong());
        assertEquals(null, h3.getLong());
    }

    @Test
    public void testGetDouble() {

        assertEquals(new Double(d1.doubleValue()), h1.getDouble());
        assertEquals(new Double(d2.doubleValue()), h2.getDouble());
        assertEquals(null, h3.getDouble());
    }

    @Test
    public void testGetBigDecimal() {

        assertEquals(d1, h1.getBigDecimal());
        assertEquals(d2, h2.getBigDecimal());
        assertEquals(null, h3.getBigDecimal());
    }

    @Test()
    public void testGetString() {

        assertEquals("21474836470999", h1.getString());
        assertEquals("0.01234567890123456789012345678901234567890123456789", h2.getString());
        assertNull(h3.getString());
    }

    @Test(expected = ParamUsageException.class)
    public void testBooleanValue() {
        h1.booleanValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetBoolean() {
        h1.getBoolean();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetDate() {
        h1.getDate();
    }

    @Test
    public void testCompareTo() {

        // dane testowe
        NumberHolder[] array = {
            new NumberHolder(new BigDecimal("999")),
            new NumberHolder(new BigDecimal("777")),
            new NumberHolder(new BigDecimal("0.0000")),
            new NumberHolder(new BigDecimal("-20.00")),
            new NumberHolder(null),
            new NumberHolder(new BigDecimal("0")),};

        // oczekiwany wynik sortowania
        BigDecimal[] expectedResult = {
            null,
            new BigDecimal("-20.00"),
            new BigDecimal("0.0000"),
            new BigDecimal("0"),
            new BigDecimal("777"),
            new BigDecimal("999")
        };

        // wykonanie sortowania
        Arrays.sort(array);

        // weryfikacja
        for (int i = 0; i < array.length; i++) {
            BigDecimal value = array[i].getValue();
            BigDecimal expected = expectedResult[i];
            assertEquals(expected, value);
        }
    }
}
