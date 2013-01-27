package org.smartparam.demo.controller;

import org.smartparam.demo.model.DemoModelObject;
import org.smartparam.demo.param.DemoParamContext;
import org.smartparam.demo.param.DemoParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Landing page controller.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private DemoParamService demoParamService;

    @RequestMapping("index")
    @ModelAttribute("output")
    public String view() {
        DemoParamContext context = new DemoParamContext();
        context.setDemoModelObject(new DemoModelObject("input1"));

        return demoParamService.get("sample", context).getString();
    }
}
