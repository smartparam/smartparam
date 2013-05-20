package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.AbstractAnnotationScanner;
import org.smartparam.engine.core.service.FunctionManager;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.assembler.AssemblerMethod;
import org.smartparam.engine.config.SmartParamRuntimeConfig;
import org.smartparam.engine.config.SmartParamRuntimeConfigBuilder;
import org.smartparam.engine.core.repository.SmartAssemblerProvider;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.repository.AssemblerProvider;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.types.plugin.PluginHolder;
import org.smartparam.engine.util.EngineUtil;
import org.smartparam.engine.util.ParamHelper;

/**
 * in progress...
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
public class SmartParamEngine extends AbstractAnnotationScanner implements ParamEngine {

    private Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private SmartParamRuntimeConfigBuilder configBuilder = new SmartParamRuntimeConfigBuilder();

    private ParamPreparer paramPreparer = null;

    private FunctionManager functionManager = null;

    private AssemblerProvider assemblerProvider;

    public SmartParamRuntimeConfig getConfiguration() {
        return configBuilder.buildConfig(this);
    }

    @Override
    public AbstractHolder getValue(String paramName, ParamContext ctx) {

        logger.debug("enter getValue[{}], ctx={}", paramName, ctx);

        PreparedParameter param = getPreparedParameter(paramName);

        PreparedEntry pe = findParameterEntry(param, ctx);

        AbstractHolder result;

        if (pe != null) {
            result = evaluateParameterEntry(pe, ctx, param.getType());

        } else if (param.isNullable()) {
            result = param.getType().convert(null);

        } else {
            throw raiseValueNotFoundException(paramName, ctx);
        }

        logger.debug("leave getValue[{}], result={}", paramName, result);
        return result;
    }

    @Override
    public AbstractHolder getValue(String paramName, Object... levelValues) {
        DefaultContext ctx = new DefaultContext();
        ctx.setLevelValues(levelValues);

        return getValue(paramName, ctx);
    }

    @SuppressWarnings("unchecked")
    public <T> T getResult(String paramName, Class<T> resultClass, ParamContext ctx) {

        if (ctx.getResultClass() == null) {
            ctx.setResultClass(resultClass);

        } else if (ctx.getResultClass() != resultClass) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Passing resultClass different from ctx#resultClass: " + resultClass + " / " + ctx.getResultClass());
        }

        T result = null;
        AbstractHolder value = getValue(paramName, ctx);

        if (value.isNotNull()) {
            AssemblerMethod asm = assemblerProvider.findAssembler(value.getClass(), resultClass);
            result = (T) asm.assemble(value, ctx);
        }

        return result;
    }

    public Object getResult(String paramName, ParamContext ctx) {

        if (ctx.getResultClass() == null) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Calling getResult() but there is no result class in param context");
        }

        return getResult(paramName, ctx.getResultClass(), ctx);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getResultArray(String paramName, Class<T> resultClass, ParamContext ctx) {

        if (ctx.getResultClass() == null) {
            ctx.setResultClass(resultClass);
        } else if (ctx.getResultClass() != resultClass) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Passing resultClass different from ctx#resultClass: " + resultClass + " / " + ctx.getResultClass());
        }

        AbstractHolder[] array = getArray(paramName, ctx);

        T[] result = (T[]) Array.newInstance(resultClass, array.length);

        for (int i = 0; i < result.length; i++) {
            AssemblerMethod asm = assemblerProvider.findAssembler(array[i].getClass(), resultClass);
            result[i] = (T) asm.assemble(array[i], ctx);
        }

        return result;
    }

    @Override
    public AbstractHolder[] getArray(String paramName, ParamContext ctx) {

        logger.debug("enter getArray[{}], ctx={}", paramName, ctx);

        PreparedParameter param = getPreparedParameter(paramName);

        if (!param.isArray()) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Calling getArray() for non-array parameter: " + paramName);
        }

        PreparedEntry pe = findParameterEntry(param, ctx);

        Type<?> type = param.getType();
        AbstractHolder[] result;

        if (pe != null) {
            result = evaluateParameterEntryAsArray(pe, ctx, type, param.getArraySeparator());
        } else {
            if (param.isNullable()) {
                result = type.newArray(0);
            } else {
                throw raiseValueNotFoundException(paramName, ctx);
            }
        }

        logger.debug("leave getArray[{}], result={}", paramName, result);
        return result;
    }

    @Override
    public MultiValue getMultiValue(String paramName, ParamContext ctx) {

        logger.debug("enter getMultiValue[{}], ctx={}", paramName, ctx);

        PreparedParameter param = getPreparedParameter(paramName);

        if (!param.isMultivalue()) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Calling getMultiValue() for non-multivalue parameter: " + paramName);
        }

        PreparedEntry pe = findParameterEntry(param, ctx);

        if (pe == null) {
            if (param.isNullable()) {
                return null;
            }

            throw raiseValueNotFoundException(paramName, ctx);
        }

        int inputLevels = param.getInputLevelsCount();   // liczba poziomow wejsciowych (k)
        int outputLevels = param.getLevelCount() - inputLevels;     // liczba poziomow wyjsciowych (n-k)
        logger.trace("k={}, l={}", inputLevels, outputLevels);

        PreparedLevel[] levels = param.getLevels();
        Object[] vector = new Object[outputLevels];
        for (int i = 0; i < outputLevels; ++i) {

            String cellText = pe.getLevel(inputLevels + i + 1);
            PreparedLevel level = levels[inputLevels + i];

            Type<?> cellType = level.getType();
            Object cellValue;

            if (level.isArray()) {
                cellValue = evaluateStringAsArray(cellText, cellType, ',');
            } else {
                cellValue = ParamHelper.decode(cellType, cellText);
            }

            vector[i] = cellValue;
        }

        MultiValue result = new MultiValue(vector);

        logger.debug("leave getMultiValue[{}], result={}", paramName, result);
        return result;
    }

    @Override
    public MultiRow getMultiRow(String paramName, ParamContext ctx) {

        logger.debug("enter getMultiRow[{}], ctx={}", paramName, ctx);

        PreparedParameter param = getPreparedParameter(paramName);

        if (!param.isMultivalue()) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Calling getMultiRow() for non-multivalue parameter: " + paramName);
        }

        PreparedEntry[] rows = findParameterEntries(param, ctx);

        if (rows.length == 0) {
            if (param.isNullable()) {
                logger.debug("leave getMultiRow[{}], result=null", paramName);
                return null;
            }

            throw raiseValueNotFoundException(paramName, ctx);
        }

        int k = param.getInputLevelsCount();   // liczba poziomow wejsciowych (k)
        int l = param.getLevelCount() - k;     // liczba poziomow wyjsciowych (n-k)

        MultiRow result = new MultiRow(rows.length);

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

            result.setRow(i, new MultiValue(vector));
        }

        logger.debug("leave getMultiRow[{}], result={}", paramName, result);
        return result;
    }

    public Object[] unwrap(AbstractHolder[] array) {
        Object[] result = new Object[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getValue();
        }
        return result;
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
            names[i] = ClassUtils.getShortClassName(args[i], "null");
        }
        return names;
    }

    @Override
    public Object call(String paramName, ParamContext ctx, Object... args) {
        AbstractHolder holder = getValue(paramName, ctx);

        if (!(holder instanceof PluginHolder)) {
            logger.warn("result is not plugin holder: {}", holder);
        }

        String functionName = holder.getString();

        if (functionName != null) {
            return callFunction(functionName, args);
        }

        return null;
    }

    /**
     * Zwraca wartosc wiersza parametru. Wartosc pochodzi z:
     * <ol>
     * <li> pola <tt>value</tt>,
     * <li> lub funkcji <tt>function</tt>, jesli <tt>value=null</tt>.
     * </ol>
     *
     * Jesli <tt>value</tt> i <tt>function</tt> sa rowne <tt>null</tt>, zwracany
     * jest <tt>null</tt> skonwetowany na typ <tt>type</tt>
     * zgodnie z metoda <tt>convert</tt> danego typu.
     *
     * @param pe wiersz parametru
     * @param ctx kontekstu uzycia parametru
     * @param type typ parametru
     *
     * @return holder reprezentujacy wartosc parametru
     */
    AbstractHolder evaluateParameterEntry(PreparedEntry pe, ParamContext ctx, Type<?> type) {

        String v = pe.getValue();
        if (v != null) {
            return ParamHelper.decode(type, v);
        }

        if (pe.getFunction() != null) {
            Object result = functionManager.invokeFunction(pe.getFunction(), ctx);
            return ParamHelper.convert(type, result);
        }

        return ParamHelper.convert(type, (Object) null);
    }

    /**
     * Zwraca wartosc wiersza parametru jako <b>tablice</b> holderow
     * odpowiedniego typu, na przyklad IntegerHolder[] czy NumberHolder[].
     * <p>
     * Wartosci zwracanej tablicy pochodza z:
     * <ol>
     * <li> pola <tt>value</tt>,
     * <li> lub funkcji <tt>function</tt>, jesli <tt>value=null</tt>.
     * </ol>
     *
     * Jesli <tt>value</tt> i <tt>function</tt> sa rowne <tt>null</tt>, zwracana
     * jest pusta tablica typu wynikajacego z <tt>type</tt>.
     *
     * @param pe wiersz parametru
     * @param ctx kontekstu uzycia parametru
     * @param type typ parametru
     * @param separator znak separatora wartosci
     *
     * @return tablica holderow typu wynikajacego z <tt>type</tt>
     */
    AbstractHolder[] evaluateParameterEntryAsArray(PreparedEntry pe, ParamContext ctx, Type<?> type, char separator) {
        String v = pe.getValue();
        if (v != null) {
            return evaluateStringAsArray(v, type, separator);
        }

        if (pe.getFunction() != null) {
            Object result = functionManager.invokeFunction(pe.getFunction(), ctx);

            // funkcja zwrocila null - zamieniamy na pusta tablice odpowiedniego typu
            if (result == null) {
                return type.newArray(0);
            }

            // rezultat funkcji to tablica
            if (result.getClass().isArray()) {
                if (result instanceof Object[]) {
                    // tablica obiektow
                    return ParamHelper.convert(type, (Object[]) result);

                } else {
                    // tablica typow prostych
                    return ParamHelper.convertNonObjectArray(type, result);
                }
            }

            // rezultat funkcji to kolekcja
            if (result instanceof Collection) {
                return ParamHelper.convert(type, (Collection) result);
            }

            // rezultat funkcji to string (csv)
            if (result instanceof String) {
                return evaluateStringAsArray(result.toString(), type, separator);
            }

            // rezultat funkcji to pojedynczy obiekt - traktujemy jako 1-elementowa tablice
            AbstractHolder[] array = type.newArray(1);
            array[0] = ParamHelper.convert(type, result);
            return array;
        }

        // brak value i function - zwracamy pusta tablice odpowiedniego typu
        return type.newArray(0);
    }

    /**
     * Dekoduje zawartosc komorki <tt>value</tt> typu tablicowego (array) na
     * tablice wartosci typu <tt>AbstractHolder[]</tt>.
     *
     * Wartosc <tt>value</tt> moze pochodzic:
     * <ol>
     * <li> z komorki poziomu - w przypadku parametru typu <tt>multivalue</tt>
     * <li> z wartosci w wierszu (ParameterEntry#value) - w przypadku parametru
     * zwyklego
     * </ol>
     *
     * Gdy wartosc jest pusta lub rowna null, zwraca pusta tablice typu
     * wynikajacego z <tt>type</tt>.
     *
     * @param value zawartosc komorki, ktora bedzie parsowana jako tablica
     * @param type typ zawartosci (typ parametru lub typ poziomu)
     * @param separator znak separatora
     *
     * @return tablica zdekodowanych wartosci
     */
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
        String[] values = new String[param.getInputLevelsCount()];

        for (int i = 0; i < values.length; ++i) {

            PreparedLevel level = levels[i];
            Function levelCreator = level.getLevelCreator();

            if (levelCreator == null) {
                throw new SmartParamException(
                        SmartParamErrorCode.UNDEFINED_LEVEL_CREATOR,
                        "Level(" + (i + 1) + ") has no level-creator funtion, but function is needed to evaluate level value");
            }

            Object result = functionManager.invokeFunction(levelCreator, ctx);
            logger.trace("L{}: evaluated: {}", i + 1, result);

            if (result == null) {
                values[i] = null;
            } else if (result instanceof String) {
                values[i] = (String) result;
            } else if (level.getType() != null) {
                values[i] = level.getType().convert(result).getString();
            } else {
                values[i] = result.toString();
            }
        }

        if (isDebug()) {
            logger.debug("discovered level values: {}", Arrays.toString(values));
        }

        ctx.setLevelValues(values);
    }

    private PreparedEntry findParameterEntry(PreparedParameter param, String[] levelValues) {

        if (param.isCacheable()) {

            LevelIndex<PreparedEntry> index = param.getIndex();
            if (levelValues.length != index.getLevelCount()) {
                throw new SmartParamUsageException(
                        SmartParamErrorCode.ILLEGAL_LEVEL_VALUES,
                        "Illegal user-supplied levelValues array: levelCount=" + index.getLevelCount() + ", levelValues=" + levelValues.length);
            }

            return index.find(levelValues);

        } else {

            List<PreparedEntry> entries = paramPreparer.findEntries(param.getName(), levelValues);
            return entries.isEmpty() ? null : entries.get(0);
        }
    }

    private PreparedEntry findParameterEntry(PreparedParameter param, ParamContext ctx) {

        if (ctx.getLevelValues() == null) {
            evaluateLevelValues(param, ctx);
        }

        return findParameterEntry(param, ctx.getLevelValues());
    }

    private PreparedEntry[] findParameterEntries(PreparedParameter param, String[] levelValues) {

        List<PreparedEntry> entries;

        if (param.isCacheable()) {

            LevelIndex<PreparedEntry> index = param.getIndex();
            if (levelValues.length != index.getLevelCount()) {
                throw new SmartParamUsageException(
                        SmartParamErrorCode.ILLEGAL_LEVEL_VALUES,
                        "Illegal user-supplied levelValues array: levelCount=" + index.getLevelCount() + ", levelValues=" + levelValues.length);
            }

            entries = index.findAll(levelValues);

        } else {

            entries = paramPreparer.findEntries(param.getName(), levelValues);
        }

        return entries != null ? entries.toArray(new PreparedEntry[entries.size()]) : new PreparedEntry[0];
    }

    private PreparedEntry[] findParameterEntries(PreparedParameter param, ParamContext ctx) {

        if (ctx.getLevelValues() == null) {
            evaluateLevelValues(param, ctx);
        }

        return findParameterEntries(param, ctx.getLevelValues());
    }

    private PreparedParameter getPreparedParameter(String paramName) {
        PreparedParameter param = paramPreparer.getPreparedParameter(paramName);
        logger.trace("prepared parameter: {}", param);

        if (param == null) {
            throw new SmartParamException(SmartParamErrorCode.UNKNOWN_PARAMETER, "parameter not found: " + paramName);
        }
        return param;
    }

    @Override
    public ParamPreparer getParamPreparer() {
        return paramPreparer;
    }

    protected boolean hasParamPreparer() {
        return paramPreparer != null;
    }

    @Override
    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    protected boolean hasFunctionManager() {
        return functionManager != null;
    }

    @Override
    public void setParamPreparer(ParamPreparer paramPreparer) {
        this.paramPreparer = paramPreparer;
    }

    @Override
    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
    }

    public void setAssemblerProvider(SmartAssemblerProvider assemblerProvider) {
        this.assemblerProvider = assemblerProvider;
    }

    //todo ph: par 0 bool type
    //todo ph: par 3 lt, le, gt, ge matchers
    private SmartParamException raiseValueNotFoundException(String paramName, ParamContext ctx) {

        return new SmartParamException(
                SmartParamErrorCode.PARAM_VALUE_NOT_FOUND,
                "Value not found for parameter [" + paramName + "] and given context: " + ctx);
    }

    boolean isDebug() {
        return logger.isDebugEnabled();
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }
}
