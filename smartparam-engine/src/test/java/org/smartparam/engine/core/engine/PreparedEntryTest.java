package org.smartparam.engine.core.engine;

import org.junit.*;
import org.smartparam.engine.model.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author Przemek Hertel
 */
public class PreparedEntryTest {

    PreparedEntry pe;

    @Before
    public void init() {
        pe = new PreparedEntry();
    }

    @Test
    public void testValue() {

        // zaleznosci
        String val = "value";

        // test
        pe.setValue(val);

        // weryfikacja
        assertEquals(val, pe.getValue());
    }

    @Test
    public void testFunction() {

        // zaleznosci
        Function f = mock(Function.class);

        // test
        pe.setFunction(f);

        // weryfikacja
        assertSame(f, pe.getFunction());
    }

    @Test
    public void testSetLevels() {

        // przypadki testowe
        String[][] tests = {
            {"A", "B", "C"},
            {"A", "B", null},
            {"A", null, null},
            {null, null, null},
            {},
            null
        };

        // oczekiwany rezultat
        String[][] expected = {
            {"A", "B", "C"},
            {"A", "B"},
            {"A"},
            {},
            {},
            {}
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            String[] levels = tests[i];
            String[] getlevels = expected[i];

            PreparedEntry entry = new PreparedEntry();
            entry.setLevels(levels);

            // weryfikacja
            assertArrayEquals(getlevels, entry.getLevels());
        }
    }

    @Test
    public void testGetLevel() {

        // inicjalizacja
        pe.setLevels(new String[]{"A", "B"});

        // testy
        assertEquals("A", pe.getLevel(1));
        assertEquals("B", pe.getLevel(2));
        assertEquals(null, pe.getLevel(3));
        assertEquals(null, pe.getLevel(0));
    }
}
