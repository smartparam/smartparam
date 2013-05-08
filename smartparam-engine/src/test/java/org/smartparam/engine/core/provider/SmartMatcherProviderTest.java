package org.smartparam.engine.core.provider;

import org.smartparam.engine.core.repository.SmartMatcherRepository;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.index.Matcher;

/**
 * @author Przemek Hertel
 */
public class SmartMatcherProviderTest {

    @Test
    public void testSetMatcherMap() {

        // konfiguracja zaleznosci
        Matcher m1 = mock(Matcher.class);
        Matcher m2 = mock(Matcher.class);

        Map<String, Matcher> map = new HashMap<String, Matcher>();
        map.put("A", m1);
        map.put("B", m2);

        // obiekt testowany
        SmartMatcherRepository provider = new SmartMatcherRepository();

        // test
        provider.setItems(map);

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
        SmartMatcherRepository provider = new SmartMatcherRepository();

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
