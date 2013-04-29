package org.smartparam.engine.types.string;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * Test klasy przechowujacej wartosci dla typu StringType.
 *
 * @author Przemek Hertel
 */
public class StringHolderTest {

    StringHolder h1 = new StringHolder("abc"); // przechowuje wartosc rozna od null (immutable)

    StringHolder h2 = new StringHolder(null);  // przechowuje null (immutable)

    @Test
    public void testGetValue() {

        // sprawdzenie wynikow testu
        assertEquals("abc", h1.getValue());
        assertNull(h2.getValue());
    }

    @Test
    public void testIsNull() {

        // sprawdzenie wynikow testu
        assertFalse(h1.isNull());
        assertTrue(h2.isNull());
    }

    @Test
    public void testIsNotNull() {

        // sprawdzenie wynikow testu
        assertTrue(h1.isNotNull());
        assertFalse(h2.isNotNull());
    }

    @Test
    public void testGetString() {

        // sprawdzenie wynikow testu
        assertEquals("abc", h1.getString());
        assertNull(h2.getString());
    }

    @Test()
    public void testIntValue() {

        // sprawdzenie wynikow testu - oczekiwany wyjatek typu: GETTING_WRONG_TYPE
        try {
            h1.intValue();
        } catch (SmartParamUsageException e) {
            assertEquals(SmartParamErrorCode.GETTING_WRONG_TYPE, e.getErrorCode());
        }
    }

    @Test(expected = SmartParamUsageException.class)
    public void testLongValue() {
        h1.longValue();
    }

    @Test(expected = SmartParamUsageException.class)
    public void testGetLong() {
        h1.getLong();
    }

    @Test(expected = SmartParamUsageException.class)
    public void testGetInteger() {
        h1.getInteger();
    }

    @Test
    public void testToString() {

        // sprawdzenie wynikow testu
        assertEquals("StringHolder[abc]", h1.toString());
        assertEquals("StringHolder[null]", h2.toString());
    }

    @Test
    public void testCompareTo() {

        // dane testowe
        StringHolder[] array = {
            new StringHolder("DF"),
            new StringHolder(null),
            new StringHolder("Ab"),
            new StringHolder("z"),
            new StringHolder("AB"),
            new StringHolder("")
        };

        // oczekiwany wynik sortowania
        String[] expectedResult = {
            null,
            "",
            "AB",
            "Ab",
            "DF",
            "z"
        };

        // wykonanie sortowania
        Arrays.sort(array);

        // weryfikacja
        for (int i = 0; i < array.length; i++) {
            String value = array[i].getValue();
            String expected = expectedResult[i];
            assertEquals(expected, value);
        }
    }
}
