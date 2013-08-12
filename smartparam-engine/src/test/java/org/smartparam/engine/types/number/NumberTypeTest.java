package org.smartparam.engine.types.number;

import org.smartparam.engine.types.number.NumberType;
import org.smartparam.engine.types.number.NumberHolder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 

/**
 * @author Przemek Hertel
 */
public class NumberTypeTest {

    private NumberType type = new NumberType();

    @Test
    public void testEncode() {

        // przypadki testowe
        Object[][] testCases = {
            {num("-100"), "-100"},
            {num("-100.00"), "-100.00"},
            {num("0.0"), "0.0"},
            {num(".0"), "0.0"},
            {num(".000"), "0.000"},
            {num("12.3456789"), "12.3456789"},
            {num(null), null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            NumberHolder value = (NumberHolder) testCase[0];
            String expectedResult = (String) testCase[1];

            assertEquals(expectedResult, type.encode(value));
        }
    }

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"-100", num2(new BigDecimal("-100"))},
            {"-100.00", num2(new BigDecimal("-100.00"))},
            {"0.0", num2(new BigDecimal("0.0"))},
            {".0", num2(new BigDecimal("0.0"))},
            {"  .00 ", num2(new BigDecimal("0.00"))},
            {null, new NumberHolder(null)},
            {"  ", new NumberHolder(null)},
            {"", new NumberHolder(null)},
            {"1e3", num2(new BigDecimal("1e3"))},
            {"1.2e2", num2(new BigDecimal("1.2E2"))},
            {"1,23", num2(new BigDecimal("1.23"))},
            {"1 234", num2(new BigDecimal("1234"))}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            NumberHolder expectedHolder = (NumberHolder) testCase[1];

            assertEquals(expectedHolder, type.decode(text));
            assertEquals(expectedHolder.getBigDecimal(), type.decode(text).getBigDecimal());
        }
    }

    @Test
    public void testDecode__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac zdekodowane do NumberHoldera
        String[] illegals = {"aa", "1b"};

        // wykonanie testow, oczekujemy wyjatku
        for (String text : illegals) {
            try {
                type.decode(text);
                fail();
            } catch (NumberFormatException nfe) {
                //ok
            }
        }
    }

    @Test
    public void testConvert() {

        // przypadki testowe: [argument (Object)][oczekiwana wartosc holdera (BigDecimal)]
        Object[][] testCases = {
            {new Long(17), new BigDecimal("17")},
            {new Integer(50), new BigDecimal("50")},
            {new Double(0.12345), new BigDecimal(0.12345)},
            {new Float(0.12345), new BigDecimal(0.12345f)},
            {(byte) 100, new BigDecimal(100)},
            {(short) 2000, new BigDecimal(2000)},
            {(float) 123, new BigDecimal(123f)},
            {null, null},
            {"", null},
            {" ", null},
            {"123,45", new BigDecimal("123.45")},
            {new BigInteger("1234"), new BigDecimal(1234)},
            {new BigDecimal("00.12"), new BigDecimal("00.12")},
            {String.valueOf(Long.MIN_VALUE), new BigDecimal(Long.MIN_VALUE)},
            {String.valueOf(Long.MAX_VALUE), new BigDecimal(Long.MAX_VALUE)}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            BigDecimal expectedValue = (BigDecimal) testCase[1];

            assertEquals(expectedValue, type.convert(obj).getBigDecimal());
            assertEquals(expectedValue, type.convert(obj).getValue());
        }
    }

    @Test
    public void testConvert__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac skonwertowane do NumberHoldera
        Object[] illegals = {new Date(), new int[0], "abc"};

        // wykonanie testow, oczekujemy wyjatku
        for (Object obj : illegals) {
            try {
                type.convert(obj);
                fail();
            } catch (RuntimeException e) {
                assertTrue(e instanceof IllegalArgumentException || e instanceof NumberFormatException);
            }
        }
    }

    @Test
    public void testNewArray() {
        NumberHolder[] arr1 = type.newArray(3);
        NumberHolder[] arr2 = type.newArray(3);

        assertNotNull(arr1);
        assertNotNull(arr2);
        assertNotSame(arr1, arr2);
    }

    private NumberHolder num(String str) {
        return new NumberHolder(str != null ? new BigDecimal(str) : null);
    }

    private NumberHolder num2(BigDecimal n) {
        return new NumberHolder(n);
    }
}
