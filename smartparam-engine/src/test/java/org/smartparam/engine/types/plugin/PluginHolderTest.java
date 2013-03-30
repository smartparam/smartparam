package org.smartparam.engine.types.plugin;

import java.util.Arrays;
import org.apache.log4j.spi.ErrorCode;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.ParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * @author Przemek Hertel
 */
public class PluginHolderTest {

    String functionName = "premium.calc.hh";

    PluginHolder h1 = new PluginHolder(functionName);

    PluginHolder h2 = new PluginHolder(null);

    @Test
    public void testGetValue() {
        assertEquals(functionName, h1.getValue());
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
        assertEquals(functionName, h1.getString());
        assertNull(h2.getString());
    }

    @Test()
    public void testIntValue() {

        // sprawdzenie wynikow testu - oczekiwany wyjatek typu: GETTING_WRONG_TYPE
        try {
            h1.intValue();
        } catch (ParamUsageException e) {
            assertEquals(SmartParamErrorCode.GETTING_WRONG_TYPE, e.getErrorCode());
        }
    }

    @Test(expected = ParamUsageException.class)
    public void testLongValue() {
        h1.longValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetLong() {
        h1.getLong();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetInteger() {
        h1.getInteger();
    }

    @Test
    public void testToString() {

        // sprawdzenie wynikow testu
        assertEquals("PluginHolder[premium.calc.hh]", h1.toString());
        assertEquals("PluginHolder[null]", h2.toString());
    }

    @Test
    public void testCompareTo() {

        // dane testowe
        PluginHolder[] array = {
            new PluginHolder("f.DF"),
            new PluginHolder(null),
            new PluginHolder("f.Ab"),
            new PluginHolder("f.z"),
            new PluginHolder("f.AB"),
            new PluginHolder("")
        };

        // oczekiwany wynik sortowania
        String[] expectedResult = {
            null,
            "",
            "f.AB",
            "f.Ab",
            "f.DF",
            "f.z"
        };

        // wykonanie sortowania
        Arrays.sort(array);

        // weryfikacja
        for (int i = 0; i < array.length; i++) {
            String fname = array[i].getString();
            String expected = expectedResult[i];
            assertEquals(expected, fname);
        }
    }

}
