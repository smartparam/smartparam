package org.smartparam.engine.core.exception;

/**
 * @author Przemek Hertel
 */
public class ParamUsageException extends ParamException {
    
    private static final long serialVersionUID = 1L;
    
	public ParamUsageException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public ParamUsageException(ErrorCode errorCode, Throwable t, String message) {
		super(errorCode, t, message);
	}

}
