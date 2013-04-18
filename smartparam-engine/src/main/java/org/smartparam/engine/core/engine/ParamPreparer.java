package org.smartparam.engine.core.engine;

import java.util.List;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;

/**
 * Interface for services building complete, in-memory representation of
 * parameter (preparing parameters).
 *
 * @see PreparedParameter
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
public interface ParamPreparer {

    /**
     * Returns prepared parameter for <tt>paramName</tt> parameter. If there is
     * no such parameter it must return null.
     *
     * @param paramName parameter name
     * @return complete representation of parameter (metadata + matrix) or null
     */
    PreparedParameter getPreparedParameter(String paramName);

    /**
     * Returns list of parameter rows that match given level values. TODO:
     * create ParamFinder for this purpose?
     *
     * @param paramName parameter name
     * @param levelValues list of values to match at each level
     * @return list of matching prepared entries (or empty list)
     */
    List<PreparedEntry> findEntries(String paramName, String[] levelValues);

    ParamCache getParamCache();

    TypeRepository getTypeRepository();

    MatcherRepository getMatcherRepository();

    ParamRepository getParamRepository();
}
