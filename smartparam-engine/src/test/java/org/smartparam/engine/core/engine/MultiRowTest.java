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

import java.util.List;
import static org.testng.AssertJUnit.*;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.types.string.StringHolder;
import org.testng.annotations.Test;

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
            } catch (SmartParamException e) {
                assertEquals(SmartParamErrorCode.INDEX_OUT_OF_BOUNDS, e.getErrorCode());
            }
        }
    }

    private void verifyMultiValue(MultiValue mv, Object... values) {
        assertArrayEquals(mv.unwrap(), values);
    }
}
