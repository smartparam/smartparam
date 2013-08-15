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
package org.smartparam.engine.types.date;

import org.smartparam.engine.types.date.SimpleDateFormatPool;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 

/**
 * @author Przemek Hertel
 */
public class SimpleDateFormatPoolTest {

    @Test
    public void testGet() {

        // przypadki testowe
        Object[][] tests = {
            new Object[]{"dd-MM-yyyy", sdf("dd-MM-yyyy")},
            new Object[]{"dd.MM.yyyy", sdf("dd.MM.yyyy")},
            new Object[]{"yyyy/MM/dd", sdf("yyyy/MM/dd")}
        };

        // uruchomienie testow
        for (Object[] row : tests) {
            String pattern = (String) row[0];
            SimpleDateFormat expected = (SimpleDateFormat) row[1];

            SimpleDateFormat result = new NonAbstractPool().get(pattern);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testGet__cache() {

        // wykonanie testu
        SimpleDateFormat sdf1 = SimpleDateFormatPool.get("dd/MM/yyyy");
        SimpleDateFormat sdf2 = SimpleDateFormatPool.get("dd/MM/yyyy");

        // weryfikacja identycznosci
        assertSame(sdf1, sdf2);
    }

    @Test
    public void testGet__multipleThreads() throws InterruptedException {

        // wektor, w ktorym sa obiekty SDF skojarzone z kolejnymi watkami
        final List<SimpleDateFormat> list = Collections.synchronizedList(new ArrayList<SimpleDateFormat>());

        // 10 watkow, kazdy pobiera swoj SDF i dodaje do kolekcji vector
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Runnable() {

                @Override
                public void run() {
                    list.add(SimpleDateFormatPool.get("dd-MM-yyyy"));
                }
            });
            threads[i].start();
        }

        // bariera
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        // weryfikacja
        for (int i = 0; i < 10; i++) {
            SimpleDateFormat sdf1 = list.get(i);
            for (int j = 0; j < 10; j++) {
                if (j != i) {
                    SimpleDateFormat sdf2 = list.get(j);
                    assertNotSame(sdf1, sdf2);
                }
            }
        }
    }

    private SimpleDateFormat sdf(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf;
    }

    private class NonAbstractPool extends SimpleDateFormatPool {
    }
}
