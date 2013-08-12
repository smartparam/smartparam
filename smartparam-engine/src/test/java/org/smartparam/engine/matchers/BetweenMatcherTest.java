package org.smartparam.engine.matchers;

import org.smartparam.engine.matchers.BetweenMatcher;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class BetweenMatcherTest {

    @Test
    public void testMatches() {

        // zaleznosci
        IntegerType intType = new IntegerType();
        StringType strType = new StringType();

        Matcher m1ii = new BetweenMatcher(true, true, ":-,");       // separat: ":-,"
        Matcher m2ie = new BetweenMatcher(true, false, "");         // default: ":"
        Matcher m3ee = new BetweenMatcher(false, false, null);      // default: ":"

        // przypadki testowe
        // [pattern] [value] [matcher] [type] [expectedResult]
        Object[][] tests = {
            {"1200 : 1300", "1200", m1ii, intType, true},
            {"1200 : 1300", "1250", m1ii, intType, true},
            {"1200 : 1300", "1300", m1ii, intType, true},
            {"1200 : 1300", "1199", m1ii, intType, false},
            {"1200 : 1300", "1301", m1ii, intType, false},
            //
            {"1200 : 1300", "1200", m2ie, intType, true},
            {"1200 : 1300", "1250", m2ie, intType, true},
            {"1200 : 1300", "1300", m2ie, intType, false},
            {"1200 : 1300", "1199", m2ie, intType, false},
            {"1200 : 1300", "1301", m2ie, intType, false},
            //
            {"1200 : 1300", "1200", m3ee, intType, false},
            {"1200 : 1300", "1250", m3ee, intType, true},
            {"1200 : 1300", "1300", m3ee, intType, false},
            {"1200 : 1300", "1199", m3ee, intType, false},
            {"1200 : 1300", "1301", m3ee, intType, false},
            //
            {"1200 : * ", "1200", m1ii, intType, true},
            {"1200 :*  ", "1199", m1ii, intType, false},
            {"* :1300 ", "1250", m1ii, intType, true},
            {"*: 1300 ", "1999", m1ii, intType, false},
            {" 1200 : ", "1300", m1ii, intType, true},
            {" 1200 : ", "1100", m1ii, intType, false},
            {" : 1300 ", "1250", m1ii, intType, true},
            {" : 1300 ", "1999", m1ii, intType, false},
            {"  1200  ", "1300", m1ii, intType, true},
            {"  1200  ", "1100", m1ii, intType, false},
            //
            {"1200 : * ", "1200", m1ii, strType, true},
            {"1200 :*  ", "1199", m1ii, strType, false},
            {"* :1300 ", "1250", m1ii, strType, true},
            {"*: 1300 ", "1999", m1ii, strType, false},
            {" 1200 : ", "1300", m1ii, strType, true},
            {" 1200 : ", "1100", m1ii, strType, false},
            {" : 1300 ", "1250", m1ii, strType, true},
            {" : 1300 ", "1999", m1ii, strType, false},
            {"  1200  ", "1300", m1ii, strType, true},
            {"  1200  ", "1100", m1ii, strType, false},
            //
            {"aa:abaaa", "a", m1ii, strType, false},
            {"aa:abaaa", "aa", m1ii, strType, true},
            {"aa:abaaa", "aaa", m1ii, strType, true},
            {"aa:abaaa", "aaaa", m1ii, strType, true},
            {"aa:abaaa", "abaaa", m1ii, strType, true},
            {"aa:abaaa", "abaab", m1ii, strType, false},
            //
            {"1200 - 1300", "1200", m1ii, intType, true},
            {"1200 - 1300", "1250", m1ii, intType, true},
            {"1200 , 1300", "1300", m1ii, intType, true},
            {"1200- 1300 ", "1199", m1ii, intType, false},
            {" 1200:1300 ", "1301", m1ii, intType, false}
        };


        // testy i weryfikacja
        for (Object[] test : tests) {
            String pattern = (String) test[0];
            String value = (String) test[1];
            Matcher matcher = (Matcher) test[2];
            Type<?> type = (Type<?>) test[3];
            boolean expectedResult = (Boolean) test[4];

            // test
            boolean result = matcher.matches(value, pattern, type);

            // weryfikacja
            assertEquals(expectedResult, result);
        }
    }
}
