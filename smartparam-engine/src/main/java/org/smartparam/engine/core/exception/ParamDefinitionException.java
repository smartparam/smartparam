package org.smartparam.engine.core.exception;

/**
 * @author Przemek Hertel
 */
public class ParamDefinitionException extends ParamException {

    private static final long serialVersionUID = 1L;

    public ParamDefinitionException(SmartParamErrorCode errorCode, Throwable cause, String message) {
        super(errorCode, cause, message);
    }

    public ParamDefinitionException(SmartParamErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
