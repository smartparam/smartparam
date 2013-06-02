package org.smartparam.repository.fs;

import java.util.Map;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ResourceResolver {

    Map<String, String> findParameterResources();

    Parameter loadParameterFromResource(String parameterResourceName);
}
