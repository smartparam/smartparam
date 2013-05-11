package org.smartparam.engine.test.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterMockBuilder {

    private ParameterMock parameter;

    public ParameterMockBuilder(String name) {
        parameter = new ParameterMock();
        parameter.setLevels(new ArrayList<Level>());
        parameter.setEntries(new HashSet<ParameterEntry>());
        parameter.setName(name);
    }
    
    public Parameter get() {
        return parameter;
    }

    public ParameterMockBuilder withInputLevels(int inputLevels) {
        parameter.setInputLevels(inputLevels);
        return this;
    }
    
    public ParameterMockBuilder cacheable(boolean cacheable) {
        parameter.setArray(cacheable);
        return this;
    }

    public ParameterMockBuilder multivalue(boolean multivalue) {
        parameter.setArray(multivalue);
        return this;
    }

    public ParameterMockBuilder nullable(boolean nullable) {
        parameter.setNullable(nullable);
        return this;
    }

    public ParameterMockBuilder withLevels(Level... levels) {
        parameter.getLevels().addAll(Arrays.asList(levels));
        return this;
    }

    public ParameterMockBuilder withEntries(ParameterEntry... entries) {
        parameter.getEntries().addAll(Arrays.asList(entries));
        return this;
    }
}
