package org.smartparam.engine.core.repository;

import java.util.Map;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionRepository {

    Map<String, Function> loadFunctions();

    Function loadFunction(String functionName);

    FunctionRepositoryCapabilities repositoryCapabilities();
}
