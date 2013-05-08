package org.smartparam.engine.test.beans;

import java.util.Map;
import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.FunctionRepositoryCapabilities;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamFunctionRepository(value = "primary", order = AnnotatedBeanConsts.PRIMARY_TEST_ORDER)
public class AnnotatedBeanPrimary implements FunctionRepository {

    public Map<String, Function> loadFunctions() {
        return null;
    }

    public Function loadFunction(String functionName) {
        return null;
    }

    public FunctionRepositoryCapabilities repositoryCapabilities() {
        return FunctionRepositoryCapabilities.SINGLE;
    }

}
