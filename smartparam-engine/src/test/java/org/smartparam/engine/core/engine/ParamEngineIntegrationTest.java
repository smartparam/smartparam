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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.types.date.DateType;
import org.testng.annotations.BeforeMethod;
import static org.mockito.Mockito.*;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.assembler.FromParamLevel;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.string.StringType;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;
import static org.smartparam.engine.test.builder.JavaFunctionTestBuilder.javaFunction;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 * @author Przemek Hertel
 */
public class ParamEngineIntegrationTest {

    private SmartParamEngine engine;

    private ParamRepository paramRepository;

    private FunctionRepository functionRepository;

    private FunctionInvoker functionInvoker;

    @BeforeMethod
    public void initialize() {
        paramRepository = mock(ParamRepository.class);
        functionRepository = mock(FunctionRepository.class);
        functionInvoker = mock(FunctionInvoker.class);

        ParamEngineConfig config;
        config = ParamEngineConfigBuilder.paramEngineConfig()
                .withType("string", new StringType())
                .withType("integer", new IntegerType())
                .withType("date", new DateType())
                .withParameterRepositories(paramRepository)
                .withFunctionRepository("java", 1, functionRepository)
                .withFunctionInvoker("java", functionInvoker)
                .withMatcher("between", new BetweenMatcher())
                .build();

        engine = (SmartParamEngine) ParamEngineFactory.paramEngine(config);
    }

    @Test
    public void shouldReturnValueOfParameterWithLevelValuesPassedExplicitly() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "F", "11").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A", "F");

        // then
        assertThat(value).hasValue(11l);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToGetParameterValueByContextWithoutLevelCreators() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "42").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        catchException(engine).get("parameter", new DefaultContext());

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.UNDEFINED_LEVEL_CREATOR);
    }

    @Test
    public void shouldThrowExceptionWhenNoValueFoundForNotNullableParameter() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "42").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(1).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        catchException(engine).get("parameter", "B");

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.PARAM_VALUE_NOT_FOUND);
    }

    @Test
    public void shouldAllowToReturnNullForNullableParameter() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "F", "11").build(),
            parameterEntry().withLevels("B", "F", "21").build()
        };
        Parameter parameter = parameter().nullable().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A", "C");

        // then
        assertThat(value).isNull();
    }

    @Test
    public void shouldReturnDefaultValueWhenNoneOtherFound() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "F", "11").build(),
            parameterEntry().withLevels("A", "*", "42").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A", "C");

        // then
        assertThat(value).hasValue(42L);
    }

    @Test
    public void shouldPreferConcreteVlueToDefaultValueWhenBothPossible() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "F", "42").build(),
            parameterEntry().withLevels("A", "*", "11").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A", "F");

        // then
        assertThat(value).hasValue(42L);
    }

    @Test
    public void shouldGetBackToTheRootIfStuckInDeadEndWhenSearchingForValue() {
        // given
        Level[] levels = new Level[]{
            level().withType("integer").withMatcher("between").build(),
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("1-10", "B", "C", "11").build(),
            parameterEntry().withLevels("1-10", "B", "D", "12").build(),
            parameterEntry().withLevels("1-10", "B", "D", "13").build(),
            parameterEntry().withLevels("3-20", "B", "E", "42").build(),
            parameterEntry().withLevels("4-25", "B", "F", "14").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(3).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "6", "B", "E");

        // then
        assertThat(value).hasValue(42L);
    }

    @Test
    public void shouldMatchNullLevelValues() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", null, "11").build(),
            parameterEntry().withLevels("B", "F", "21").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A", null);

        // then
        assertThat(value).hasValue(11L);
    }

    @Test
    public void shouldReturnMultipleValuesInOneRow() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("integer").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "1", "11").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(1).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A");

        // then
        assertThat(value).hasSingleRow(1L, 11L);
    }

    @Test
    public void shouldReturnValueForParameterWithNoInputLevels() {
        // given
        Level[] levels = new Level[]{
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("42").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(0).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter");

        // then
        assertThat(value).hasValue(42l);
    }

    @Test
    public void shouldThrowExceptionWhenProvidingMoreQueryValuesThanDeclaredInputLevels() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "42").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(1).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        catchException(engine).get("parameter", "A", "B");

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.ILLEGAL_LEVEL_VALUES);
    }

    @Test
    public void shouldThrowExceptionWhenProvidingLessQueryValuesThanDeclaredInputLevels() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "B", "42").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        catchException(engine).get("parameter", "A");

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.ILLEGAL_LEVEL_VALUES);
    }

    @Test
    public void shouldFindValueViaRepositoryWhenEvaluatingNoncacheableParameter() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "B", "42").build()
        };
        Parameter parameter = parameter().withName("parameter").nullable().noncacheable().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);
        when(paramRepository.findEntries("parameter", new String[]{"A", "B"})).thenReturn(new HashSet<ParameterEntry>(Arrays.asList(entries)));

        // when
        ParamValue value = engine.get("parameter", "A", "B");

        // then
        assertThat(value).hasValue(42l);
    }

    @Test
    public void shouldReturnArrayFromCellContentWhenArrayFlagIsSetForLevel() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").array().build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "B,C").build()
        };
        Parameter parameter = parameter().withArraySeparator(',').withLevels(levels).withEntries(entries).withInputLevels(1).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A");

        // then
        assertThat(value).hasArray(0, "B", "C");
    }

    @Test
    public void shouldReturnMultipleMatchingRows() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "B", "42", "43").build(),
            parameterEntry().withLevels("A", "B", "43", "44").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue value = engine.get("parameter", "A", "B");

        // then
        assertThat(value).hasRows(2).hasRowWithValues(42l, 43l).hasRowWithValues(43l, 44l);
    }

    @Test
    public void shouldReturnValueOfParameterWithLevelValuesPassedInAnotherFormat() {

        // given
        Level[] levels = new Level[]{
                level().withType("date").build(), // input
                level().withType("integer").build() // output
        };
        ParameterEntry[] entries = new ParameterEntry[]{
                parameterEntry().withLevels("2013-01-27", "5").build(),
                parameterEntry().withLevels("*", "9").build()
        };
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(1).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        ParamValue v1 = engine.get("parameter", "2013-01-27");
        ParamValue v2 = engine.get("parameter", "2013.01.27");
        ParamValue v3 = engine.get("parameter", "27/01/2013");
        ParamValue v4 = engine.get("parameter", "27/01/1999");

        // then
        assertThat(v1).hasIntValue(5);
        assertThat(v2).hasIntValue(5);
        assertThat(v3).hasIntValue(5);
        assertThat(v4).hasIntValue(9);
    }

    @Test
    public void shouldThrowExceptionIfParamterNotFoundInRepositories() {
        // given
        // when
        catchException(engine).get("unknown");

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.UNKNOWN_PARAMETER);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToReadArrayFromlevelThatWasNotMarkedAsArray() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("string").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "B", "42,43").build(),};
        Parameter parameter = parameter().withArraySeparator(',').withLevels(levels).withEntries(entries).withInputLevels(2).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);
        ParamValue value = engine.get("parameter", "A", "B");

        // when
        catchException(value.row()).getArray(0);

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.GETTING_WRONG_TYPE);
    }

    @Test
    public void shouldCallFunctionReturnedByParameter() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("string").build(),
            level().withType("string").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "B", "function").build(),};
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(2).build();
        Function function = javaFunction().build();
        when(paramRepository.load("parameter")).thenReturn(parameter);
        when(functionRepository.loadFunction("function")).thenReturn(function);

        // when
        engine.call("parameter", new LevelValues("A", "B"), "argument");

        // then
        verify(functionInvoker, times(1)).invoke(function, "argument");
    }

    @Test
    public void shouldThrowExceptionIfTryingToCallFunctionFromNonStringType() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").build(),
            level().withType("integer").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "42").build()};
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(1).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        catchException(engine).call("parameter", new LevelValues("A"), "argument");

        // then
        assertThat((SmartParamException) caughtException()).hasErrorCode(SmartParamErrorCode.ILLEGAL_API_USAGE);
    }

    @Test
    public void shouldAssembleObjectFromParameterData() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").withName("code").build(),
            level().withType("integer").withName("count").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "42").build()};
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(0).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        AssembledObject object = engine.getObject("parameter", AssembledObject.class);

        // then
        assertThat(object).isNotNull();
    }

    @Test
    public void shouldAssembleMultipleObjectsFromParameterData() {
        // given
        Level[] levels = new Level[]{
            level().withType("string").withName("code").build(),
            level().withType("integer").withName("count").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("A", "42").build(),
            parameterEntry().withLevels("B", "42").build()};
        Parameter parameter = parameter().withLevels(levels).withEntries(entries).withInputLevels(0).build();
        when(paramRepository.load("parameter")).thenReturn(parameter);

        // when
        Collection<AssembledObject> objects = engine.getObjects("parameter", AssembledObject.class);

        // then
        assertThat(objects).hasSize(2);
    }

    private static class AssembledObject {

        String code;

        int count;

        private AssembledObject(@FromParamLevel("code") String code, @FromParamLevel("count") int count) {
            this.code = code;
            this.count = count;
        }
    }
}
