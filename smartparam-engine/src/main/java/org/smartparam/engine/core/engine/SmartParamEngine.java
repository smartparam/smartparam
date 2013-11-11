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
package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.service.FunctionManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.config.ParamEngineRuntimeConfigBuilder;
import org.smartparam.engine.core.assembler.ObjectAssembler;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.util.EngineUtil;
import org.smartparam.engine.util.ParamHelper;

/**
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public class SmartParamEngine implements ParamEngine {

    private Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private ParamEngineRuntimeConfigBuilder configBuilder;

    private ParamPreparer paramPreparer;

    private FunctionManager functionManager;

    private ObjectAssembler objectAssembler;

    public SmartParamEngine(ParamPreparer paramPreparer, FunctionManager functionManager, ObjectAssembler objectAssembler, ParamEngineRuntimeConfigBuilder configBuilder) {
        this.paramPreparer = paramPreparer;
        this.functionManager = functionManager;
        this.objectAssembler = objectAssembler;
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

            throw raiseValueNotFoundException(paramName, ctx);
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
                    cellValue = ParamHelper.decode(cellType, cellText);
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
    public <T> T getObject(String paramName, Class<T> outputClass, ParamContext context) {
        ParamValue paramValue = get(paramName, context);
        return objectAssembler.assemble(outputClass, paramValue.row());
    }

    @Override
    public <T> T getObject(String paramName, Class<T> outputClass, Object... inputLevels) {
        ParamContext context = new LevelValues(inputLevels);
        return getObject(paramName, outputClass, context);
    }

    @Override
    public <T> Collection<T> getObjects(String paramName, Class<T> outputClass, ParamContext context) {
        ParamValue paramValue = get(paramName, context);
        return objectAssembler.assemble(outputClass, paramValue);
    }

    @Override
    public <T> Collection<T> getObjects(String paramName, Class<T> outputClass, Object... inputLevels) {
        ParamContext context = new LevelValues(inputLevels);
        return getObjects(paramName, outputClass, context);
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

    public Object call(String paramName, ParamContext ctx, Object... args) {
        AbstractHolder holder = get(paramName, ctx).get();

        if (!(holder instanceof StringHolder)) {
            throw new SmartParamException(SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Can't call function if returned value is not of \"string\" type! Got " + holder.getClass().getSimpleName() + " instead of StringHolder.");
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
                array[i] = ParamHelper.decode(type, tokens[i]);
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

        for (int i = 0; i < values.length; ++i) {
            PreparedLevel level = levels[i];
            Function levelCreator = level.getLevelCreator();

            if (levelCreator == null) {
                throw new SmartParamException(
                        SmartParamErrorCode.UNDEFINED_LEVEL_CREATOR,
                        String.format("Level[%d] has no level creator function registered. "
                        + "When using dynamic context, level creators are mandatory for all input levels.", i + 1));
            }

            Object result = functionManager.invokeFunction(levelCreator, ctx);
            logger.trace("L{}: evaluated: {}", i + 1, result);

            values[i] = result;
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
            throw new SmartParamUsageException(SmartParamErrorCode.ILLEGAL_LEVEL_VALUES,
                    String.format("Level values array length differs from parameter input levels count (%d != %d). Provided values: %s.",
                    levelValues.length, parameterLevelCount, Arrays.toString(levelValues)));
        }
    }

    private PreparedEntry[] findParameterEntries(PreparedParameter param, ParamContext ctx) {

        if (ctx.getLevelValues() == null) {
            evaluateLevelValues(param, ctx);
        }

        validateLevelValues(ctx.getLevelValues(), param.getInputLevelsCount());

        String[] normalizedInputValues = ParamHelper.normalize(param, ctx.getLevelValues());
        return findParameterEntries(param, normalizedInputValues);
    }

    private PreparedParameter getPreparedParameter(String paramName) {
        PreparedParameter param = paramPreparer.getPreparedParameter(paramName);
        logger.trace("prepared parameter: {}", param);

        if (param == null) {
            throw new SmartParamException(SmartParamErrorCode.UNKNOWN_PARAMETER,
                    String.format("Parameter %s was not found in any of registered repositories. "
                    + "Check if name is correct and repositories are properly configured and initalized.", paramName));
        }
        return param;
    }

    private SmartParamException raiseValueNotFoundException(String paramName, ParamContext context) {
        return new SmartParamException(
                SmartParamErrorCode.PARAM_VALUE_NOT_FOUND,
                String.format("No value found for parameter [%s] using values from context %s.\n"
                + "If parameter should return null values instead of throwing this exception, set nullable flag to true.", paramName, context));
    }
}
