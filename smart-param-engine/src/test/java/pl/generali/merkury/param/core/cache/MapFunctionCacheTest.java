package pl.generali.merkury.param.core.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import pl.generali.merkury.param.model.Function;

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
        Function f = new Function();
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
        Function f1 = new Function();
        Function f2 = new Function();
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
        Function f1 = new Function();
        Function f2 = new Function();
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
