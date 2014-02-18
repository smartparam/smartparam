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
package org.smartparam.engine.core.prepared;

import java.util.Collection;

/**
 * Cache for prepared parameters ({@link  PreparedParameter}).
 *
 * Implementation must be thread safe!
 *
 * @author Przemek Hertel
 */
public interface PreparedParamCache {

    /**
     * Put parameter under given key.
     */
    void put(String paramName, PreparedParameter pp);

    /**
     * Returns parameter from cache or null if none found.
     */
    PreparedParameter get(String paramName);

    /**
     * Evicts parameter from cache.
     */
    void invalidate(String paramName);

    /**
     * Evicts all parameters from cache.
     */
    void invalidate();

    /**
     * Return collection of all parameters stored in cache.
     */
    Collection<String> cachedParameterNames();
}
