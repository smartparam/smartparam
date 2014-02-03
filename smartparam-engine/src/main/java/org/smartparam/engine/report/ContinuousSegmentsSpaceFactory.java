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

import org.smartparam.engine.annotated.annotations.ParamReportLevelSpaceFactory;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.report.tree.ReportLevelValuesSpace;
import org.smartparam.engine.report.tree.ReportLevelValuesSpaceFactory;

/**
 *
 * @author Adam Dubiel
 */
@ParamReportLevelSpaceFactory(value = "", values = {
    BetweenMatcher.BETWEEN_IE,
    BetweenMatcher.BETWEEN_EI,
    BetweenMatcher.BETWEEN_II,
    BetweenMatcher.BETWEEN_EE
})
public class ContinuousSegmentsSpaceFactory implements ReportLevelValuesSpaceFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <V> ReportLevelValuesSpace<V> createSpace() {
        return new ContinuousSegmentsSpace();
    }

}
