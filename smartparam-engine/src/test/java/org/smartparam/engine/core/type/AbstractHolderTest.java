package org.smartparam.engine.core.type;

import org.smartparam.engine.core.type.AbstractHolder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.string.StringHolder;

/**
 * @author Przemek Hertel
 */
public class AbstractHolderTest {

    @Test
    public void testCompareTo() {

        // konfiguracja
        AbstractHolder holder1 = new NonComparableHolder();
        AbstractHolder holder2 = new StringHolder("abc");

        // test
        int result = holder1.compareTo(holder2);

        // weryfikacja
        assertEquals(0, result);
    }

    @Test
    public void testEquals() {

        // przypadki testowe
        Object[][] pairs = {
            {new StringHolder("ab"), new StringHolder("ab"), true},
            {new StringHolder("ab"), new StringHolder("ac"), false},
            {new IntegerHolder(1L), new IntegerHolder(1L), true},
            {new IntegerHolder(1L), new StringHolder("1"), false},
            {new StringHolder("ab"), null, false}
        };

        // testy
        for (int i = 0; i < pairs.length; i++) {
            Object[] test = pairs[i];
            AbstractHolder h1 = (AbstractHolder) test[0];
            AbstractHolder h2 = (AbstractHolder) test[1];
            boolean expectedResult = (Boolean) test[2];

            // test 1
            boolean result = h1.equals(h2);
            assertEquals(expectedResult, result);

            // test 2
            if (h2 != null) {
                result = h2.equals(h1);
                assertEquals(expectedResult, result);
            }
        }

    }

    private class NonComparableHolder extends AbstractHolder {

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean isComparable() {
            return false;
        }
    }
}
