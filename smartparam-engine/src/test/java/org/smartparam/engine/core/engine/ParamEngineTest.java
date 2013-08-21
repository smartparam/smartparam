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

//    private PreparedParameter pp(PreparedLevel... levels) {
//        PreparedParameter pp = new PreparedParameter(new SimpleParameter());
//        pp.setLevels(levels);
//        return pp;
//    }

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
