package org.smartparam.editor.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Controller
@RequestMapping(IndexController.REQUEST_MAPPING)
public class IndexController {

    public static final String REQUEST_MAPPING = "/";

    @RequestMapping("")
    public String index() {
        return "index";
    }
}
