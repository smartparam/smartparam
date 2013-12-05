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

import org.smartparam.engine.core.output.MultiValue;
import org.smartparam.engine.core.output.ParamValueImpl;
import org.smartparam.engine.core.output.ParamValue;
import org.smartparam.engine.core.prepared.ParamPreparer;
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
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.prepared.InputValueNormalizer;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.util.EngineUtil;

/**
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public class SmartParamEngine implements ParamEngine {

    private final Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private final ParamEngineRuntimeConfigBuilder configBuilder;

    private final ParamPreparer paramPreparer;

    private final FunctionManager functionManager;

    public SmartParamEngine(ParamPreparer paramPreparer, FunctionManager functionManager, ParamEngineRuntimeConfigBuilder configBuilder) {
        this.paramPreparer = paramPreparer;
        this.functionManager = functionManager;
        this.configBuilder = configBuilder;
    }

    @Override
    public ParamEngineRuntimeConfig getConfiguration() {
        return configBuilder.buildConfig();
    }

    @Override
    public ParamValue get(String paramName, ParamContext ctx) {

        logger.debug("enter get[{}], ctx={}", paramName, ctx);

        // obtain prepared parameter
        PreparedParameter param = getPreparedParameter(paramName);

        // find entries matching given context
        PreparedEntry[] rows = findParameterEntries(param, ctx);

        // todo ph think about it
        if (rows.length == 0) {
            if (param.isNullable()) {
                logger.debug("leave get[{}], result=null", paramName);
                return null;
            }

            throw new ParameterValueNotFoundException(paramName, ctx);
        }

        int k = param.getInputLevelsCount();   // liczba poziomow wejsciowych (k)
        int l = param.getLevelCount() - k;     // liczba poziomow wyjsciowych (n-k)

        // allocate result matrix
        MultiValue[] mv = new MultiValue[rows.length];

        // iteracja po wierszach podmacierzy
        for (int i = 0; i < rows.length; i++) {
            PreparedEntry pe = rows[i];

            PreparedLevel[] levels = param.getLevels();
            Object[] vector = new Object[l];

            // iteracja po kolumnach podmacierzy (czyli po poziomach wyjsciowych)
            for (int j = 0; j < l; ++j) {
                String cellText = pe.getLevel(k + j + 1);
                PreparedLevel level = levels[k + j];

                Type<?> cellType = level.getType();
                Object cellValue;

                if (level.isArray()) {
                    cellValue = evaluateStringAsArray(cellText, cellType, ',');
                } else {
                    cellValue = TypeDecoder.decode(cellType, cellText);
                }

                vector[j] = cellValue;
            }

            mv[i] = new MultiValue(vector, param.getLevelNameMap());
        }

        ParamValue result = new ParamValueImpl(mv, param.getLevelNameMap());

        logger.debug("leave get[{}], result={}", paramName, result);
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

    public Object callEvaluatedFunction(String paramName, ParamContext ctx, Object... args) {
        AbstractHolder holder = get(paramName, ctx).get();

        if (!(holder instanceof StringHolder)) {
            throw new InvalidFunctionToCallException(paramName, holder);
        }

        String functionName = holder.getString();

        if (functionName != null) {
            return callFunction(functionName, args);
        }

        return null;
    }

    AbstractHolder[] evaluateStringAsArray(String value, Type<?> type, char separator) {

        if (EngineUtil.hasText(value)) {
            String[] tokens = EngineUtil.split(value, separator);
            AbstractHolder[] array = type.newArray(tokens.length);
            for (int i = 0; i < tokens.length; i++) {
                array[i] = TypeDecoder.decode(type, tokens[i]);
            }
            return array;

        } else {
            return type.newArray(0);
        }
    }

    void evaluateLevelValues(PreparedParameter param, ParamContext ctx) {
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

    private PreparedEntry[] findParameterEntries(PreparedParameter param, String[] levelValues) {

        List<PreparedEntry> entries;

        if (param.isCacheable()) {
            LevelIndex<PreparedEntry> index = param.getIndex();
            entries = index.find(levelValues);
        } else {
            entries = paramPreparer.findEntries(param.getName(), levelValues);
        }

        return entries != null ? entries.toArray(new PreparedEntry[entries.size()]) : new PreparedEntry[0];
    }

    private void validateLevelValues(Object[] levelValues, int parameterLevelCount) {
        if (levelValues.length != parameterLevelCount) {
            throw new InvalidLevelValuesQuery(levelValues, parameterLevelCount);
        }
    }

    private PreparedEntry[] findParameterEntries(PreparedParameter param, ParamContext ctx) {

        if (ctx.getLevelValues() == null) {
            evaluateLevelValues(param, ctx);
        }

        validateLevelValues(ctx.getLevelValues(), param.getInputLevelsCount());

        String[] normalizedInputValues = InputValueNormalizer.normalize(param, ctx.getLevelValues());
        return findParameterEntries(param, normalizedInputValues);
    }

    private PreparedParameter getPreparedParameter(String paramName) {
        PreparedParameter param = paramPreparer.getPreparedParameter(paramName);
        logger.trace("prepared parameter: {}", param);

        if (param == null) {
            throw new UnknownParameterException(paramName);
        }
        return param;
    }
}
