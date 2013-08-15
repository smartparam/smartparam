package org.smartparam.engine.test.builder;

import java.util.ArrayList;
import java.util.HashMap;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class SmartParamConfigTestBuilder {

    private ParamEngineConfig config;

    private SmartParamConfigTestBuilder() {
        config = new ParamEngineConfig();
    }

    public static SmartParamConfigTestBuilder config() {
        return new SmartParamConfigTestBuilder();
    }

    public ParamEngineConfig build() {
        return config;
    }

    public SmartParamConfigTestBuilder withRepository(ParamRepository repository) {
        if(config.getParameterRepositories() == null) {
            config.setParameterRepositories(new ArrayList<ParamRepository>());
        }
        config.getParameterRepositories().add(repository);
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
