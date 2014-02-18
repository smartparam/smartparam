/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core;

import org.smartparam.engine.core.output.ParamValue;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.function.FunctionManager;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.output.DetailedParamValue;
import org.smartparam.engine.core.output.factory.DefaultParamValueFactory;
import org.smartparam.engine.core.output.factory.DetailedParamValueFactory;
import org.smartparam.engine.core.output.factory.ParamValueFactory;
import org.smartparam.engine.core.parameter.ParameterManager;
import org.smartparam.engine.core.prepared.InputValueNormalizer;
import org.smartparam.engine.types.string.StringHolder;

/**
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public class SmartParamEngine implements ParamEngine {

    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private final ParamEngineRuntimeConfigBuilder configBuilder;

    private final ParameterManager parameterManager;

    private final FunctionManager functionManager;

    private final DefaultParamValueFactory defaultParamValueFactory;

    private final DetailedParamValueFactory detailedParamValueFactory;

    private final LevelIndexWalkerFactory fastIndexWalkerFactory = new FastLevelIndexWalkerFactory();

    public SmartParamEngine(ParamEngineRuntimeConfigBuilder configBuilder,
            ParameterManager parameterManager,
            FunctionManager functionManager,
            DefaultParamValueFactory defaultParamValueFactory,
            DetailedParamValueFactory detailedParamValueFactory) {
        this.configBuilder = configBuilder;
        this.parameterManager = parameterManager;
        this.functionManager = functionManager;
        this.defaultParamValueFactory = defaultParamValueFactory;
        this.detailedParamValueFactory = detailedParamValueFactory;
    }

    @Override
    public ParamEngineRuntimeConfig runtimeConfiguration() {
        return configBuilder.buildConfig();
    }

    @Override
    public ParamValue get(String parameterName, ParamContext context) {
        return get(parameterName, fastIndexWalkerFactory, context);
    }

    @Override
    public ParamValue get(String parameterName, LevelIndexWalkerFactory customWalkerFactory, ParamContext context) {
        return get(parameterName, customWalkerFactory, defaultParamValueFactory, context);
    }

    @Override
    public DetailedParamValue getDetailed(String parameterName, ParamContext context) {
        return getDetailed(parameterName, fastIndexWalkerFactory, context);
    }

    @Override
    public DetailedParamValue getDetailed(String parameterName, LevelIndexWalkerFactory customWalkerFactory, ParamContext context) {
        return (DetailedParamValue) get(parameterName, customWalkerFactory, detailedParamValueFactory, context);
    }

    private ParamValue get(String parameterName, LevelIndexWalkerFactory customWalkerFactory, ParamValueFactory paramValueFactory, ParamContext context) {
        logger.debug("enter get[{}], walker={}, ctx={}", parameterName, customWalkerFactory.getClass().getSimpleName(), context);

        // obtain prepared parameter
        PreparedParameter param = getPreparedParameter(parameterName);

        // find entries matching given context
        PreparedEntry[] rows = findParameterEntries(customWalkerFactory, param, context);

        if (rows.length == 0) {
            if (param.isNullable()) {
                logger.debug("leave get[{}], result=null", parameterName);
                return paramValueFactory.empty();
            }

            throw new ParameterValueNotFoundException(parameterName, context);
        }

        ParamValue result = paramValueFactory.create(param, rows);

        logger.debug("leave get[{}], result={}", parameterName, result);
        return result;
    }

    @Override
    public ParamValue get(String paramName, Object... inputLevels) {
        ParamContext ctx = new LevelValues(inputLevels);
        return get(paramName, ctx);
    }

    @Override
    public Object callFunction(String functionName, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug("calling function [{}] with args: {}", functionName, classNames(args));
        }

        Object result = functionManager.invokeFunction(functionName, args);

        logger.debug("function result: {}", result);
        return result;
    }

    private String[] classNames(Object... args) {
        String[] names = new String[args.length];
        for (int i = 0; i < args.length; ++i) {
            names[i] = args[i] != null ? args[i].getClass().getSimpleName() : "null";
        }
        return names;
    }

    @Override
    public Object callEvaluatedFunction(String paramName, ParamContext ctx, Object... args) {
        ValueHolder holder = get(paramName, ctx).getHolder();

        if (!(holder instanceof StringHolder)) {
            throw new InvalidFunctionToCallException(paramName, holder);
        }

        String functionName = holder.getString();

        if (functionName != null) {
            return callFunction(functionName, args);
        }

        return null;
    }

    private void evaluateLevelValues(PreparedParameter param, ParamContext ctx) {
        logger.trace("evaluating level values");

        PreparedLevel[] levels = param.getLevels();
        Object[] values = new Object[param.getInputLevelsCount()];

        for (int levelIndex = 0; levelIndex < values.length; ++levelIndex) {
            PreparedLevel level = levels[levelIndex];
            Function levelCreator = level.getLevelCreator();

            if (levelCreator == null) {
                throw new UndefinedLevelCreatorException(levelIndex);
            }

            Object result = functionManager.invokeFunction(levelCreator, ctx);
            logger.trace("L{}: evaluated: {}", levelIndex, result);

            values[levelIndex] = result;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("discovered level values: {}", Arrays.toString(values));
        }

        ctx.setLevelValues(values);
    }

    private PreparedEntry[] findParameterEntries(LevelIndexWalkerFactory indexWalkerFactory, PreparedParameter param, ParamContext ctx) {
        if (ctx.getLevelValues() == null) {
            evaluateLevelValues(param, ctx);
        }

        validateLevelValues(ctx.getLevelValues(), param.getInputLevelsCount());

        String[] normalizedInputValues = InputValueNormalizer.normalize(param, ctx.getLevelValues());
        return findParameterEntries(indexWalkerFactory, param, normalizedInputValues);
    }

    private PreparedEntry[] findParameterEntries(LevelIndexWalkerFactory indexWalkerFactory, PreparedParameter param, String[] levelValues) {

        List<PreparedEntry> entries;

        if (param.isCacheable()) {
            entries = indexWalkerFactory.create(param, levelValues).find();
        } else {
            entries = parameterManager.findEntries(param.getName(), levelValues);
        }

        return entries != null ? entries.toArray(new PreparedEntry[entries.size()]) : new PreparedEntry[0];
    }

    private void validateLevelValues(Object[] levelValues, int parameterLevelCount) {
        if (levelValues.length != parameterLevelCount) {
            throw new InvalidLevelValuesQuery(levelValues, parameterLevelCount);
        }
    }

    private PreparedParameter getPreparedParameter(String paramName) {
        PreparedParameter param = parameterManager.getPreparedParameter(paramName);
        logger.trace("prepared parameter: {}", param);

        if (param == null) {
            throw new UnknownParameterException(paramName);
        }
        return param;
    }
}
