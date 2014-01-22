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

import org.smartparam.engine.core.index.Star;

/**
 *
 * @author Adam Dubiel
 */
public class Range {

    private final Object from;

    private final Object to;

    public Range(Object from, Object to) {
        this.from = from;
        this.to = to;
    }

    public static Range of(Object from, Object to) {
        return new Range(from, to);
    }

    public Object from() {
        return from;
    }

    public boolean isFromStar() {
        return from instanceof Star;
    }

    @SuppressWarnings("unchecked")
    public <T> T fromAs(Class<T> clazz) {
        return isFromStar() ? null : (T) from;
    }

    public Object to() {
        return to;
    }

    public boolean isToStar() {
        return to instanceof Star;
    }

    @SuppressWarnings("unchecked")
    public <T> T toAs(Class<T> clazz) {
        return isToStar() ? null : (T) to;
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
