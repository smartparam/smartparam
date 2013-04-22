package org.smartparam.editor.spring;

import java.util.List;
import org.smartparam.editor.backend.form.ParameterForm;
import org.smartparam.editor.backend.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Controller
@RequestMapping(ParameterController.REQUEST_MAPPING)
public class ParameterController {

    public static final String REQUEST_MAPPING = "/smartparam/param";

    @Autowired
    private ParamService smartParamEditorParamService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public List<ParameterForm> list(@RequestBody Object object) {
        return smartParamEditorParamService.listParams();
    }

    @RequestMapping(value = "{paramName}", method = RequestMethod.GET)
    @ResponseBody
    public ParameterForm fetchParameter(@PathVariable String paramName) {
        return smartParamEditorParamService.fetchParam(paramName);
    }
}
