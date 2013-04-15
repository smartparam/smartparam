package org.smartparam.engine.core.cache;

import org.junit.Test;
import org.smartparam.engine.model.function.Function;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * @author Przemek Hertel
 */
public class MapFunctionCacheTest {

    @Test
    public void testGet() {

        // testowany obiekt
        FunctionCache cache = new MapFunctionCache();

        // testy
        assertNull(cache.get("fun"));
    }

    @Test
    public void testPut() {

        // zaleznosci
        Function f = mock(Function.class);
        String fname = "fun.calc";

        // testowany obiekt
        FunctionCache cache = new MapFunctionCache();

        // testy
        cache.put(fname, f);

        // weryfikacja
        assertSame(f, cache.get(fname));
    }

    @Test
    public void testInvalidate() {

        // zaleznosci
        Function f1 = mock(Function.class);
        Function f2 = mock(Function.class);
        String fname1 = "fun.calc.1";
        String fname2 = "fun.calc.2";

        // testowany obiekt
        FunctionCache cache = new MapFunctionCache();
        cache.put(fname1, f1);
        cache.put(fname2, f2);

        // testy:
        // 1)
        assertSame(f1, cache.get(fname1));
        assertSame(f2, cache.get(fname2));

        // 2)
        cache.invalidate(fname1);

        // 3)
        assertNull(cache.get(fname1));
        assertSame(f2, cache.get(fname2));
    }

    @Test
    public void testInvalidate_all() {

        // zaleznosci
        Function f1 = mock(Function.class);
        Function f2 = mock(Function.class);
        String fname1 = "fun.calc.1";
        String fname2 = "fun.calc.2";

        // testowany obiekt
        FunctionCache cache = new MapFunctionCache();
        cache.put(fname1, f1);
        cache.put(fname2, f2);

        // testy:
        // 1)
        assertSame(f1, cache.get(fname1));
        assertSame(f2, cache.get(fname2));

        // 2)
        cache.invalidate();

        // 3)
        assertNull(cache.get(fname1));
        assertNull(cache.get(fname2));
    }
}
