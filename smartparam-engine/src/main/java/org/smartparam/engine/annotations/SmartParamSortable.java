package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks annotation, that has sorting capabilities.
 * Sortable annotation should provide order() method.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartParamSortable {
}
