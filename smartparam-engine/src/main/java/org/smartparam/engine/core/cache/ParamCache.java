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
package org.smartparam.engine.core.cache;

import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 * Kontrakt zapewniajacy cache'owanie przygotowanych parametrow ({@link PreparedParameter}).
 * Silnik parametryczny pobiera obiekty PreparedParameter na podstawie nazwy.
 * Do tego celu wykorzystuje implementacje tego wlasnie interfejsu.
 * <p>
 *
 * Implementacja cache'a musi byc <b>thread-safe</b>.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ParamCache {

    /**
     * Wstawia parametr <tt>pp</tt> pod kluczem <tt>paramName</tt>.
     *
     * @param paramName unikalna nazwa parametru
     * @param pp        przygotowany parametr
     */
    void put(String paramName, PreparedParameter pp);

    /**
     * Zwraca parametr o nazwie <tt>paramName</tt> lub <tt>null</tt>,
     * jesli nie ma w cache'u takiego parametru.
     *
     * @param paramName nazwa parametru
     *
     * @return parametr pobrany z cache'a
     */
    PreparedParameter get(String paramName);

    /**
     * Usuwa z cache'a parametr o nazwie <tt>paramName</tt>.
     *
     * @param paramName nazwa parametru
     */
    void invalidate(String paramName);

    /**
     * Usuwa z cache'a wszystkie parametry.
     */
    void invalidate();
}
