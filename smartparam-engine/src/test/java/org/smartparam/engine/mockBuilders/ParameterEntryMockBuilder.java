package org.smartparam.engine.mockBuilders;

import java.util.Arrays;
import org.smartparam.engine.model.Function;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.util.EngineUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link ParameterEntry} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class ParameterEntryMockBuilder {

    private ParameterEntry parameterEntry;

    private ParameterEntryMockBuilder() {
        this.parameterEntry = mock(ParameterEntry.class);
    }

    public static ParameterEntryMockBuilder parameterEntry() {
        return new ParameterEntryMockBuilder();
    }

    public static ParameterEntry parameterEntry(String csvLevels, String value) {
        return parameterEntry().withLevels(EngineUtil.split(csvLevels, ';')).withValue(value).get();
    }

    public static ParameterEntry parameterEntry(String[] levels, String value) {
        return parameterEntry().withLevels(levels).withValue(value).get();
    }

    public static ParameterEntry parameterEntry(String csvLevels, Function function) {
        return parameterEntry().withLevels(EngineUtil.split(csvLevels, ';')).withFunction(function).get();
    }

    public ParameterEntry get() {
        return parameterEntry;
    }

    public ParameterEntryMockBuilder withLevels(String... levels) {
        when(parameterEntry.getLevels()).thenReturn(levels);

        for (int index = 0; index < levels.length; ++index) {
            when(parameterEntry.getLevel(index)).thenReturn(levels[index]);
            when(parameterEntry.getLevels(index + 1)).thenReturn(Arrays.copyOfRange(levels, 0, index + 1));
        }

        return this;
    }

    public ParameterEntryMockBuilder withValue(String value) {
        when(parameterEntry.getValue()).thenReturn(value);
        return this;
    }

    public ParameterEntryMockBuilder withFunction(Function function) {
        when(parameterEntry.getFunction()).thenReturn(function);
        return this;
    }
}
