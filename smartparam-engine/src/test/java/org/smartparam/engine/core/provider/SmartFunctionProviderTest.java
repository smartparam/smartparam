package org.smartparam.engine.core.provider;

import org.junit.*;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.loader.FunctionLoader;
import org.smartparam.engine.model.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * @author Przemek Hertel
 */
public class SmartFunctionProviderTest {

    @Test
    public void testGetFunction() {

        // zaleznosci
        Function fun1 = mock(Function.class);
        FunctionLoader loader = mock(FunctionLoader.class);
        FunctionCache cache = mock(FunctionCache.class);

        // konfiguracja zaleznosci
        when(loader.load("fun1")).thenReturn(fun1);
        when(cache.get("fun1")).thenReturn(null, fun1);

        // utworzenie testowanego obiektu
        SmartFunctionProvider fp = new SmartFunctionProvider();
        fp.setCache(cache);
        fp.setLoader(loader);

        // test
        assertSame(fun1, fp.getFunction("fun1"));       // fizyczne wczytanie przez loader
        assertSame(fun1, fp.getFunction("fun1"));       // uzycie cache'a
    }

    @Test
    public void testGetFunction__unknownFunction() {

        // konfiguracja zaleznosci
        FunctionLoader loader = mock(FunctionLoader.class);
        when(loader.load("fun2")).thenReturn(null);

        FunctionCache cache = mock(FunctionCache.class);
        when(cache.get("fun2")).thenReturn(null);

        // utworzenie testowanego obiektu
        SmartFunctionProvider fp = new SmartFunctionProvider();
        fp.setCache(cache);
        fp.setLoader(loader);

        // test
        try {
            fp.getFunction("fun2");
            fail();
        } catch (SmartParamDefinitionException e) {
            assertEquals(SmartParamErrorCode.UNKNOWN_FUNCTION, e.getErrorCode());
        }

    }
}
