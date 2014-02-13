/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.parameter.request.ParameterRequest;
import org.smartparam.engine.core.parameter.request.ParameterRequestQueue;
import org.smartparam.engine.core.prepared.ParamPreparer;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParameterManager implements ParameterManager {

    private final ParamPreparer preparer;

    private final ParameterProvider parameterProvider;

    private final PreparedParamCache cache;

    private final ParameterRequestQueue requestQueue;

    private final ParameterRequest request;

    public BasicParameterManager(ParamPreparer preparer,
            ParameterProvider parameterProvider,
            PreparedParamCache cache,
            ParameterRequestQueue requestQueue) {
        this.preparer = preparer;
        this.parameterProvider = parameterProvider;
        this.cache = cache;
        this.requestQueue = requestQueue;

        request = createRequest();
    }

    private ParameterRequest createRequest() {
        return new ParameterRequest() {
            @Override
            public PreparedParameter loadAndPrepare(String parameterName) {
                ParameterFromRepository parameter = parameterProvider.load(parameterName);
                if (parameter == null) {
                    return null;
                }

                PreparedParameter preparedParameter = preparer.prepare(parameter);
                cache.put(parameterName, preparedParameter);
                return preparedParameter;
            }
        };
    }

    @Override
    public PreparedParameter getPreparedParameter(final String parameterName) {
        PreparedParameter preparedParameter = cache.get(parameterName);

        if (preparedParameter == null) {
            requestQueue.resolve(parameterName, request);
        }

        return preparedParameter;
    }

    @Override
    public List<PreparedEntry> findEntries(String paramName, String[] levelValues) {
        Set<ParameterEntry> entries = parameterProvider.findEntries(paramName, levelValues);

        List<PreparedEntry> result = new ArrayList<PreparedEntry>(entries.size());
        for (ParameterEntry pe : entries) {
            // no cache, everything can be identifiable
            result.add(preparer.prepareIdentifiableEntry(pe));
        }

        return result;
    }

}
