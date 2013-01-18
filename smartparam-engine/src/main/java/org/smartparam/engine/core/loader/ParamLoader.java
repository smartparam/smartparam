package org.smartparam.engine.core.loader;

import java.util.List;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 */
public interface ParamLoader {

    Parameter load(String parameterName);

    List<ParameterEntry> findEntries(String parameterName, String[] levelValues);
}
