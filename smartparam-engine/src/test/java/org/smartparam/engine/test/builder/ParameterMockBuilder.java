package org.smartparam.engine.test.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mockito.Mockito;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link Parameter} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class ParameterMockBuilder {

    private Parameter parameter;

    private ParameterMockBuilder() {
        this.parameter = mock(Parameter.class);
        when(parameter.isArchive()).thenReturn(false);
        when(parameter.isArray()).thenReturn(false);
        when(parameter.isCacheable()).thenReturn(true);
        when(parameter.isMultivalue()).thenReturn(false);
        when(parameter.isNullable()).thenReturn(false);
        when(parameter.getArraySeparator()).thenReturn(',');
    }

    private ParameterMockBuilder(Parameter base) {
        this.parameter = base;
    }

    public static ParameterMockBuilder parameter() {
        return new ParameterMockBuilder();
    }

    public static ParameterMockBuilder parameter(Parameter base) {
        return new ParameterMockBuilder(base);
    }

    public static Parameter parameter(String name, String type, boolean nullable, Set<ParameterEntry> entries) {
        return parameter().withName(name).withType(type).withNullable(nullable).withEntries(entries).get();
    }

    public Parameter get() {
        return parameter;
    }

    public ParameterMockBuilder withName(String name) {
        when(parameter.getName()).thenReturn(name);
        return this;
    }

    public ParameterMockBuilder withType(String type) {
        when(parameter.getType()).thenReturn(type);
        return this;
    }

    public ParameterMockBuilder withNullable(boolean nullable) {
        when(parameter.isNullable()).thenReturn(nullable);
        return this;
    }

    public ParameterMockBuilder withEntries(Set<ParameterEntry> entries) {
        Mockito.doReturn(entries).when(parameter).getEntries();
        return this;
    }

    public ParameterMockBuilder withEntries(ParameterEntry... entries) {
        Set<ParameterEntry> entriesSet = new HashSet<ParameterEntry>(Arrays.asList(entries));
        Mockito.doReturn(entriesSet).when(parameter).getEntries();
        return this;
    }

    public ParameterMockBuilder withLevels(Level... levels) {
        List<Level> list = new ArrayList<Level>();
        for (int index = 0; index < levels.length; ++index) {
            when(parameter.getLevel(index)).thenReturn(levels[index]);
            list.add(levels[index]);
        }
        Mockito.doReturn(list).when(parameter).getLevels();
        when(parameter.getLevelCount()).thenReturn(levels.length);

        return this;
    }

    public ParameterMockBuilder withArchive(boolean archive) {
        when(parameter.isArchive()).thenReturn(false);
        return this;
    }

    public ParameterMockBuilder withArray(boolean array) {
        when(parameter.isArray()).thenReturn(array);
        return this;
    }

    public ParameterMockBuilder withCacheable(boolean cacheable) {
        when(parameter.isCacheable()).thenReturn(cacheable);
        return this;
    }

    public ParameterMockBuilder withMultivalue(boolean multivalue) {
        when(parameter.isMultivalue()).thenReturn(multivalue);
        return this;
    }

    public ParameterMockBuilder withInputLevels(int inputLevels) {
        when(parameter.getInputLevels()).thenReturn(inputLevels);
        return this;
    }

    public ParameterMockBuilder withArraySeparator(char separator) {
        when(parameter.getArraySeparator()).thenReturn(separator);
        return this;
    }
}
