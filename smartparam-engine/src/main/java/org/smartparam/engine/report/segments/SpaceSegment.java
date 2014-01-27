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
package org.smartparam.engine.report.segments;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public class SpaceSegment<C extends Comparable<? super C>, V> implements Comparable<SpaceSegment<C, V>> {

    private final C segmentStart;

    private final C segmentEnd;

    private final List<V> values = new ArrayList<V>();

    public SpaceSegment(SpaceSet<C> set) {
        this.segmentStart = set.from();
        this.segmentEnd = set.to();
    }

    public boolean intersectsWith(C from, C to) {
        return contains(from) || contains(to);
    }

    public boolean contains(C point) {
        return segmentStart.compareTo(point) < 0 && segmentEnd.compareTo(point) > 0;
    }

    @Override
    public int compareTo(SpaceSegment<C, V> other) {
        int fromComparison = segmentStart.compareTo(other.segmentStart);
        if (fromComparison == 0) {
            return segmentEnd.compareTo(other.segmentEnd);
        }
        return fromComparison;
    }

}
