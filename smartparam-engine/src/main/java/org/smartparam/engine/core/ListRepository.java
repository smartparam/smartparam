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

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Adam Dubiel
 */
public class ListRepository<V> {

    private static final Logger logger = LoggerFactory.getLogger(ListRepository.class);

    private Class<?> containedClass;

    private List<V> repositoryList = new ArrayList<V>();

    public ListRepository(Class<?> containedClass) {
        this.containedClass = containedClass;
    }

    public void register(V value) {
        logger.info("{} repository: registering {}", new Object[]{containedClass.getSimpleName(), value.getClass().getSimpleName()});
        repositoryList.add(value);
    }

    public void registerAll(List<V> values) {
        for(V value : values) {
            register(value);
        }
    }

    public List<V> getItems() {
        return new ArrayList<V>(repositoryList);
    }
}
