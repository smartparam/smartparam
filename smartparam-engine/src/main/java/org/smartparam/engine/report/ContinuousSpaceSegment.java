/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.report;

/**
 *
 * @author Adam Dubiel
 */
public class ContinuousSpaceSegment<C extends Comparable<? super C>, V> implements Comparable<ContinuousSpaceSegment<C, V>> {

    private final C segmentStart;

    private final C segmentEnd;

    private final V value;

    public ContinuousSpaceSegment(C from, C to, V value) {
        this.segmentStart = from;
        this.segmentEnd = to;
        this.value = value;
    }

    public ContinuousSpaceSegment(ContinuousSpaceSegment<C, V> other, V value) {
        this.segmentStart = other.segmentStart();
        this.segmentEnd = other.segmentEnd();
        this.value = value;
    }

    public boolean contains(C point) {
        return segmentStart.compareTo(point) < 0 && segmentEnd.compareTo(point) > 0;
    }

    IntersectionType intersects(C from, C to) {
        boolean containsFrom = contains(from);
        boolean containsTo = contains(to);

        if (from.compareTo(segmentStart) == 0 && to.compareTo(segmentEnd()) == 0) {
            return IntersectionType.IDENTICAL;
        } else if (containsFrom && containsTo) {
            return IntersectionType.CONTAINS;
        } else if (containsTo) {
            return IntersectionType.BEFORE;
        } else if (containsFrom) {
            return IntersectionType.AFTER;
        }
        return IntersectionType.NONE;
    }

    public C segmentStart() {
        return segmentStart;
    }

    public C segmentEnd() {
        return segmentEnd;
    }

    public V value() {
        return value;
    }

    @Override
    public int compareTo(ContinuousSpaceSegment<C, V> other) {
        int fromComparison = segmentStart.compareTo(other.segmentStart);
        if (fromComparison == 0) {
            return segmentEnd.compareTo(other.segmentEnd);
        }
        return fromComparison;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.segmentStart != null ? this.segmentStart.hashCode() : 0);
        hash = 97 * hash + (this.segmentEnd != null ? this.segmentEnd.hashCode() : 0);
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
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
        final ContinuousSpaceSegment<?, ?> other = (ContinuousSpaceSegment<?, ?>) obj;
        if (this.segmentStart != other.segmentStart && (this.segmentStart == null || !this.segmentStart.equals(other.segmentStart))) {
            return false;
        }
        if (this.segmentEnd != other.segmentEnd && (this.segmentEnd == null || !this.segmentEnd.equals(other.segmentEnd))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    static enum IntersectionType {

        NONE, IDENTICAL, BEFORE, CONTAINS, AFTER
    }
}
