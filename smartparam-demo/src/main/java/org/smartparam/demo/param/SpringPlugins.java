
package org.smartparam.demo.param;

import org.smartparam.spring.function.SpringPlugin;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Component
public class SpringPlugins {

    @SpringPlugin("spring.sample")
    public void sampleSpringFunction() {
        
    }

}
