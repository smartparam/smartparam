package pl.generali.merkury.param.core.loader;

import pl.generali.merkury.param.model.Function;

/**
 * @author Przemek Hertel
 */
public interface FunctionLoader {

    Function load(String functionName);
}
