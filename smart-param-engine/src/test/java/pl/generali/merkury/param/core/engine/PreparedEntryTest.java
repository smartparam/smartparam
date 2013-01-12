package pl.generali.merkury.param.core.engine;

import org.junit.*;
import static org.junit.Assert.*;
import pl.generali.merkury.param.model.Function;

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
        Function f = new Function();

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
