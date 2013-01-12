package pl.generali.merkury.param.core.loader;

import java.util.List;
import pl.generali.merkury.param.model.Parameter;
import pl.generali.merkury.param.model.ParameterEntry;

/**
 * @author Przemek Hertel
 */
public interface ParamLoader {

    Parameter load(String parameterName);

    List<ParameterEntry> findEntries(String parameterName, String[] levelValues);
}
