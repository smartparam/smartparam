package pl.generali.merkury.param.core.exception;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Przemek Hertel
 */
public class ParamExceptionTest {

    @Test
    public void testConstrucor__throwable() {
        
        // zaleznosci
        Throwable t = mock(Exception.class);

        // test
        ParamException e = new ParamException(t);

        // weryfikacja
        assertSame(t, e.getCause());
    }

    @Test
    public void testConstrucor__message() {

        // zaleznosci
        String m = "message";

        // test
        ParamException e = new ParamException(m);

        // weryfikacja
        assertEquals(m, e.getMessage());
    }

    @Test
    public void testGetMessage() {

        // testy
        ParamException e1 = new ParamException("message");
        ParamException e2 = new ParamException(ParamException.ErrorCode.UNKNOWN_FUNCTION, "message");

        // weryfikacja
        assertEquals("message", e1.getMessage());
        assertEquals("message [errorcode=UNKNOWN_FUNCTION]", e2.getMessage());
    }
}
