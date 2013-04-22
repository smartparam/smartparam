
package org.smartparam.editor.backend.service;

import java.util.List;
import org.smartparam.editor.backend.form.ParameterForm;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParamService {
    
    ParameterForm fetchParam(String parameterName);

    List<ParameterForm> listParams();

    void saveParam(ParameterForm parameterForm);
}
