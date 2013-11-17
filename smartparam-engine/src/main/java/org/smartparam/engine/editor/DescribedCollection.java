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
package org.smartparam.engine.editor;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Adam Dubiel
 */
public class DescribedCollection<T> {

    private final RepositoryName source;

    private final Collection<T> collection;

    DescribedCollection(RepositoryName source, Collection<T> collection) {
        this.source = source;
        this.collection = collection;
    }

    public RepositoryName source() {
        return source;
    }

    public Collection<T> collection() {
        return Collections.unmodifiableCollection(collection);
    }
}
