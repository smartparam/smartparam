
package org.smartparam.engine.test.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.smartparam.engine.annotations.SmartParamObjectInstance;
import org.smartparam.engine.annotations.SmartParamSortable;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SmartParamSortable
public @interface SmartParamDummyWithoutOrder {

    String value();

    String[] values() default {};

    SmartParamObjectInstance[] instances() default {};

}
