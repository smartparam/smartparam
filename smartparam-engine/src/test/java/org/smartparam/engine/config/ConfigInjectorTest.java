package org.smartparam.engine.config;

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.SmartTypeRepository;
import org.smartparam.engine.core.service.SmartFunctionProvider;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.test.beans.config.DummyPreparableBean;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigInjectorTest {

    @Test
    public void testInjection_map() {
        SmartParamConfig config = new SmartParamConfig();
        config.setScanAnnotations(false);

        HashMap<String, Type<?>> testTypes = new HashMap<String, Type<?>>();
        testTypes.put("type1", mock(Type.class));
        testTypes.put("type2", mock(Type.class));
        config.setTypes(testTypes);

        SmartTypeRepository typeRepository = new SmartTypeRepository();
        typeRepository.setScanAnnotations(false);

        ConfigInjector injector = new ConfigInjector(config);
        injector.injectConfig(typeRepository);

        assertEquals(testTypes.size(), typeRepository.registeredItems().size());
        assertTrue(typeRepository.registeredItems().keySet().containsAll(testTypes.keySet()));
    }

    @Test
    public void testInjection_object() {
        SmartParamConfig config = new SmartParamConfig();
        config.setScanAnnotations(false);

        HashMap<String, FunctionRepository> testRepositories = new HashMap<String, FunctionRepository>();
        testRepositories.put("repository_testInjection_1", mock(FunctionRepository.class));
        testRepositories.put("repository_testInjection_2", mock(FunctionRepository.class));
        config.setFunctionRepositories(testRepositories);

        FunctionCache functionCache = new MapFunctionCache();
        config.setFunctionCache(functionCache);

        SmartFunctionProvider functionProvider = new SmartFunctionProvider();
        functionProvider.setScanAnnotations(false);

        ConfigInjector injector = new ConfigInjector(config);
        injector.injectConfig(functionProvider);

        assertEquals(testRepositories.size(), functionProvider.registeredItems().size());
        assertSame(functionProvider.getFunctionCache(), functionCache);
    }

    @Test
    public void testRunningPostConstruct() {
        SmartParamConfig config = new SmartParamConfig();
        DummyPreparableBean preparableBean = new DummyPreparableBean();

        assertFalse(preparableBean.isPrepared());

        ConfigInjector injector = new ConfigInjector(config);
        injector.injectConfig(preparableBean);

        assertTrue(preparableBean.isPrepared());
    }
}
