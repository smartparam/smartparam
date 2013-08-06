package org.smartparam.engine.core.repository;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 */
public interface ParamRepository {

    Parameter load(String parameterName);
    
    ParameterBatchLoader batchLoad(String parameterName);

    List<ParameterEntry> findEntries(String parameterName, String[] levelValues);

    Set<String> listParameters();

}
