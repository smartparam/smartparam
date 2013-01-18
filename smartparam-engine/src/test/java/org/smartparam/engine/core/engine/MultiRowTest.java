package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.engine.MultiValue;
import org.smartparam.engine.core.engine.MultiRow;
import java.math.BigDecimal;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.number.NumberHolder;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.util.Formatter;

/**
 * @author Przemek Hertel
 */
public class MultiRowTest {

    @Test
    public void testGetRow() {

        // zaleznosci
        AbstractHolder h11 = new StringHolder("11");
        AbstractHolder h12 = new StringHolder("12");
        AbstractHolder h21 = new StringHolder("21");
        AbstractHolder h22 = new StringHolder("22");
        AbstractHolder h31 = new StringHolder("31");
        AbstractHolder h32 = new StringHolder("32");

        // przygotowanie obiektu
        MultiRow mr = new MultiRow(3);
        mr.setRow(0, new MultiValue(new Object[]{h11, h12}));
        mr.setRow(1, new MultiValue(new Object[]{h21, h22}));
        mr.setRow(2, new MultiValue(new Object[]{h31, h32}));

        // test
        verifyMultiValue(mr.getRow(1), "11", "12");
        verifyMultiValue(mr.getRow(2), "21", "22");
        verifyMultiValue(mr.getRow(3), "31", "32");

    }

    @Test
    public void testGetRows() {
        // zaleznosci
        AbstractHolder h11 = new StringHolder("11");
        AbstractHolder h12 = new StringHolder("12");
        AbstractHolder h21 = new StringHolder("21");
        AbstractHolder h22 = new StringHolder("22");

        // przygotowanie obiektu
        MultiRow mr = new MultiRow(2);
        mr.setRow(0, new MultiValue(new Object[]{h11, h12}));
        mr.setRow(1, new MultiValue(new Object[]{h21, h22}));

        // test
        MultiValue[] rows = mr.getRows();

        // weryfikacja
        assertEquals(2, rows.length);
        verifyMultiValue(rows[0], "11", "12");
        verifyMultiValue(rows[1], "21", "22");
    }

    @Test
    public void testGetRowsAsList() {
        // zaleznosci
        AbstractHolder h11 = new StringHolder("11");
        AbstractHolder h12 = new StringHolder("12");
        AbstractHolder h21 = new StringHolder("21");
        AbstractHolder h22 = new StringHolder("22");

        // przygotowanie obiektu
        MultiRow mr = new MultiRow(2);
        mr.setRow(0, new MultiValue(new Object[]{h11, h12}));
        mr.setRow(1, new MultiValue(new Object[]{h21, h22}));

        // test
        List<MultiValue> rows = mr.getRowsAsList();

        // weryfikacja
        assertEquals(2, rows.size());
        verifyMultiValue(rows.get(0), "11", "12");
        verifyMultiValue(rows.get(1), "21", "22");
    }

    @Test
    public void testLength() {

        // przygotowanie obiektu
        MultiRow mr = new MultiRow(4);

        // test
        assertEquals(4, mr.length());
    }

    @Test
    public void testGetRow__illegalArgument() {

        // zaleznosci
        AbstractHolder h11 = new StringHolder("11");
        AbstractHolder h12 = new StringHolder("12");

        // przygotowanie obiektu
        MultiRow mr = new MultiRow(1);
        mr.setRow(0, new MultiValue(new Object[]{h11, h12}));

        // bledne indeksy
        int[] indices = {-1, 0, 2, 3};

        // test
        for (int k : indices) {
            try {
                mr.getRow(k);
                fail();
            } catch (ParamException e) {
                assertEquals(ParamException.ErrorCode.INDEX_OUT_OF_BOUNDS, e.getErrorCode());
            }
        }
    }

    @Test
    public void testToString() {

        // zaleznosci
        Object[] row1 = {
            new StringHolder("STR1"),
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new NumberHolder(new BigDecimal("1.23"))
        };
        Object[] row2 = {
            new StringHolder("STR2"),
            new IntegerHolder[]{new IntegerHolder(99L)},
            new NumberHolder(new BigDecimal("2.99"))
        };
        Object[] row3 = {
            new StringHolder("STR3"),
            new IntegerHolder[]{},
            new NumberHolder(null)
        };

        // konfiguracja
        MultiRow mr = new MultiRow(3);
        mr.setRow(0, new MultiValue(row1));
        mr.setRow(1, new MultiValue(row2));
        mr.setRow(2, new MultiValue(row3));

        // oczekiwany wynik
        String expectedResult = Formatter.NL
                + "MultiRow (3)" + Formatter.NL
                + "  1. [STR1, [1, 2, 3], 1.23]" + Formatter.NL
                + "  2. [STR2, [99], 2.99]" + Formatter.NL
                + "  3. [STR3, [], null]" + Formatter.NL;

        // test
        String result = mr.toString();

        // weryfikacja
        assertEquals(expectedResult, result);
    }

    private void verifyMultiValue(MultiValue mv, Object... values) {
        assertArrayEquals(mv.unwrap(), values);
    }
}
