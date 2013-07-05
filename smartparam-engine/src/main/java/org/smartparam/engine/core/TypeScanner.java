package org.smartparam.engine.core;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface TypeScanner {

    <REGISTERED_OBJECT> Map<RepositoryObjectKey, REGISTERED_OBJECT> scanTypes(Class<? extends Annotation> annotationType);
}
