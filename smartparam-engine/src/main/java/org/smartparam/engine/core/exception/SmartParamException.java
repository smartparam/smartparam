package org.smartparam.engine.core.exception;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class SmartParamException extends RuntimeException {

    private SmartParamErrorCode errorCode;

    public SmartParamException(Throwable t) {
        super(t.getMessage(), t);
    }

    public SmartParamException(String message) {
        super(message);
    }

    public SmartParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartParamException(SmartParamErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SmartParamException(SmartParamErrorCode errorCode, Throwable t, String message) {
        super(message, t);
        this.errorCode = errorCode;
    }

    public SmartParamErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(super.getMessage());
        if (errorCode != null) {
            sb.append(" [errorcode=").append(errorCode).append(']');
        }
        return sb.toString();
    }
}
