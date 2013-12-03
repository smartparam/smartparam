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

import java.util.Map;
import org.smartparam.engine.annotated.RepositoryObjectKey;

/**
 * Contract for repository, that guarantees order of items. Ordering is based on
 * provided indexes, not registration order.
 *
 * @param <TYPE> type of objects in repository
 *
 * @author Adam Dubiel
 */
public interface OrderedRepository<TYPE> extends ItemsContainer<RepositoryObjectKey, TYPE> {

    /**
     * Register object in repository of type under given index.
     *
     * @param type object type (key)
     * @param index order number (simply: priority)
     * @param object object
     */
    void register(String type, int index, TYPE object);

    void registerWithKeys(Map<RepositoryObjectKey, TYPE> objects);
}
