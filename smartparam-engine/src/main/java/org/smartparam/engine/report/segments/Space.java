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
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Adam Dubiel
 */
public class Space<C extends Comparable<? super C>, V> {

    private final Set<SpaceSegment<C, V>> segments = new TreeSet<SpaceSegment<C, V>>();

    private final Set<C> discreteValues = new TreeSet<C>();

    public void put(SpaceSet<C> set, V value) {
        SpaceSegment<C, V> segment = new SpaceSegment<C, V>(set);
        segments.add(new SpaceSegment<C, V>(set));
        discreteValues.add(set.from());
        discreteValues.add(set.to());

        for(SpaceSegment<C, V> intersectingSegment : segmentsAt(set.from(), set.to())) {

        }
    }

    private void refreshSegments() {

    }

    private List<SpaceSegment<C, V>> segmentsAt(C from, C to) {
        List<SpaceSegment<C, V>> intersectingSegments = new ArrayList<SpaceSegment<C, V>>();

        for (SpaceSegment<C, V> segment : segments) {
            if (segment.intersectsWith(from, to)) {
                intersectingSegments.add(segment);
            }
        }

        return intersectingSegments;
    }
}
