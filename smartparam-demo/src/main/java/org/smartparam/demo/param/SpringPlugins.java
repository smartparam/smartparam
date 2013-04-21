
package org.smartparam.demo.param;

import org.smartparam.spring.function.SmartParamSpringPlugin;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Component
public class SpringPlugins {

    @SmartParamSpringPlugin("spring.sample")
    public void sampleSpringFunction() {
        
    }

}
