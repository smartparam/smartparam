package org.smartparam.engine.core.repository;

import org.smartparam.engine.core.assembler.AssemblerMethod;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface AssemblerProvider {

    void registerAssemblerOwner(Object owner);

    AssemblerMethod findAssembler(Class<?> source, Class<?> target);

    void logAssemblers();
}
