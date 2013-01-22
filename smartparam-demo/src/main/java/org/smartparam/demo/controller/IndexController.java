package org.smartparam.demo.controller;

import org.springframework.stereotype.Controller;
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

    @RequestMapping("index")
    public void view() {

    }
    
}
