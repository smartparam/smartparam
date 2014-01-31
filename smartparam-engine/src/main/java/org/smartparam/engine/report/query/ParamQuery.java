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
package org.smartparam.engine.report.query;

import org.smartparam.engine.report.sekleton.ReportSkeleton;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.output.DetailedParamValue;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.index.CustomizableIndexWalkerBuilder;
import org.smartparam.engine.index.CustomizableIndexWalkerFactory;
import org.smartparam.engine.report.FirstWinsValueChooser;
import org.smartparam.engine.report.ReportValueChooser;
import org.smartparam.engine.report.ReportingLevelLeafValuesExtractor;

/**
 *
 * @author Adam Dubiel
 */
public final class ParamQuery {

    private final ParamEngine paramEngine;

    private final CustomizableIndexWalkerBuilder walkerBuilder;

    private ReportValueChooser<PreparedEntry> valueChooser = new FirstWinsValueChooser<PreparedEntry>();

    private String parameterName;

    private ParamContext context;

    private ReportSkeleton reportSkeleton;

    public ParamQuery(ParamEngine paramEngine) {
        this.paramEngine = paramEngine;
        this.walkerBuilder = CustomizableIndexWalkerBuilder.customizableIndexWalker(paramEngine);
    }

    public static ParamQuery select(ParamEngine paramEngine) {
        return new ParamQuery(paramEngine);
    }

    public DetailedParamValue execute() {
        ReportingLevelLeafValuesExtractor valuesExtractor = new ReportingLevelLeafValuesExtractor(paramEngine, reportSkeleton, valueChooser);
        walkerBuilder.withValuesExtractor(valuesExtractor);
        CustomizableIndexWalkerFactory factory = walkerBuilder.build();

        return paramEngine.getDetailed(parameterName, factory, context);
    }

    public ParamQuery fromParameter(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }

    public ParamQuery withGreedyLevel(String levelName) {
        walkerBuilder.withGreedyLevels(levelName);
        return this;
    }

    public ParamQuery withGreedyLevels(String... levelNames) {
        walkerBuilder.withGreedyLevels(levelNames);
        return this;
    }

    public ParamQuery usingMatcher(String levelName, String matcherCode) {
        walkerBuilder.withOverridenMatcher(levelName, matcherCode);
        return this;
    }

    public ParamQuery makingChoiceUsing(ReportValueChooser<PreparedEntry> valueChooser) {
        this.valueChooser = valueChooser;
        return this;
    }

    public ParamQuery askingFor(ParamContext context) {
        this.context = context;
        return this;
    }

    public ParamQuery fillingIn(ReportSkeleton reportSkeleton) {
        this.reportSkeleton = reportSkeleton;
        return this;
    }
}
