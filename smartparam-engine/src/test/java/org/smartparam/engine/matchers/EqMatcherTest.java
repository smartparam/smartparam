package org.smartparam.engine.matchers;

import org.smartparam.engine.matchers.EqMatcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class EqMatcherTest {

    @Test
    public void testMatches__caseSensitive() {

        // obiekty pomocnicze
        StringType strType = new StringType();
        IntegerType intType = new IntegerType();

        // utworzenie testowanego obiektu
        Matcher matcher = new EqMatcher();

        // konfiguracja testu (key = string, value = oczekiwany wynik matchowania)
        String[] pattern = {"ABC", "ABC", "",   ""};        // wzorzec wartosci levelu
        String[] value   = {"ABC", "abc", "",   null};      // wartosc dla danego levelu
        boolean[] result = {true,  false, true, false};     // oczekiwany wynik matchowania

        // sprawdzenie wynikow testu
        for(int i=0; i<pattern.length; ++i) {
            assertEquals(result[i], matcher.matches(value[i], pattern[i], strType));
            assertEquals(result[i], matcher.matches(value[i], pattern[i], intType));
        }
    }

    @Test
    public void testMatches__ignoreCase() {

        // obiekty pomocnicze
        StringType strType = new StringType();
        IntegerType intType = new IntegerType();

        // utworzenie testowanego obiektu
        Matcher matcher = new EqMatcher(false);

        // konfiguracja testu (key = string, value = oczekiwany wynik matchowania)
        String[] pattern = {"ABC", "ABC", "Aa", "",   ""};        // wzorzec wartosci levelu
        String[] value   = {"ABC", "abc", "aA", "",   null};      // wartosc dla danego levelu
        boolean[] result = {true,  true,  true, true, false};     // oczekiwany wynik matchowania

        // sprawdzenie wynikow testu
        for(int i=0; i<pattern.length; ++i) {
            assertEquals(result[i], matcher.matches(value[i], pattern[i], strType));
            assertEquals(result[i], matcher.matches(value[i], pattern[i], intType));
        }
    }

}
