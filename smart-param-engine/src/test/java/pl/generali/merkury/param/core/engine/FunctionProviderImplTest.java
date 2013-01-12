package pl.generali.merkury.param.core.engine;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import pl.generali.merkury.param.core.cache.FunctionCache;
import pl.generali.merkury.param.core.exception.ParamDefinitionException;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.loader.FunctionLoader;
import pl.generali.merkury.param.model.Function;

/**
 * @author Przemek Hertel
 */
public class FunctionProviderImplTest {

    @Test
    public void testGetFunction() {
        
        // zaleznosci
        Function fun1 = new Function();
        FunctionLoader loader = mock(FunctionLoader.class);
        FunctionCache cache = mock(FunctionCache.class);

        // konfiguracja zaleznosci
        when(loader.load("fun1")).thenReturn(fun1);
        when(cache.get("fun1")).thenReturn(null, fun1);

        // utworzenie testowanego obiektu
        FunctionProviderImpl fp = new FunctionProviderImpl();
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
        FunctionProviderImpl fp = new FunctionProviderImpl();
        fp.setCache(cache);
        fp.setLoader(loader);

        // test
        try {
            fp.getFunction("fun2");
            fail();
        }
        catch(ParamDefinitionException e) {
            assertEquals(ParamException.ErrorCode.UNKNOWN_FUNCTION, e.getErrorCode());
        }

    }

}
