package org.smartparam.engine.core.exception;

import org.apache.log4j.spi.ErrorCode;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class ParamException extends RuntimeException {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Kod bledu.
     */
    private SmartParamErrorCode errorCode;

    /**
     * Konstruktor.
     *
     * @param t przyczyna wyjatku
     */
    public ParamException(Throwable t) {
        super(t.getMessage(), t);
    }

    /**
     * Konstruktor.
     *
     * @param message komunikat o przyczynie wyjatku
     */
    public ParamException(String message) {
        super(message);
    }

    /**
     * Konstruktor.
     *
     * @param errorCode kod bledu
     * @param message komunikat o przyczynie wyjatku
     */
    public ParamException(SmartParamErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Konstruktor.
     *
     * @param errorCode kod bledu
     * @param t przyczyna wyjatku
     * @param message komunikat o przyczynie wyjatku
     */
    public ParamException(SmartParamErrorCode errorCode, Throwable t, String message) {
        super(message, t);
        this.errorCode = errorCode;
    }

    /**
     * Zwraca kod bledu.
     *
     * @return kod bledu
     */
    public SmartParamErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getMessage());
        if (errorCode != null) {
            sb.append(" [errorcode=").append(errorCode).append(']');
        }
        return sb.toString();
    }
}
