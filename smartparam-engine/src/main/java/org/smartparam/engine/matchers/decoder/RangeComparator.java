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

import java.util.Comparator;

/**
 *
 * @author Adam Dubiel
 */
public class RangeComparator implements Comparator<Range> {

    private boolean byLowerBound = true;

    private boolean ascending = true;

    public RangeComparator() {
    }

    public RangeComparator(boolean byLowerBound, boolean ascending) {
        this.byLowerBound = byLowerBound;
        this.ascending = ascending;
    }

    public RangeComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Range o1, Range o2) {
        Comparable val1 = extractComparable(o1);
        Comparable val2 = extractComparable(o2);

        int direction = ascending ? 1 : -1;

        return val1.compareTo(val2) * direction;
    }

    private Comparable extractComparable(Range o1) {
        return (Comparable) (byLowerBound ? o1.from() : o1.to());
    }
}
