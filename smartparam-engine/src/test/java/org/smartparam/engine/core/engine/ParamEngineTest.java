package org.smartparam.engine.core.engine;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.testng.AssertJUnit.*; 
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class ParamEngineTest {

    Function levelCreator;

    @BeforeMethod
    public void init() {
        levelCreator = mock(Function.class);
    }

    @Test
    public void testEvaluateStringAsArray() {

        // values[i] - string wejsciowy
        String[] values = {
            "A, B,  ,D",
            " 1,  2,3 ",
            " "
        };

        // types[i] - typ parametru
        Type<?>[] types = {
            new StringType(),
            new IntegerType(),
            new IntegerType()
        };

        // expectations[i] - tablica wynikowa okreslonego typu
        Object[][] expectations = {
            new StringHolder[]{new StringHolder("A"), new StringHolder("B"), new StringHolder(""), new StringHolder("D")},
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new IntegerHolder[]{}
        };

        // wykonanie testow
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            Type<?> type = types[i];

            AbstractHolder[] expected = (AbstractHolder[]) expectations[i];
            AbstractHolder[] result = new SmartParamEngine().evaluateStringAsArray(value, type, ',');
            checkArrays(expected, result);
        }
    }

    /**
     * rzuca: java.lang.ArithmeticException: / by zero
     */
    public int badMethod(int a) {
        return 1 / (a / a - 1);
    }

    private PreparedParameter pp(PreparedLevel... levels) {
        PreparedParameter pp = new PreparedParameter();
        pp.setLevels(levels);
        return pp;
    }

    private void checkArrays(AbstractHolder[] expected, AbstractHolder[] result) {
        assertEquals(expected.length, result.length);
        assertTrue(expected.getClass() == result.getClass());

        for (int i = 0; i < result.length; i++) {
            AbstractHolder e = expected[i];
            AbstractHolder r = result[i];
            assertEquals(e.getValue(), r.getValue());
        }
    }
}
