package pl.generali.merkury.param.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.*;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.type.AbstractHolder;
import pl.generali.merkury.param.core.type.AbstractType;
import pl.generali.merkury.param.types.integer.IntegerHolder;
import pl.generali.merkury.param.types.integer.IntegerType;

/**
 * @author Przemek Hertel
 */
public class ParamHelperTest {

    private AbstractType<?> type = new IntegerType();

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"0", new IntegerHolder(0L)},
            {" -99", new IntegerHolder(-99L)},
            {null, new IntegerHolder(null)},
            {"\t \n", new IntegerHolder(null)}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            AbstractHolder expectedHolder = (AbstractHolder) testCase[1];

            AbstractHolder holder = ParamHelper.decode(type, text);
            assertEquals(expectedHolder.getValue(), holder.getValue());
        }
    }

    @Test
    public void testDecode__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac zdekodowane do IntegerHoldera
        String[] illegals = {"1.1", "9aa", "."};

        // wykonanie testow, oczekujemy wyjatku
        for (String text : illegals) {
            try {
                ParamHelper.decode(type, text);
                fail();
            } catch (ParamException e) {
                assertEquals(ParamException.ErrorCode.TYPE_DECODING_FAILURE, e.getErrorCode());
            }
        }
    }

    @Test
    public void testConvert() {
        // przypadki testowe: [argument][oczekiwana wartosc holdera]
        Object[][] testCases = {
            {new Long(17), 17L},
            {new Integer(50), 50L},
            {null, null},
            {"", null},
            {" ", null},
            {"123", 123L}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            Long expectedValue = (Long) testCase[1];

            AbstractHolder holder = ParamHelper.convert(type, obj);
            assertEquals(expectedValue, holder.getValue());
        }
    }

    @Test
    public void testConvert__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac skonwertowane do IntegerHoldera
        Object[] illegals = {"1.1", "9aa", 0.1d, 0.1f, BigDecimal.ZERO};

        // wykonanie testow, oczekujemy wyjatku
        for (Object obj : illegals) {
            try {
                ParamHelper.convert(type, obj);
                fail();
            } catch (ParamException e) {
                assertEquals(ParamException.ErrorCode.TYPE_CONVERSION_FAILURE, e.getErrorCode());
            }
        }
    }

    @Test
    public void testConvert__array() {

        // dane wejsciowe
        Object[][] arrays = {
            {1L, 2L, 3L},
            {}
        };

        // oczekiwane rezultaty
        Object[][] expectations = {
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new IntegerHolder[]{}
        };

        // wykonanie testow
        for (int i = 0; i < arrays.length; i++) {
            Object[] array = arrays[i];
            IntegerHolder[] expected = (IntegerHolder[]) expectations[i];

            AbstractHolder[] result = ParamHelper.convert(type, array);
            checkArrays(expected, result);
        }
    }

    @Test
    public void testConvert__collection() {

        // dane wejsciowe
        Object[] arrays = {
            Arrays.asList(1L, 2L, 3L),
            new ArrayList<Long>()
        };

        // oczekiwane rezultaty
        Object[][] expectations = {
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new IntegerHolder[]{}
        };

        // wykonanie testow
        for (int i = 0; i < arrays.length; i++) {
            Collection<?> coll = (Collection<?>) arrays[i];
            IntegerHolder[] expected = (IntegerHolder[]) expectations[i];

            AbstractHolder[] result = new NonAbstractParamHelper().convert(type, coll);
            checkArrays(expected, result);
        }
    }

    private void checkArrays(IntegerHolder[] expected, AbstractHolder[] result) {
        assertTrue(result instanceof IntegerHolder[]);
        assertArrayEquals(expected, result);
    }

    private class NonAbstractParamHelper extends ParamHelper {
    }
}
