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

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.index.Star;

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

    Range(RangeBoundary<C> from, RangeBoundary<C> to) {
        boolean swap = from.compareTo(to) > 0;
        this.from = !swap ? from : to;
        this.to = !swap ? to : from;
    }

    public C from() {
        return from.value();
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

    public boolean disjoint(Range<C> other) {
        return this.to.compareTo(other.from) <= 0 || this.from.compareTo(other.to) >= 0;
    }

    public boolean contains(C value) {
        RangeBoundary<C> encapsulatedValue = new RangeBoundary<C>(value);
        return this.from.compareTo(encapsulatedValue) < 0 && this.to.compareTo(encapsulatedValue) > 0;
    }

    public boolean contains(Range<C> other) {
        return this.from.compareTo(other.from) <= 0 && this.to.compareTo(other.to) >= 0;
    }

    public Range<C> intersection(Range<C> other) {
        if (disjoint(other)) {
            return null;
        }
        if (this.contains(other)) {
            return new Range<C>(other.from, other.to);
        }
        if (other.contains(this)) {
            return new Range<C>(this.from, this.to);
        }
        if (this.contains(other.from.value())) {
            return new Range<C>(other.from, this.to);
        } else {
            return new Range<C>(this.from, other.to);
        }
    }

    public List<Range<C>> subtract(Range<C> other) {
        List<Range<C>> difference = new ArrayList<Range<C>>();
        Range<C> intersection = this.intersection(other);

        if (intersection != null && !other.contains(this)) {
            if (this.contains(other)) {
                if (from.compareTo(intersection.from) != 0) {
                    difference.add(new Range<C>(this.from, intersection.from));
                }
                if (to.compareTo(intersection.to) != 0) {
                    difference.add(new Range<C>(intersection.to, this.to));
                }
            } else if (this.contains(intersection.from())) {
                difference.add(new Range<C>(this.from, intersection.from));
            } else {
                difference.add(new Range<C>(intersection.to, this.to));
            }
        }

        return difference;
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
        int hash = 7;
        hash = 67 * hash + (this.from != null ? this.from.hashCode() : 0);
        hash = 67 * hash + (this.to != null ? this.to.hashCode() : 0);
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
        final Range other = (Range) obj;
        if (this.from != other.from && (this.from == null || !this.from.equals(other.from))) {
            return false;
        }
        if (this.to != other.to && (this.to == null || !this.to.equals(other.to))) {
            return false;
        }
        return true;
    }

}
