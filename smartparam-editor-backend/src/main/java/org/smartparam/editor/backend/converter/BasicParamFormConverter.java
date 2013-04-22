package org.smartparam.editor.backend.converter;

import java.util.LinkedList;
import java.util.List;
import org.smartparam.editor.backend.form.ParameterForm;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BasicParamFormConverter implements ParamFormConverter {

    public ParameterForm createForm(Parameter parameter) {
        ParameterForm parameterForm = new ParameterForm();

        parameterForm.setName(parameter.getName());
        parameterForm.setLabel(parameter.getLabel());

        parameterForm.setInputLevels(parameter.getInputLevels());
        parameterForm.setMutlivalue(parameter.isMultivalue());
        parameterForm.setNullable(parameter.isNullable());
        parameterForm.setCacheable(parameter.isCacheable());

        parameterForm.setArray(parameter.isArray());
        parameterForm.setArraySeparator(parameter.getArraySeparator());

        return parameterForm;
    }

    public List<ParameterForm> createList(List<Parameter> parameters) {
        List<ParameterForm> parameterForms = new LinkedList<ParameterForm>();
        for(Parameter parameter : parameters) {
            parameterForms.add(createForm(parameter));
        }

        return parameterForms;
    }

}
