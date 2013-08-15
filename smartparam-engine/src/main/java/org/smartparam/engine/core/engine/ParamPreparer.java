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
package org.smartparam.engine.core.engine;

import java.util.List;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.service.ParameterProvider;

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

    void setParamCache(ParamCache cache);

    FunctionProvider getFunctionProvider();

    void setFunctionProvider(FunctionProvider functionProvider);

    TypeRepository getTypeRepository();

    void setTypeRepository(TypeRepository typeRepository);

    MatcherRepository getMatcherRepository();

    void setMatcherRepository(MatcherRepository matcherRepository);

    ParameterProvider getParameterProvider();

    void setParameterProvider(ParameterProvider parameterProvider);
}
