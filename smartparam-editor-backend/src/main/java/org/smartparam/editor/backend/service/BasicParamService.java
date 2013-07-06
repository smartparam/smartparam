package org.smartparam.editor.backend.service;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.smartparam.editor.backend.converter.BasicParamFormConverter;
import org.smartparam.editor.backend.converter.ParamFormConverter;
import org.smartparam.editor.backend.form.ParameterForm;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BasicParamService implements ParamService {

    private ParamRepository paramRepository;

    private ParamFormConverter paramFormConverter = null;

    @PostConstruct
    public void initialize() {
        if (paramFormConverter == null) {
            paramFormConverter = new BasicParamFormConverter();
        }
    }

    public ParameterForm fetchParam(String parameterName) {
        Parameter parameter = paramRepository.load(parameterName);
        ParameterForm form = null;
        if (parameter != null) {
            form = paramFormConverter.createForm(parameter);
        }
        return form;
    }

    public List<ParameterForm> listParams() {
        List<Parameter> parameters = new LinkedList<Parameter>();
        parameters.add(paramRepository.load("sample"));

        return paramFormConverter.createList(parameters);
    }

    public void saveParam(ParameterForm parameterForm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setParamEngine(ParamEngine paramEngine) {
        
    }

    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }
}
