package org.smartparam.editor.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Controller
@RequestMapping(PartialsController.REQUEST_MAPPING)
public class PartialsController {

    public static final String REQUEST_MAPPING = "/partials";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String fetchPartial(@RequestParam("partial") String partialName) {
        return partialName;
    }
}
