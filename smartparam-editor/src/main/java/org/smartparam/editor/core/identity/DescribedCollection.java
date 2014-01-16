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
package org.smartparam.editor.core.identity;

import org.smartparam.engine.core.repository.RepositoryName;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public class DescribedCollection<T> implements Iterable<T> {

    private final RepositoryName source;

    private final Collection<T> items;

    public DescribedCollection(RepositoryName source, Collection<T> items) {
        this.source = source;
        this.items = items;
    }

    public DescribedCollection(RepositoryName source, T... items) {
        this.source = source;
        this.items = Arrays.asList(items);
    }

    public RepositoryName source() {
        return source;
    }

    public Collection<T> items() {
        return Collections.unmodifiableCollection(items);
    }

    public List<T> itemsList() {
        if (List.class.isAssignableFrom(items.getClass())) {
            return Collections.unmodifiableList((List<T>) items);
        }
        throw new IllegalStateException("Tried to get a non-list collection as list!");
    }

    public T firstItem() {
        return items.iterator().next();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }
}
