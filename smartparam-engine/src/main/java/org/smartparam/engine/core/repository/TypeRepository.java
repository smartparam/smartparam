package org.smartparam.engine.core.repository;

import org.smartparam.engine.core.Repository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface TypeRepository extends Repository<Type<?>> {

    Type<?> getType(String code);
}
