package org.smartparam.engine.test.beans;

import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamFunctionRepository(value = "primary", order = AnnotatedBeanConsts.PRIMARY_TEST_ORDER)
public class AnnotatedBeanPrimary implements FunctionRepository {

    @Override
    public Function loadFunction(String functionName) {
        return null;
    }
}
