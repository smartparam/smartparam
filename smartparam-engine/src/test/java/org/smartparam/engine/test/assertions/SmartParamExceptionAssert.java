package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamExceptionAssert extends AbstractAssert<SmartParamExceptionAssert, SmartParamException> {

    private SmartParamExceptionAssert(SmartParamException actual) {
        super(actual, SmartParamExceptionAssert.class);
    }

    public static SmartParamExceptionAssert assertThat(SmartParamException actual) {
        return new SmartParamExceptionAssert(actual);
    }

    public SmartParamExceptionAssert hasErrorCode(SmartParamErrorCode errorCode) {
        Assertions.assertThat(actual.getErrorCode()).isSameAs(errorCode);
        return this;
    }
}
