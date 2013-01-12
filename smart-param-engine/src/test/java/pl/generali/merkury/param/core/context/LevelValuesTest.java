package pl.generali.merkury.param.core.context;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Przemek Hertel
 */
public class LevelValuesTest {

    @Test
    public void testConstructor() {

        // przypadki testowe
        // format: 1 kolumna - podane obiekty jako levelValues, 2 kolumna - levelValues zapisane w kontekscie
        Object[][] tests = {
            {"A", "B"}, new String[]{"A", "B"},
            {"A", 7}, new String[]{"A", "7"},
            {"A", 7, "C"}, new String[]{"A", "7", "C"},
            {1, null}, new String[]{"1", null},
            {'a', new BigDecimal("1.2")}, new String[]{"a", "1.2"}
        };

        // wykonanie testow
        for (int i = 0; i < tests.length; i+=2) {
            Object[] givenLevelValues = tests[i];
            String[] expectedLevelValues = (String[]) tests[i+1];

            // stworzenie kontekstu
            ParamContext ctx = new LevelValues(givenLevelValues);

            // weryfikacja tablicy levelValues
            assertArrayEquals(expectedLevelValues, ctx.getLevelValues());
        }
    }
}
