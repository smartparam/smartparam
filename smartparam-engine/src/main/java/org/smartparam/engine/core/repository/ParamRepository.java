/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.core.repository;

import java.util.Set;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * Contract for every readonly parameter repository. ParamEngine instance can
 * have multiple parameter repositories defined, either to serve different
 * parameters from different sources or to create a hierarchy, so it is
 * possible to cover cherry-picked parameters for development purposes.
 *
 * Repositories should be registered using {@link org.smartparam.engine.core.service.ParameterProvider}
 * (or just via {@link org.smartparam.engine.config.ParamEngineConfigBuilder}).
 *
 * Parameter can be retrieved using one of two modes. First one is loading whole parameter
 * at once (to create {@link org.smartparam.engine.core.index.LevelIndex}), which
 * is preferred way and should be used for most of parameters.
 * Second one useful for huge parameters (too big to be able to live on the heap)
 * is to load only metadata without entries, and use repository mechanisms to
 * query for values each time parameter is used.
 * Every repository has to implement first mode, second one is complementary.
 *
 * @author Przemek Hertel
 */
public interface ParamRepository {

    /**
     * Load parameter.
     * If parameter is not cacheable, this method should return only metadata
     * (parameter object with empty entry set). Parameter query evaluation should be
     * done using {@link #findEntries(java.lang.String, java.lang.String[])} method.
     *
     * @param parameterName unique name
     * @return parameter
     */
    Parameter load(String parameterName);

    /**
     * Support for batch loading of parameter, when there are too many entries
     * to read them all at once.
     *
     * @param parameterName name of parameter
     * @return metadata + batch loader of entries
     */
    ParameterBatchLoader batchLoad(String parameterName);

    /**
     * Return set of entries from parameter that match provided query values.
     * Repository has to do the matching by its own means. Implementation
     * of this method is optional, should return null if not supported.
     *
     * @param parameterName parameter to evaluate
     * @param levelValues query (level) values
     * @return set of entries or null if either not found or not supported
     */
    Set<ParameterEntry> findEntries(String parameterName, String[] levelValues);

    /**
     * Returns set of all parameter names kept in this repository.
     *
     * @return parameter names
     */
    Set<String> listParameters();

}
