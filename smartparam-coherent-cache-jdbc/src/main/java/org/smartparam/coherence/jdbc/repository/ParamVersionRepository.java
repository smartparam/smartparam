package org.smartparam.coherence.jdbc.repository;

import java.util.Map;

public interface ParamVersionRepository {

    /**
     * Increments given parameter's version.
     *
     * @param paramName name of the parameter to increment.
     * @return the new version of the given parameter.
     */
    Long incrementVersion(String paramName);

    /**
     * @return current version of the given parameter, null if param not versioned.
     */
    Long versionOfParam(String paramName);

    /**
     * @return mapping of all tracked parameter names and their versions.
     */
    Map<String, Long> versionOfAllParams();

}
