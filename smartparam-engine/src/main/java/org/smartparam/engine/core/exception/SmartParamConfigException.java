package org.smartparam.engine.core.exception;

/**
 *
 * @author Adam Dubiel
 */
public class SmartParamConfigException extends SmartParamException {

    private static final long serialVersionUID = 1L;

    public SmartParamConfigException(String message) {
        super(message);
    }

    public SmartParamConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
