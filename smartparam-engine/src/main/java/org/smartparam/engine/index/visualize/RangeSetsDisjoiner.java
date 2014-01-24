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
package org.smartparam.engine.index.visualize;

import java.util.List;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherAwareDecoder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.matchers.decoder.Range;

/**
 *
 * @author Adam Dubiel
 */
public class RangeSetsDisjoiner implements SetsDisjoiner<Range<?>> {

    @Override
    @SuppressWarnings("unchecked")
    public DisjointSets disjoin(String setA, String setB, MatcherAwareDecoder<Range<?>> decoder, Matcher matcher, Type<?> valuesType) {
        DisjointSets product = new DisjointSets();

        Range setARange = decoder.decode(setA, valuesType, matcher);
        Range setBRange = decoder.decode(setB, valuesType, matcher);

        if (setARange.disjoint(setBRange)) {
            product.withPartOfSetA(setA);
            product.withPartOfSetB(setB);
        }
        else {
            Range intersection = setARange.intersection(setBRange);
            List<Range> differenceA = setARange.subtract(intersection);
            List<Range> differenceB = setBRange.subtract(intersection);
        }

        return product;
    }
}
