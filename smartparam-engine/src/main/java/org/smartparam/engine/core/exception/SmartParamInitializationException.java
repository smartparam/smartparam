package org.smartparam.engine.core.exception;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class SmartParamInitializationException extends SmartParamException {

    private static final long serialVersionUID = 1L;

    public SmartParamInitializationException(SmartParamErrorCode errorCode, Throwable t, String message) {
        super(errorCode, t, message);
    }
}
