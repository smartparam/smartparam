package org.smartparam.provider.hibernate.model;

import org.smartparam.provider.hibernate.model.HibernateParameter;
import org.smartparam.provider.hibernate.model.HibernateParameterEntry;
import org.smartparam.provider.hibernate.model.HibernateFunction;
import org.junit.Test;
import org.smartparam.engine.core.exception.ParamDefinitionException;
import org.smartparam.engine.model.Function;
import org.smartparam.engine.model.Parameter;

import static org.junit.Assert.*;

/**
 * @author Przemek Hertel
 */
public class HibernateParameterEntryTest {

    @Test
    public void testConstructors() {

        // obiekty pomocnicze
        HibernateFunction f1 = new HibernateFunction();
        HibernateFunction f2 = new HibernateFunction();

        // obiekty stworzone przez rozne konstruktory
        HibernateParameterEntry[] tests = {
            new HibernateParameterEntry(),
            new HibernateParameterEntry(new String[]{}, "value", null),
            new HibernateParameterEntry(new String[]{"1", "2"}, "value", f1),
            new HibernateParameterEntry(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}, null, f2),
            new HibernateParameterEntry("1", "2", "3"),
            new HibernateParameterEntry("1", "2", "3", "4", "5", "6", "7", "8"),
            new HibernateParameterEntry("1", "2", "3", "4", "5", "6", "7", "8", "9"),
            new HibernateParameterEntry("1;2;3", f1),
            new HibernateParameterEntry("1;2;3;4;5;6;7;8;9;10;11", f1),
            new HibernateParameterEntry("", "val"),
            new HibernateParameterEntry("1;2;3;4", "val"),
            new HibernateParameterEntry(new String[]{}, "v"),
            new HibernateParameterEntry(new String[]{"A"}, "v"),
            new HibernateParameterEntry("a;b;;;e;f;g;h;i", null, f1),
            new HibernateParameterEntry(new String[]{"X", "Y"}, "V", f1)
        };

        // oczekiwana zawartosc obiektow HibernateParameterEntry:
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
            HibernateParameterEntry pe = tests[i];
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
    public void testGetLevels() {

        // przygotowanie obiektu
        HibernateParameterEntry pe = new HibernateParameterEntry("1;2;3;4;5;6;7;8;9;10;11;12;13;14", "value");

        // przypadki testowe
        Object[][] tests = {
            new Object[]{0, new String[]{}},
            new Object[]{1, new String[]{"1"}},
            new Object[]{4, new String[]{"1", "2", "3", "4"}},
            new Object[]{8, new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}},
            new Object[]{11, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}},
            new Object[]{14, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"}},
            new Object[]{15, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", null}}
        };

        // wykonanie testow
        for (Object[] test : tests) {
            Integer n = (Integer) test[0];
            String[] expectedLevels = (String[]) test[1];

            assertArrayEquals(expectedLevels, pe.getLevels(n));
        }
    }

    @Test
    public void testGetLevel() {

        // przygotowanie obiektu
        HibernateParameterEntry pe = new HibernateParameterEntry("1;2;3;4;5;6;7;8;9", "value");

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
        HibernateParameterEntry pe = new HibernateParameterEntry();

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
        HibernateParameterEntry pe = new HibernateParameterEntry();

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
        HibernateParameterEntry pe = new HibernateParameterEntry();

        // scenariusz
        pe.setLevel8("8");

        // weryfikacja
        assertArrayEquals(new String[]{null, null, null, null, null, null, null, "8"}, pe.getLevels());
    }

    @Test(expected = ParamDefinitionException.class)
    public void testSetLevel__illegalArgument() {

        // przygotowanie obiektu
        HibernateParameterEntry pe = new HibernateParameterEntry();

        // test
        pe.setLevel(-1, "A");
    }

    @Test
    public void testSetLevels() {

        // przygotowanie obiektu
        HibernateParameterEntry pe = new HibernateParameterEntry();

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
        HibernateParameterEntry pe = new HibernateParameterEntry("1;2;3;4;5;6;7;8", "value");

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
        HibernateParameterEntry pe = new HibernateParameterEntry("1;2;3;4;5;6;7;8;9;10", "value");

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
        HibernateParameterEntry pe = new HibernateParameterEntry();
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
        HibernateParameterEntry pe = new HibernateParameterEntry();
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
        HibernateParameterEntry pe = new HibernateParameterEntry();
        HibernateFunction expectedValue = new HibernateFunction();

        // test
        pe.setFunction(expectedValue);
        Function result = pe.getFunction();

        // sprawdzenie wynikow testu
        assertSame(expectedValue, result);
    }

    @Test
    public void testParameter() {
        // konfiguracja testu
        HibernateParameterEntry pe = new HibernateParameterEntry();
        HibernateParameter expectedValue = new HibernateParameter();

        // test
        pe.setParameter(expectedValue);
        Parameter result = pe.getParameter();

        // sprawdzenie wynikow testu
        assertSame(expectedValue, result);
    }

    @Test
    public void testToString() {

        // zaleznosci
        HibernateFunction f = new HibernateFunction();
        f.setName("fun1");

        // konfiguracja testu
        HibernateParameterEntry[] tests = {
            new HibernateParameterEntry("", null, null),
            new HibernateParameterEntry("A", "value1", null),
            new HibernateParameterEntry("A;B;C;D", "value2", f)
        };

        // oczekiwane wyniki
        String[] expected = {
            "ParameterEntry[#0 [] v=null f=null]",
            "ParameterEntry[#0 [A] v=value1 f=null]",
            "ParameterEntry[#0 [A, B, C, D] v=value2 f=fun1]"
        };

        // test
        for (int i = 0; i < expected.length; i++) {
            HibernateParameterEntry pe = tests[i];
            String expectedResult = expected[i];

            String result = pe.toString();
            assertEquals(expectedResult, result);
        }
    }
}
