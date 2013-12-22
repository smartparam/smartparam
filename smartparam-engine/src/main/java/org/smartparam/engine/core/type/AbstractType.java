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
package org.smartparam.engine.core.type;

import org.smartparam.engine.util.EngineUtil;

/**
 * Abstract class with default {@link Type#convert(java.lang.Object) }
 * implementation that should be good enough for most custom types.
 *
 * @author Adam Dubiel
 */
public abstract class AbstractType<V, H extends ValueHolder> implements Type<H> {

    private final Class<V> heldClass;

    protected AbstractType(Class<V> heldClass) {
        this.heldClass = heldClass;
    }

    /**
     * Create holder without value.
     */
    protected abstract H createEmptyHolder();

    /**
     * Create holder with not null value.
     */
    protected abstract H createHolder(V value);

    @Override
    public H decode(String text) {
        return EngineUtil.hasText(text) ? decodeHolder(text) : createEmptyHolder();
    }

    @Override
    public String encode(H holder) {
        return holder.getString();
    }

    /**
     * Decode text into holder, text is not null and not empty.
     */
    protected abstract H decodeHolder(String text);

    @SuppressWarnings("unchecked")
    public H convert(Object obj) {
        if (obj == null) {
            return createEmptyHolder();
        }
        if (heldClass.isAssignableFrom(obj.getClass())) {
            return createHolder((V) obj);
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }

        throw new IllegalArgumentException("Conversion not supported for: " + obj.getClass());
    }

}
