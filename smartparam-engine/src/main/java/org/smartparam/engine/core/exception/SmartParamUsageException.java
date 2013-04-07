package org.smartparam.engine.core.exception;

/**
 * @author Przemek Hertel
 */
public class SmartParamUsageException extends SmartParamException {
    
    private static final long serialVersionUID = 1L;
    
	public SmartParamUsageException(SmartParamErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public SmartParamUsageException(SmartParamErrorCode errorCode, Throwable t, String message) {
		super(errorCode, t, message);
	}

}
