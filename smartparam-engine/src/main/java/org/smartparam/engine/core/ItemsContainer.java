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
package org.smartparam.engine.core;

import java.util.Map;

/**
 * General item container contract.
 *
 * @param <KEY> object key type
 * @param <TYPE> object type
 *
 * @author Adam Dubiel
 */
public interface ItemsContainer<KEY, TYPE> {

    /**
     * Return registered items.
     *
     * @return registered items
     */
    Map<String, TYPE> registeredItems();

    /**
     * Insert all items to repository.
     *
     * @param objects items
     */
    void registerAll(Map<String, TYPE> objects);

}
