package org.smartparam.engine.core.provider;

import org.smartparam.engine.core.type.AbstractType;

/**
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface TypeProvider {

    public void registerType(String code, AbstractType<?> type);

    public AbstractType<?> getType(String code);
}
