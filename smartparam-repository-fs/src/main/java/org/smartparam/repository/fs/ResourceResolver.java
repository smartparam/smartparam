package org.smartparam.repository.fs;

import java.util.Map;
import org.smartparam.engine.core.batch.ParameterBatchLoader;

/**
 *
 * @author Adam Dubiel
 */
public interface ResourceResolver {

    Map<String, String> findParameterResources();

    ParameterBatchLoader loadParameterFromResource(String parameterResourceName);
}
