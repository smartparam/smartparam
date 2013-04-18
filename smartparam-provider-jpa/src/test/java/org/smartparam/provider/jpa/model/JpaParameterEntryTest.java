package org.smartparam.provider.jpa.model;

import org.junit.Test;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.model.Function;
import org.smartparam.engine.model.Parameter;

import static org.junit.Assert.*;

/**
 * @author Przemek Hertel
 */
public class JpaParameterEntryTest {

    @Test
    public void testConstructors() {

        // obiekty pomocnicze
        JpaFunction f1 = new JpaFunction();
        JpaFunction f2 = new JpaFunction();

        // obiekty stworzone przez rozne konstruktory
        JpaParameterEntry[] tests = {
            new JpaParameterEntry(),
            new JpaParameterEntry(new String[]{}, "value", null),
            new JpaParameterEntry(new String[]{"1", "2"}, "value", f1),
            new JpaParameterEntry(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, null, f2),
            new JpaParameterEntry("1", "2", "3"),
            new JpaParameterEntry("1", "2", "3", "4", "5", "6", "7", "8"),
            new JpaParameterEntry("1", "2", "3", "4", "5", "6", "7", "8", "9"),
            new JpaParameterEntry("1;2;3", f1),
            new JpaParameterEntry("1;2;3;4;5;6;7;8;9;10;11", f1),
            new JpaParameterEntry("", "val"),
            new JpaParameterEntry("1;2;3;4", "val"),
            new JpaParameterEntry(new String[]{}, "v"),
            new JpaParameterEntry(new String[]{"A"}, "v"),
            new JpaParameterEntry("a;b;;;e;f;g;h;i", null, f1),
            new JpaParameterEntry(new String[]{"X", "Y"}, "V", f1)
        };

        // oczekiwana zawartosc obiektow JpaParameterEntry:
        // [tablica leveli] [value] [function]
        Object[][] expectations = {
            new Object[]{new String[0], null, null},
            new Object[]{new String[0], "value", null},
            new Object[]{new String[]{"1", "2"}, "value", f1},
            new Object[]{new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, null, f2},
            new Object[]{new String[]{"1", "2", "3"}, null, null},
            new Object[]{new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}, null, null},
            new Object[]{new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, null, null},
            new Object[]{new String[]{"1", "2", "3"}, null, f1},
            new Object[]{new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}, null, f1},
            new Object[]{new String[]{""}, "val", null},
            new Object[]{new String[]{"1", "2", "3", "4"}, "val", null},
            new Object[]{new String[]{}, "v", null},
            new Object[]{new String[]{"A"}, "v", null},
            new Object[]{new String[]{"a", "b", "", "", "e", "f", "g", "h", "i"}, null, f1},
            new Object[]{new String[]{"X", "Y"}, "V", f1}
        };

        // wykonanie testow
        for (int i = 0; i < tests.length; i++) {
            JpaParameterEntry pe = tests[i];
            Object[] expected = expectations[i];

            String[] expectedLevels = (String[]) expected[0];
            String expectedValue = (String) expected[1];
            Function expectedFunction = (Function) expected[2];

            assertArrayEquals(expectedLevels, pe.getLevels());
            assertEquals(expectedValue, pe.getValue());
            assertEquals(expectedFunction, pe.getFunction());

        }
    }

    @Test
    public void testGetLevel() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry("1;2;3;4;5;6;7;8;9", "value");

        // weryfikacja
        assertEquals(null, pe.getLevel(0));
        assertEquals("1", pe.getLevel(1));
        assertEquals("8", pe.getLevel(8));
        assertEquals("9", pe.getLevel(9));
        assertEquals(null, pe.getLevel(10));
    }

    /*
     * Przykladowy scenariusz:
     * hibernate wola settery level1, ..., level8 w kolejnosci podczas wczytywania rekordu z bazy
     */
    @Test
    public void testSetLevel__scenario1() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry();

        // scenariusz
        pe.setLevel1("1");
        pe.setLevel2("2");
        pe.setLevel3("3");
        pe.setLevel4("4");
        pe.setLevel5("5");
        pe.setLevel6("6");
        pe.setLevel7(null);
        pe.setLevel8(null);

        // weryfikacja
        assertArrayEquals(new String[]{"1", "2", "3", "4", "5", "6", null, null}, pe.getLevels());
    }

    /*
     * Przykladowy scenariusz:
     * hibernate wola settery level1, ..., level8 w dowolnej kolejnosci podczas wczytywania rekordu z bazy;
     * dodatkowo - ostatni level jest zlozony
     */
    @Test
    public void testSetLevel__scenario2() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry();

        // scenariusz
        pe.setLevel1("1");
        pe.setLevel2(null);
        pe.setLevel3("3");
        pe.setLevel4("4");
        pe.setLevel5(null);
        pe.setLevel6("6");
        pe.setLevel7("7");
        pe.setLevel8("8;9;10");

        // weryfikacja
        assertArrayEquals(new String[]{"1", null, "3", "4", null, "6", "7", "8", "9", "10"}, pe.getLevels());
    }

    /*
     * Przykladowy scenariusz:
     * hibernate wola setter level8 z pojedyncza wartoscia
     */
    @Test
    public void testSetLevel__scenario3() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry();

        // scenariusz
        pe.setLevel8("8");

        // weryfikacja
        assertArrayEquals(new String[]{null, null, null, null, null, null, null, "8"}, pe.getLevels());
    }

    @Test(expected = SmartParamDefinitionException.class)
    public void testSetLevel__illegalArgument() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry();

        // test
        pe.setLevel(-1, "A");
    }

    @Test
    public void testSetLevels() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry();

        // test
        pe.setLevels(null);

        // weryfikacja
        assertArrayEquals(new String[0], pe.getLevels());

        // test
        pe.setLevels(new String[]{"A", "B", "C"});

        // weryfikacja
        assertArrayEquals(new String[]{"A", "B", "C"}, pe.getLevels());
    }

    @Test
    public void testGetLevel__scenario1() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry("1;2;3;4;5;6;7;8", "value");

        // weryfikacja
        assertEquals("1", pe.getLevel1());
        assertEquals("2", pe.getLevel2());
        assertEquals("3", pe.getLevel3());
        assertEquals("4", pe.getLevel4());
        assertEquals("5", pe.getLevel5());
        assertEquals("6", pe.getLevel6());
        assertEquals("7", pe.getLevel7());
        assertEquals("8", pe.getLevel8());
    }

    @Test
    public void testGetLevel__scenario2() {

        // przygotowanie obiektu
        JpaParameterEntry pe = new JpaParameterEntry("1;2;3;4;5;6;7;8;9;10", "value");

        // weryfikacja
        assertEquals("1", pe.getLevel1());
        assertEquals("2", pe.getLevel2());
        assertEquals("3", pe.getLevel3());
        assertEquals("4", pe.getLevel4());
        assertEquals("5", pe.getLevel5());
        assertEquals("6", pe.getLevel6());
        assertEquals("7", pe.getLevel7());
        assertEquals("8;9;10", pe.getLevel8());
    }

    @Test
    public void testId() {
        // konfiguracja testu
        JpaParameterEntry pe = new JpaParameterEntry();
        int expectedValue = 1234567;

        // test
        pe.setId(expectedValue);
        int result = pe.getId();

        // sprawdzenie wynikow testu
        assertEquals(expectedValue, result);
    }

    @Test
    public void testValue() {
        // konfiguracja testu
        JpaParameterEntry pe = new JpaParameterEntry();
        String expectedValue = "value";

        // test
        pe.setValue(expectedValue);
        String result = pe.getValue();

        // sprawdzenie wynikow testu
        assertEquals(expectedValue, result);
    }

    @Test
    public void testFunction() {
        // konfiguracja testu
        JpaParameterEntry pe = new JpaParameterEntry();
        JpaFunction expectedValue = new JpaFunction();

        // test
        pe.setFunction(expectedValue);
        Function result = pe.getFunction();

        // sprawdzenie wynikow testu
        assertSame(expectedValue, result);
    }

    @Test
    public void testParameter() {
        // konfiguracja testu
        JpaParameterEntry pe = new JpaParameterEntry();
        JpaParameter expectedValue = new JpaParameter();

        // test
        pe.setParameter(expectedValue);
        Parameter result = pe.getParameter();

        // sprawdzenie wynikow testu
        assertSame(expectedValue, result);
    }

    @Test
    public void testToString() {

        // zaleznosci
        JpaFunction f = new JpaFunction();
        f.setName("fun1");

        // konfiguracja testu
        JpaParameterEntry[] tests = {
            new JpaParameterEntry("", null, null),
            new JpaParameterEntry("A", "value1", null),
            new JpaParameterEntry("A;B;C;D", "value2", f)
        };

        // oczekiwane wyniki
        String[] expected = {
            "ParameterEntry[#0 [] v=null f=null]",
            "ParameterEntry[#0 [A] v=value1 f=null]",
            "ParameterEntry[#0 [A, B, C, D] v=value2 f=fun1]"
        };

        // test
        for (int i = 0; i < expected.length; i++) {
            JpaParameterEntry pe = tests[i];
            String expectedResult = expected[i];

            String result = pe.toString();
            assertEquals(expectedResult, result);
        }
    }
}
