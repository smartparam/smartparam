package org.smartparam.engine.core.service;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.OrderedRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public interface ParameterProvider extends OrderedRepository<ParamRepository> {

    Parameter load(String parameterName);

    Set<ParameterEntry> findEntries(String parameterName, String[] levelValues);

    void registerAll(List<ParamRepository> repositories);
}
