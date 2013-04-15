package org.smartparam.engine.core.loader;

import org.smartparam.engine.model.function.Function;

/**
 * @author Przemek Hertel
 */
public interface FunctionLoader {

    Function load(String functionName);
}
