package org.smartparam.engine.test.builder;

import org.smartparam.engine.core.repository.BasicTypeRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class SmartTypeRepositoryTestBuilder {

    private BasicTypeRepository typeRepository;

    private SmartTypeRepositoryTestBuilder() {
        typeRepository = new BasicTypeRepository();
    }

    public static SmartTypeRepositoryTestBuilder typeRepository() {
        return new SmartTypeRepositoryTestBuilder();
    }

    public BasicTypeRepository build() {
        return typeRepository;
    }

    public SmartTypeRepositoryTestBuilder withType(String name, Type<?> type) {
        typeRepository.register(name, type);
        return this;
    }
}
