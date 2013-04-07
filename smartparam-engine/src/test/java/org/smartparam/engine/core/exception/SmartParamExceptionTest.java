package org.smartparam.engine.core.exception;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Przemek Hertel
 */
public class SmartParamExceptionTest {

    @Test
    public void testConstrucor__throwable() {
        
        // zaleznosci
        Throwable t = mock(Exception.class);

        // test
        SmartParamException e = new SmartParamException(t);

        // weryfikacja
        assertSame(t, e.getCause());
    }

    @Test
    public void testConstrucor__message() {

        // zaleznosci
        String m = "message";

        // test
        SmartParamException e = new SmartParamException(m);

        // weryfikacja
        assertEquals(m, e.getMessage());
    }

    @Test
    public void testGetMessage() {

        // testy
        SmartParamException e1 = new SmartParamException("message");
        SmartParamException e2 = new SmartParamException(SmartParamErrorCode.UNKNOWN_FUNCTION, "message");

        // weryfikacja
        assertEquals("message", e1.getMessage());
        assertEquals("message [errorcode=UNKNOWN_FUNCTION]", e2.getMessage());
    }
}
