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
package org.smartparam.engine.cache;

import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 * Implementacja {@link PreparedParamCache} oparata na wspolbieznej wersji HashMapy.
 * Funkcje zapisane w tym cache'u sie nie przedawniaja az do wywolania
 * metody {@link #invalidate(java.lang.String)}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MapPreparedParamCache extends MapCache<PreparedParameter> implements PreparedParamCache {
}
