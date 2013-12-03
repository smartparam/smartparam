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
package org.smartparam.engine.core.provider;

import org.smartparam.engine.annotated.repository.ScanningMatcherRepository;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.matcher.Matcher;

/**
 * @author Przemek Hertel
 */
public class BasicMatcherProviderTest {

    @Test
    public void testSetMatcherMap() {

        // konfiguracja zaleznosci
        Matcher m1 = mock(Matcher.class);
        Matcher m2 = mock(Matcher.class);

        Map<String, Matcher> map = new HashMap<String, Matcher>();
        map.put("A", m1);
        map.put("B", m2);

        // obiekt testowany
        ScanningMatcherRepository provider = new ScanningMatcherRepository();

        // test
        provider.registerAll(map);

        // weryfikacja
        assertSame(m1, provider.getMatcher("A"));
        assertSame(m2, provider.getMatcher("B"));
        assertNull(provider.getMatcher("C"));
        assertNull(provider.getMatcher(null));
    }

    @Test
    public void testRegisterMatcher() {

        // konfiguracja zaleznosci
        Matcher m1 = mock(Matcher.class);
        Matcher m2 = mock(Matcher.class);

        // obiekt testowany
        ScanningMatcherRepository provider = new ScanningMatcherRepository();

        // test
        provider.register("A", m1);
        provider.register("B", m2);

        // weryfikacja
        assertSame(m1, provider.getMatcher("A"));
        assertSame(m2, provider.getMatcher("B"));
        assertNull(provider.getMatcher("C"));
        assertNull(provider.getMatcher(null));
    }
}
