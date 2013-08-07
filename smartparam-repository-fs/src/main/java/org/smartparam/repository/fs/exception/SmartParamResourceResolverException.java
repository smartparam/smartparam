package org.smartparam.repository.fs.exception;

import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SuppressWarnings("serial")
public class SmartParamResourceResolverException extends SmartParamException {

    public SmartParamResourceResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartParamResourceResolverException(String message) {
        super(message);
    }
}
