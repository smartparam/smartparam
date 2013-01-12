package pl.generali.merkury.param.core.exception;

/**
 * @author Przemek Hertel
 */
public class ParamDefinitionException extends ParamException {

    private static final long serialVersionUID = 1L;

    public ParamDefinitionException(ErrorCode errorCode, Throwable cause, String message) {
        super(errorCode, cause, message);
    }

    public ParamDefinitionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
