/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.util;

import org.smartparam.engine.util.EngineUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 

/**
 * @author Przemek Hertel
 */
public class EngineUtilTest {

    /**
     * Test ogolnej, szybkiej metody splitowania bez ograniczania liczby tokenow.
     */
    @Test
    public void testSplit() {

        // konfiguracja testu (key = string, value = oczekiwany wynik splitowania)
        Map<String, String[]> casesMap = new LinkedHashMap<String, String[]>();
        casesMap.put("A,B,C",   new String[]{"A", "B", "C"});
        casesMap.put(",A,B",    new String[]{"", "A", "B"});
        casesMap.put(",A,B,",   new String[]{"", "A", "B", ""});
        casesMap.put(",,",      new String[]{"", "", ""});
        casesMap.put(" ,, ",    new String[]{" ", "", " "});
        casesMap.put("A,,B,,C,",new String[]{"A", "", "B", "", "C", ""});
        casesMap.put("AA,BB",   new String[]{"AA", "BB"});
        casesMap.put(",AA,BB",  new String[]{"", "AA", "BB"});
        casesMap.put(",AA,BB,", new String[]{"", "AA", "BB", ""});
        casesMap.put("AA,,BB",  new String[]{"AA", "", "BB"});

        // sprawdzenie wynikow testu
        for (Map.Entry<String, String[]> e : casesMap.entrySet()) {
            String str = e.getKey();
            String[] expected = e.getValue();
            String[] result = EngineUtil.split(str, ',');

            assertArrayEquals(expected, result);
        }
    }

    /**
     * Test metody split z ograniczeniem na [max] tokenow.
     */
    @Test
    public void testSplit__max() {

        // konfiguracja testu (key = string, value = oczekiwany wynik splitowania)
        Map<String, String[]> casesMap = new LinkedHashMap<String, String[]>();
        casesMap.put("A,B,C",   new String[]{"A", "B"});
        casesMap.put(",A,B",    new String[]{"", "A"});
        casesMap.put(",A,B,",   new String[]{"", "A"});
        casesMap.put(",,",      new String[]{"", ""});
        casesMap.put("A,B",     new String[]{"A", "B"});
        casesMap.put("A,",      new String[]{"A", ""});
        casesMap.put(",",       new String[]{"", ""});

        // sprawdzenie wynikow testu
        for (Map.Entry<String, String[]> e : casesMap.entrySet()) {
            String str = e.getKey();
            String[] expected = e.getValue();
            String[] result = EngineUtil.split(str, ',', 2);

            assertArrayEquals(expected, result);
        }
    }

    /**
     * Test dedykowanej metody splitujacej dokladnie 2 tokeny.
     */
    @Test
    public void testSplit2() {

        // konfiguracja testu (key = string, value = oczekiwany wynik splitowania)
        Map<String, String[]> casesMap = new LinkedHashMap<String, String[]>();
        casesMap.put("A;B", new String[]{"A", "B"});
        casesMap.put("AA;BB", new String[]{"AA", "BB"});
        casesMap.put(";B", new String[]{"", "B"});
        casesMap.put(";BB", new String[]{"", "BB"});
        casesMap.put("A;", new String[]{"A", ""});
        casesMap.put("AA;", new String[]{"AA", ""});
        casesMap.put(";", new String[]{"", ""});
        casesMap.put("", new String[]{"", ""});

        // sprawdzenie wynikow testu
        for (Map.Entry<String, String[]> e : casesMap.entrySet()) {
            String str = e.getKey();
            String[] expected = e.getValue();
            String[] result = EngineUtil.split2(str, ';');
            
            assertEquals(2, result.length);
            assertEquals(expected[0], result[0]);
            assertEquals(expected[1], result[1]);
        }

        // przypadek szczegolny, argument rowny null
        String[] tokens = instance.split2(null, ';');
        assertEquals("", tokens[0]);
        assertEquals("", tokens[1]);
    }

    private static class NonAbstractEngineUtil extends EngineUtil {
    }
    EngineUtil instance = new NonAbstractEngineUtil();
}
