package org.smartparam.editor.backend.converter;

import java.util.List;
import org.smartparam.editor.backend.form.ParameterForm;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParamFormConverter {

    ParameterForm createForm(Parameter parameter);

    List<ParameterForm> createList(List<Parameter> parameters);
}
