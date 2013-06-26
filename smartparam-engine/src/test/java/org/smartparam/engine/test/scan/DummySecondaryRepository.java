package org.smartparam.engine.test.scan;

import org.smartparam.engine.annotations.SmartParamFunctionRepository;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamFunctionRepository(value = "secondaryRepsitory", order = 2)
public class DummySecondaryRepository implements FunctionRepository {

    @Override
    public Function loadFunction(String functionName) {
        throw new UnsupportedOperationException("Dummy implementation");
    }
}
