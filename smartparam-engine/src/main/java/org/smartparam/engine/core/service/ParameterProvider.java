package org.smartparam.engine.core.service;

import java.util.List;
import org.smartparam.engine.core.OrderedRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterProvider extends OrderedRepository<ParamRepository> {

    Parameter load(String parameterName);

    List<ParameterEntry> findEntries(String parameterName, String[] levelValues);
}
