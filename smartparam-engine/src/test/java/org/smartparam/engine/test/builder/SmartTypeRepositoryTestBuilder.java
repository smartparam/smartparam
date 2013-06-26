package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.repository.SmartTypeRepository;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartTypeRepositoryTestBuilder {

    private SmartTypeRepository typeRepository;

    private SmartTypeRepositoryTestBuilder() {
        typeRepository = new SmartTypeRepository();
    }

    public static SmartTypeRepositoryTestBuilder typeRepository() {
        return new SmartTypeRepositoryTestBuilder();
    }

    public SmartTypeRepository build() {
        return typeRepository;
    }

    public SmartTypeRepositoryTestBuilder withoutAnnotationScan() {
        typeRepository.setScanAnnotations(false);
        return this;
    }
}
