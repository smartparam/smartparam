package org.smartparam.engine.core.exception;

/**
 * @author Przemek Hertel
 */
public class SmartParamDefinitionException extends SmartParamException {

    private static final long serialVersionUID = 1L;

    public SmartParamDefinitionException(SmartParamErrorCode errorCode, Throwable cause, String message) {
        super(errorCode, cause, message);
    }

    public SmartParamDefinitionException(SmartParamErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
