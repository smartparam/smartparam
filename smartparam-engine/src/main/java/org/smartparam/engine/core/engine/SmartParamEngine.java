package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.service.FunctionManager;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.assembler.AssemblerMethod;
import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.config.ParamEngineRuntimeConfigBuilder;
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
public class SmartParamEngine implements ParamEngine {

    private Logger logger = LoggerFactory.getLogger(SmartParamEngine.class);

    private ParamEngineRuntimeConfigBuilder configBuilder = new ParamEngineRuntimeConfigBuilder();

    private ParamPreparer paramPreparer;

    private FunctionManager functionManager;

    private AssemblerProvider assemblerProvider;

    @Override
    public ParamEngineRuntimeConfig getConfiguration() {
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

	@Deprecated
    @Override
    public MultiValue getMultiValue(String paramName, ParamContext ctx) {

        PreparedParameter param = getPreparedParameter(paramName);

        if (!param.isMultivalue()) {
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ILLEGAL_API_USAGE,
                    "Calling getMultiValue() for non-multivalue parameter: " + paramName);
        }

		ParamValue value = get(paramName, ctx);
		if (value == null) {
			return null;
		}

		return value.row();
    }

	@Deprecated
    @Override
    public MultiRow getMultiRow(String paramName, ParamContext ctx) {

		ParamValue value = get(paramName, ctx);
		if (value == null) {
			return null;
		}

		MultiRow mr = new MultiRow(value.size());
		for (int i = 0; i < value.size(); i++) {
			mr.setRow(i, value.row(i + 1));
		}

		return mr;
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

			mv[i] = new MultiValue(vector);
		}

		ParamValue result = new ParamValueImpl(mv, param.getLevelNameMap());

		logger.debug("leave get[{}], result={}", paramName, result);
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
            names[i] = args[i] != null ? args[i].getClass().getSimpleName() : "null";
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
                        String.format("Level[%d] has no level creator function registered. "
                        + "When using dynamic context, level creators are mandatory for all input levels.", i + 1));
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
            validateLevelValues(levelValues, index.getLevelCount());
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
            validateLevelValues(levelValues, index.getLevelCount());
            entries = index.findAll(levelValues);
        } else {
            entries = paramPreparer.findEntries(param.getName(), levelValues);
        }

        return entries != null ? entries.toArray(new PreparedEntry[entries.size()]) : new PreparedEntry[0];
    }

    private void validateLevelValues(String[] levelValues, int parameterLevelCount) {
        if (levelValues.length != parameterLevelCount) {
                throw new SmartParamUsageException(
                        SmartParamErrorCode.ILLEGAL_LEVEL_VALUES,
                        String.format("Level values array length differs from parameter input levels count (%d != %d). "
                        + "Provided values: %s.",
                        levelValues.length, parameterLevelCount, levelValues));
            }
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
            throw new SmartParamException(SmartParamErrorCode.UNKNOWN_PARAMETER,
                    String.format("Parameter %s was not found in any of registered repositories. "
                    + "Check if name is correct and repositories are properly configured and initalized.", paramName));
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

    private SmartParamException raiseValueNotFoundException(String paramName, ParamContext context) {

        return new SmartParamException(
                SmartParamErrorCode.PARAM_VALUE_NOT_FOUND,
                String.format("No value found for parameter [%s] using values from context %s.\n"
                + "If parameter should return null values instead of throwing this exception, set nullable flag to true.", paramName, context));
    }

    boolean isDebug() {
        return logger.isDebugEnabled();
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }
}
