package org.smartparam.engine.test.builder;

import java.util.HashMap;
import org.smartparam.engine.config.SmartParamConfig;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamConfigTestBuilder {

    private SmartParamConfig config;

    private SmartParamConfigTestBuilder() {
        config = new SmartParamConfig();
    }

    public static SmartParamConfigTestBuilder config() {
        return new SmartParamConfigTestBuilder();
    }

    public SmartParamConfig build() {
        return config;
    }

    public SmartParamConfigTestBuilder withAnnotationScan() {
        config.setScanAnnotations(true);
        return this;
    }

    public SmartParamConfigTestBuilder withoutAnnotationScan() {
        config.setScanAnnotations(false);
        return this;
    }

    public SmartParamConfigTestBuilder withRepository(ParamRepository repository) {
        config.setParamRepository(repository);
        return this;
    }

    public SmartParamConfigTestBuilder withType(String key, Type<?> type) {
        if (config.getTypes() == null) {
            config.setTypes(new HashMap<String, Type<?>>());
        }
        config.getTypes().put(key, type);
        return this;
    }

    public SmartParamConfigTestBuilder withFunctionCache(FunctionCache functionCache) {
        config.setFunctionCache(functionCache);
        return this;
    }
}
