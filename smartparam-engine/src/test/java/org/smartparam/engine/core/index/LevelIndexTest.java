package org.smartparam.engine.core.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.matchers.EqMatcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.string.StringType;
import org.smartparam.engine.util.EngineUtil;
import org.smartparam.engine.util.Formatter;
import static org.smartparam.engine.test.builder.LevelIndexTestBuilder.levelIndex;

/**
 * Test indeksu budowanego dla macierzy parametru.
 *
 * @author Przemek Hertel
 */
public class LevelIndexTest {

    /**
     * Test konstruktora
     * {@link LevelIndex#LevelIndex(int, org.smartparam.engine.core.type.AbstractType<?>[], org.smartparam.engine.core.index.Matcher[])}.
     */
    @Test
    public void testConstructor1() {

        // konfiguracja zaleznosci
        StringType t1 = new StringType();
        IntegerType t2 = new IntegerType();
        Matcher m1 = new EqMatcher();
        Matcher m2 = new BetweenMatcher();

        Type<?>[] types = {t1, t2};
        Matcher[] matchers = {m1, m2};

        // utworzenie testowanego obiektu
        LevelIndex<Integer> index = new LevelIndex<Integer>(2, types, matchers);

        // sprawdzenie wynikow testu
        assertEquals(2, index.getLevelCount());
        assertNotSame(index.getTypes(), types);
        assertArrayEquals(types, index.getTypes());
        assertArrayEquals(matchers, index.getMatchers());
        for (int i = 0; i < 2; i++) {
            assertEquals(types[i], index.getType(i));
            assertEquals(matchers[i], index.getMatcher(i));
        }
    }

    @Test
    public void testConstructor1__nullMatchers() {

        // konfiguracja zaleznosci
        StringType t1 = new StringType();
        IntegerType t2 = new IntegerType();

        Type<?>[] types = {t1, t2};

        // utworzenie testowanego obiektu
        LevelIndex<Integer> index = new LevelIndex<Integer>(2, types, (Matcher[]) null);

        // sprawdzenie wynikow testu
        assertEquals(2, index.getLevelCount());
        assertNotSame(index.getTypes(), types);
        assertArrayEquals(types, index.getTypes());
        assertArrayEquals(new Matcher[2], index.getMatchers());
    }

    /**
     * Test konstruktora
     * {@link LevelIndex#LevelIndex(int, org.smartparam.engine.core.type.AbstractType<?>[], org.smartparam.engine.core.index.Matcher[])}.
     *
     * Tablice typow i matcherow maja elementy nie dla wszystkich poziomow.
     */
    @Test
    public void testConstructor1__subarrays() {

        // konfiguracja zaleznosci
        StringType t1 = new StringType();
        IntegerType t2 = new IntegerType();
        Matcher m1 = new EqMatcher();

        Type<?>[] types = {t1, t2};

        // utworzenie testowanego obiektu
        // 3 poziomy, 2 typy, 1 matcher
        LevelIndex<Integer> index = new LevelIndex<Integer>(3, types, m1);

        // sprawdzenie wynikow testu
        assertEquals(3, index.getLevelCount());                     // parametr ma 3 poziomy

        assertEquals(types[0], index.getType(0));                   // tylko 2 pierwsze poziomy maja typ
        assertEquals(types[1], index.getType(1));
        assertNull(index.getType(2));                               // trzeci poziom nie ma typu

        assertEquals(m1, index.getMatcher(0));                      // tylko 1 pierwszy poziom ma matcher
        assertNull(index.getMatcher(1));                            // pozostale poziomy maja domyslny matcher
        assertNull(index.getMatcher(2));
    }

    /**
     * Test konstruktora {@link LevelIndex#LevelIndex(int)}.
     */
    @Test
    public void testConstructor2() {

        // utworzenie testowanego obiektu
        LevelIndex<String> index = levelIndex().withLevelCount(2).build();     // 2 poziomy, brak typow, domyslne matchery

        // sprawdzenie wynikow testu
        assertEquals(2, index.getLevelCount());
        assertArrayEquals(new Type<?>[]{null, null}, index.getTypes());
        assertArrayEquals(new Matcher[]{null, null}, index.getMatchers());
    }

    /**
     * Podstawowy test metody find
     */
    @Test
    public void testFind() {

        // oczekiwane wyniki wyszukiwania
        Map<String, Integer> cases = new HashMap<String, Integer>();
        cases.put("A;X", 7);
        cases.put("A;Y", 8);
        cases.put("A;AA", 9);
        cases.put("B;X", 11);
        cases.put("B;B", 20);
        cases.put("C;X", 30);
        cases.put("X;X", 30);
        cases.put("Z;Z", 99);

        // utworzenie testowanego obiektu
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);

        // wypelnienie testowanego obiektu
        index.add("A;X", 7);
        index.add("A;Y", 8);
        index.add("A;*", 9);
        index.add("B;X", 11);
        index.add("B;*", 20);
        index.add("*;X", 30);
        index.add("*;*", 99);

        // sprawdzenie wynikow testu
        for (Map.Entry<String, Integer> e : cases.entrySet()) {
            String[] levelValues = EngineUtil.split(e.getKey(), ';');
            Integer expectedResult = e.getValue();

            assertEquals(expectedResult, index.find(levelValues));
        }
    }

    /**
     * Test metody find dla skrajnego przypadku zastosowania wartosci domyslnych.
     * Wartosci domyslne wystepuja na kazdym z poziomow.
     */
    @Test
    public void testFind__multipleDefaults() {

        // utworzenie testowanego obiektu
        LevelIndex<String> ix = new LevelIndex<String>(3);

        // wypelnienie testowanego obiektu
        ix.add("A;B;C", "ABC");
        ix.add("A;B;A", "ABA");
        ix.add("A;*;*", "A**");
        ix.add("*;*;*", "***");
        ix.add("*;*;E", "**E");
        ix.add("*;E;*", "*E*");

        // oczekiwane wyniki wyszukiwania
        String[][] cases = {
            {"A", "B", "C", "ABC"}, // dla poziomow A,B,C wynikiem jest string ABC
            {"A", "B", "A", "ABA"},
            {"A", "D", "A", "A**"},
            {"B", "B", "A", "***"}, // dla B,B,A wynikiem jest string ***
            {"B", "B", "E", "**E"},
            {"B", "E", "E", "*E*"},
            {"A", "E", "E", "A**"}
        };

        // sprawdzenie wynikow testu
        for (String[] row : cases) {
            String expectedResult = row[3];
            String result = ix.find(row[0], row[1], row[2]);

            assertEquals(expectedResult, result);
        }
    }

    /**
     * Test metody find dla parametru, ktory wykorzystuje niedomyslny Matcher.
     */
    @Test
    public void testFind__customMatcher() {

        // utworzenie zaleznosci
        StringType stringType = new StringType();
        Matcher m1 = new EqMatcher(true);           // case sensitive
        Matcher m2 = new EqMatcher(false);          // ignore case

        // utworzenie testowanego obiektu
        LevelIndex<Integer> ix = new LevelIndex<Integer>(2, new StringType[]{stringType, stringType}, m1, m2);
        ix.add("A;X", 7);
        ix.add("A;Y", 8);
        ix.add("A;*", 9);
        ix.add("b;y", 11);
        ix.add("b;*", 20);
        ix.add("*;z", 30);

        // oczekiwane wyniki wyszukiwania
        Object[][] cases = {
            {"A", "X", 7}, // dla poziomow A,X wynikiem jest integer 7
            {"A", "Y", 8},
            {"A", "C", 9},
            {"B", "Y", null}, // m1 jest case sensitive, wiec B;Y != b;y
            {"b", "y", 11},
            {"b", "Y", 11}, // m2 jest ignore case, wiec b;Y == b;y
            {"b", "a", 20}, // b;a == b;*

            {"C", "z", 30}, // C;z == *;z
            {"C", "Z", 30}, // C;Z == *;z
            {"D", "D", null}
        };

        // sprawdzenie wynikow testu
        for (Object[] row : cases) {
            Integer expectedResult = (Integer) row[2];
            Integer result = ix.find((String) row[0], (String) row[1]);

            assertEquals(expectedResult, result);
        }
    }

    /**
     * Test metody find dla parametru, ktory wykorzystuje 2 rozne niedomyslne Matchery.
     */
    @Test
    public void testFind__customMatcher2() {

        // utworzenie zaleznosci
        Matcher m1 = new EqMatcher();
        Matcher m2 = new BetweenMatcher();

        StringType t1 = new StringType();
        IntegerType t2 = new IntegerType();

        // utworzenie testowanego obiektu
        LevelIndex<Integer> ix = new LevelIndex<Integer>(2, new Type<?>[]{t1, t2}, m1, m2);

        ix.add("A;1:5", 15);
        ix.add("A;5:12", 512);
        ix.add("A;12:20", 1220);
        ix.add("B;1:50", 150);
        ix.add("B;*", 99);
        ix.add("*;1:9", 19);
        ix.add("*;*", 999);

        // sprawdzenie wynikow testu
        assertEquals(15, (int) ix.find("A", "1"));
        assertEquals(15, (int) ix.find("A", "4"));
        assertEquals(512, (int) ix.find("A", "5"));
        assertEquals(1220, (int) ix.find("A", "12"));
    }

    @Test
    public void testPrintTree() {

        // utworzenie testowanego obiektu
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);
        index.add("A;X", 11);
        index.add("B;X", 22);
        index.add("B;*", 33);

        // oczekiwany wynik
        String expectedPrefix = ""
                + "path : " + Formatter.NL;
        String expectedForA = ""
                + "    path : /A" + Formatter.NL
                + "        path : /A/X   (leaf=[11])" + Formatter.NL;
        String expectedForB = ""
                + "    path : /B" + Formatter.NL
                + "        path : /B/X   (leaf=[22])" + Formatter.NL
                + "        path : /B/*   (leaf=[33])" + Formatter.NL;

        // test
        String result = index.printTree();

        // weryfikacja
        assertTrue(result.startsWith(expectedPrefix));
        assertTrue(result.contains(expectedForA));
        assertTrue(result.contains(expectedForB));
    }

    @Test
    public void testFindAll() {

        // utworzenie testowanego obiektu
        LevelIndex<Integer> index = new LevelIndex<Integer>(1);
        index.add("A", 1);
        index.add("A", 2);
        index.add("A", 3);
        index.add("B", 11);
        index.add("B", 12);
        index.add("C", 99);

        // szukane wartosci
        String[] cases = {
            "A",
            "B",
            "C",
            "D"
        };

        // oczekiwane wyniki
        List<?>[] expected = {
            Arrays.asList(1, 2, 3),
            Arrays.asList(11, 12),
            Arrays.asList(99),
            null
        };

        // testy
        for (int i = 0; i < cases.length; ++i) {
            String level = cases[i];
            List<?> expectedResult = expected[i];

            List<Integer> result = index.findAll(level);
            assertEquals(expectedResult, result);
        }
    }
}
