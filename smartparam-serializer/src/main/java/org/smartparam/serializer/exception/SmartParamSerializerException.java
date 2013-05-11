package org.smartparam.serializer.exception;

import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamSerializerException extends SmartParamException {

    private static final long serialVersionUID = 1L;

    public SmartParamSerializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
