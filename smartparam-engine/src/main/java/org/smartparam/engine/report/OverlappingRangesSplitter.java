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
package org.smartparam.engine.report;

import java.util.List;
import org.smartparam.engine.annotated.annotations.ParamReportingAmbiguousLevelValuesSpace;
import org.smartparam.engine.matchers.decoder.Range;

/**
 *
 * @author Adam Dubiel
 */
public class OverlappingRangesSplitter<C extends Comparable<C>> implements OverlappingSetsSplitter<Range<C>> {

    @Override
    public DisjointSets<Range<C>> split(Range<C> setA, Range<C> setB) {
        DisjointSets<Range<C>> product = new DisjointSets<Range<C>>();

        if (setA.disjoint(setB)) {
            product.withPartOfSetA(setA);
            product.withPartOfSetB(setB);
        } else {
            Range<C> intersection = setA.intersection(setB);
            List<Range<C>> differenceA = setA.subtract(intersection);
            List<Range<C>> differenceB = setB.subtract(intersection);

            product.withIntersection(intersection).withPartsOfSetA(differenceA).withPartsOfSetB(differenceB);
        }

        return product;
    }
}
