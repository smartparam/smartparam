package pl.generali.merkury.param.types.string;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Przemek Hertel
 */
public class StringTypeTest {

    private StringType type = new StringType();

    @Test
    public void testEncode() {

        // przypadki testowe
        Object[][] testCases = {
            {new StringHolder("ABC"), "ABC"},
            {new StringHolder(""), ""},
            {new StringHolder(null), null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            StringHolder holder = (StringHolder) testCase[0];
            String expectedResult = (String) testCase[1];

            String result = type.encode(holder);
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"abc", new StringHolder("abc")},
            {"", new StringHolder("")},
            {null, new StringHolder(null)}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            StringHolder expectedHolder = (StringHolder) testCase[1];

            StringHolder holder = type.decode(text);
            assertEquals(expectedHolder.getValue(), holder.getValue());
        }
    }

    @Test
    public void testConvert() {

        // przypadki testowe: [argument][oczekiwana wartosc holdera]
        Object[][] testCases = {
            {new Integer(17), "17"},
            {new Float(1. / 3), "0.33333334"},
            {(byte) 100, "100"},
            {null, null},
            {"123", "123"},};

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            String expectedValue = (String) testCase[1];

            StringHolder holder = type.convert(obj);
            assertEquals(expectedValue, holder.getValue());
        }
    }
}
