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
package org.smartparam.engine.matchers.type;

import org.smartparam.engine.core.index.Star;
import org.smartparam.engine.util.Objects;

/**
 *
 * @author Adam Dubiel
 */
public class Range<C extends Comparable<? super C>> {

    private final RangeBoundary<C> from;

    private final RangeBoundary<C> to;

    @SuppressWarnings("unchecked")
    public Range(Object from, Object to) {
        this(
                (RangeBoundary<C>) ((from instanceof Star) ? RangeBoundary.minusInfinity() : new RangeBoundary<C>((C) from)),
                (RangeBoundary<C>) ((to instanceof Star) ? RangeBoundary.plusInfinity() : new RangeBoundary<C>((C) to))
        );
    }

    public Range(C from, C to) {
        this(new RangeBoundary<C>(from), new RangeBoundary<C>(to));
    }

    public Range(RangeBoundary<C> from, RangeBoundary<C> to) {
        boolean swap = from.compareTo(to) > 0;
        this.from = !swap ? from : to;
        this.to = !swap ? to : from;
    }

    public C from() {
        return from.value();
    }

    public RangeBoundary<C> boundaryFrom() {
        return from;
    }

    public RangeBoundary<C> boundaryTo() {
        return to;
    }

    public boolean isFromInfinity() {
        return from.isMinusInfinity();
    }

    public C to() {
        return to.value();
    }

    public boolean isToInfinity() {
        return to.isPlusInfinity();
    }

    @Override
    public String toString() {
        return "Range[" + boundAsString(from) + " - " + boundAsString(to) + "]";
    }

    private String boundAsString(Object bound) {
        return bound == null ? "*" : bound.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.from, this.to);
    }

    @Override
    public boolean equals(Object obj) {
        if (!Objects.classEquals(this, obj)) {
            return false;
        }
        final Range<?> other = (Range) obj;
        return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to);
    }

}
