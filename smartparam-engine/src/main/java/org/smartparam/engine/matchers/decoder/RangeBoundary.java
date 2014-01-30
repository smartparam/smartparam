/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.engine.matchers.decoder;

/**
 *
 * @author Adam Dubiel
 */
public class RangeBoundary<C extends Comparable<? super C>> implements Comparable<RangeBoundary<C>> {

    private final C value;

    private final boolean infinity;

    private final int infinitySign;

    private RangeBoundary(C value, boolean infinity, int infinitySign) {
        this.value = value;
        this.infinity = infinity;
        this.infinitySign = infinitySign;
    }

    public RangeBoundary(C value) {
        this(value, value == null, 1);
    }

    static <C extends Comparable<C>> RangeBoundary<C> plusInfinity() {
        return new RangeBoundary<C>(null, true, 1);
    }

    static <C extends Comparable<C>> RangeBoundary<C> minusInfinity() {
        return new RangeBoundary<C>(null, true, -1);
    }

    C value() {
        return value;
    }

    boolean finite() {
        return !infinity;
    }

    boolean isPlusInfinity() {
        return infinity && infinitySign > 0;
    }

    boolean isMinusInfinity() {
        return infinity && infinitySign < 0;
    }

    @Override
    public int compareTo(RangeBoundary<C> other) {
        if (this.infinity && other.infinity) {
            return this.infinitySign - other.infinitySign;
        }
        if (this.infinity) {
            return this.isPlusInfinity() ? 1 : -1;
        }
        if (other.infinity) {
            return other.isPlusInfinity() ? -1 : 1;
        }
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        if (isMinusInfinity()) {
            return "-*";
        } else if (isPlusInfinity()) {
            return "+*";
        }
        return value.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 47 * hash + (this.infinity ? 1 : 0);
        hash = 47 * hash + this.infinitySign;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RangeBoundary<?> other = (RangeBoundary<?>) obj;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        if (this.infinity != other.infinity) {
            return false;
        }
        if (this.infinitySign != other.infinitySign) {
            return false;
        }
        return true;
    }

}
