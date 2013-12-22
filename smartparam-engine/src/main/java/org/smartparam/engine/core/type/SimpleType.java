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

/**
 * Implementation of {@link Type} that provides default holder and implements
 * most of methods. To create custom type encode/decode routines are needed.
 * User has no control over used holder ({@link ObjectHolder} for non null and
 * {@link EmptyHolder} for null values), so it should never be used with
 * ValueHolder#get*() methods, only {@link ValueHolder#getValue() } is allowed.
 *
 * @author Adam Dubiel
 */
public abstract class SimpleType<V> extends AbstractType<V, ValueHolder> {

    protected SimpleType(Class<V> heldClass) {
        super(heldClass);
    }

    @Override
    protected ValueHolder createEmptyHolder() {
        return new EmptyHolder();
    }

    @Override
    protected ValueHolder createHolder(Object heldValue) {
        return new ObjectHolder(heldValue);
    }

    protected ValueHolder decodeHolder(String text) {
        return new ObjectHolder(decodeValue(text));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String encode(ValueHolder holder) {
        if (holder.isNotNull()) {
            return encodeValue((V) holder.getValue());
        }
        return null;
    }

    protected abstract String encodeValue(V value);

    protected abstract V decodeValue(String text);

    public ValueHolder[] newArray(int size) {
        return new ObjectHolder[size];
    }

}
