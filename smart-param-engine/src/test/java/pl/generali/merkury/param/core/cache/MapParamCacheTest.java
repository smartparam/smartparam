package pl.generali.merkury.param.core.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.engine.PreparedParameter;

/**
 * @author Przemek Hertel
 */
public class MapParamCacheTest {

    @Test
    public void testGet() {

        // testowany obiekt
        ParamCache cache = new MapParamCache();

        // testy
        assertNull(cache.get("fun"));
    }

    @Test
    public void testPut() {

        // zaleznosci
        PreparedParameter pp = new PreparedParameter();
        String name = "calc.factor";

        // testowany obiekt
        ParamCache cache = new MapParamCache();

        // testy
        cache.put(name, pp);

        // weryfikacja
        assertSame(pp, cache.get(name));
    }

    @Test
    public void testInvalidate() {

        // zaleznosci
        PreparedParameter pp1 = new PreparedParameter();
        PreparedParameter pp2 = new PreparedParameter();
        String name1 = "par.calc.1";
        String name2 = "par.calc.2";

        // testowany obiekt
        ParamCache cache = new MapParamCache();
        cache.put(name1, pp1);
        cache.put(name2, pp2);

        // testy:
        // 1)
        assertSame(pp1, cache.get(name1));
        assertSame(pp2, cache.get(name2));

        // 2)
        cache.invalidate(name1);

        // 3)
        assertNull(cache.get(name1));
        assertSame(pp2, cache.get(name2));
    }

    @Test
    public void testInvalidate_all() {

        // zaleznosci
        PreparedParameter pp1 = new PreparedParameter();
        PreparedParameter pp2 = new PreparedParameter();
        String name1 = "par.calc.1";
        String name2 = "par.calc.2";

        // testowany obiekt
        ParamCache cache = new MapParamCache();
        cache.put(name1, pp1);
        cache.put(name2, pp2);

        // testy:
        // 1)
        assertSame(pp1, cache.get(name1));
        assertSame(pp2, cache.get(name2));

        // 2)
        cache.invalidate();

        // 3)
        assertNull(cache.get(name1));
        assertNull(cache.get(name2));
    }

}
