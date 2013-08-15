package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel
 */
public interface TypeScanner {

    <REGISTERED_OBJECT> Map<RepositoryObjectKey, REGISTERED_OBJECT> scanTypes(Class<? extends Annotation> annotationType);

    <REGISTERED_OBJECT> List<REGISTERED_OBJECT> scanTypesWithoutName(Class<? extends Annotation> annotationType);
}
