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
package org.smartparam.engine.core.function;

import java.util.Map;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.function.Function;

/**
 * Kontrakt zapewniajacy cache'owanie obiektow funkcji z repozytorium.
 * Silnik parametryczny w wielu metodach pobiera obiektu funkcji
 * na podstawie jej unikalnej nazwy. Do tego celu wykorzystuje
 * implementacje tego wlasnie interfejsu.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface FunctionCache {

    /**
     * Wstawia funkcje <tt>function</tt> pod kluczem <tt>functionName</tt>.
     *
     * @param functionName unikalna nazwa funkcji
     * @param function     funkcja z repozytorium
     */
    void put(String functionName, Function function);

    void putAll(Map<String, Function> functions);

    /**
     * Zwraca funkcje o nazwie <tt>functionName</tt> lub <tt>null</tt>,
     * jesli nie ma w cache'u takiej funkcji.
     *
     * @param functionName nazwa funkcji
     *
     * @return funkcja pobrana z cache'a
     */
    Function get(String functionName);

    /**
     * Usuwa z cache'a funkcje o nazwie <tt>functionName</tt>.
     *
     * @param functionName nazwa funkcji
     */
    void invalidate(String functionName);

    /**
     * Usuwa z cache'a wszystkie funkcje.
     */
    void invalidate();
}
