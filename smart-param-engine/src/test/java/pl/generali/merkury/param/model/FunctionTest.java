package pl.generali.merkury.param.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;

/**
 * @author Przemek Hertel
 */
public class FunctionTest {

    private Function f;

    @Before
    public void init() {
        f = new Function();
    }

    @Test
    public void testId() {

        // konfiguracja testu
        int expectedId = 20120220;

        // test
        f.setId(expectedId);

        // weryfikacja
        assertEquals(expectedId, f.getId());
    }

    @Test
    public void testLevelCreator() {

        // weryfikacja pustego obiektu
        assertFalse(f.isLevelCreator());

        // test
        f.setLevelCreator(true);

        // weryfikacja
        assertTrue(f.isLevelCreator());
    }

    @Test
    public void testVersionSelector() {

        // weryfikacja pustego obiektu
        assertFalse(f.isVersionSelector());

        // test
        f.setVersionSelector(true);

        // weryfikacja
        assertTrue(f.isVersionSelector());
    }

    @Test
    public void testPlugin() {

        // weryfikacja pustego obiektu
        assertFalse(f.isPlugin());

        // test
        f.setPlugin(true);

        // weryfikacja
        assertTrue(f.isPlugin());
    }

    @Test
    public void testName() {

        // weryfikacja pustego obiektu
        assertNull(f.getName());

        // test
        f.setName("name");

        // weryfikacja
        assertEquals("name", f.getName());
    }

    @Test
    public void testType() {

        // weryfikacja pustego obiektu
        assertNull(f.getType());

        // test
        f.setType("number");

        // weryfikacja
        assertEquals("number", f.getType());
    }

    @Test
    public void testImplementation() {

        // weryfikacja pustego obiektu
        assertNull(f.getImplementation());

        // zaleznosci
        FunctionImpl impl = mock(FunctionImpl.class);

        // test
        f.setImplementation(impl);

        // weryfikacja
        assertSame(impl, f.getImplementation());
    }

    @Test
    public void testToString() {

        // oczekiwany wynik dla pustego obiektu
        String expected = "Function#0[name=null, type=null]";

        // test
        assertEquals(expected, f.toString());
    }

    @Test
    public void testToString__flags() {

        // konfiguracja obiektu
        f.setId(1234);
        f.setName("plugin.calc.hh");
        f.setType("number");

        // flagi {V, L, P}
        boolean[][] flagTests = {
            {true, true, true},
            {true, true, false},
            {true, false, true},
            {true, false, false},

            {false, true, true},
            {false, true, false},
            {false, false, true},
            {false, false, false}
        };

        // oczekiwane wartosci
        String[] expected = {
            "Function#1234[name=plugin.calc.hh, type=number, flags=VLP]",
            "Function#1234[name=plugin.calc.hh, type=number, flags=VL]",
            "Function#1234[name=plugin.calc.hh, type=number, flags=VP]",
            "Function#1234[name=plugin.calc.hh, type=number, flags=V]",

            "Function#1234[name=plugin.calc.hh, type=number, flags=LP]",
            "Function#1234[name=plugin.calc.hh, type=number, flags=L]",
            "Function#1234[name=plugin.calc.hh, type=number, flags=P]",
            "Function#1234[name=plugin.calc.hh, type=number]"
        };

        // testy
        for (int i = 0; i < flagTests.length; i++) {
            boolean[] flags = flagTests[i];
            String expectedResult = expected[i];

            // przygotowanie obiektu
            f.setVersionSelector(flags[0]);
            f.setLevelCreator(flags[1]);
            f.setPlugin(flags[2]);

            // test
            String result = f.toString();

            // weryfikacja
            assertEquals(expectedResult, result);
        }
    }
}
