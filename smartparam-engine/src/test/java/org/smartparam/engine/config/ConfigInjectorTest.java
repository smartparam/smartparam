package org.smartparam.engine.config;

import org.junit.Test;
import org.smartparam.engine.core.cache.FunctionCache;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.repository.SmartTypeRepository;
import org.smartparam.engine.core.service.SmartFunctionProvider;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.test.bean.config.DummyPreparableBean;
import static org.smartparam.engine.test.builder.SmartFunctionProviderTestBuilder.*;
import static org.smartparam.engine.test.builder.SmartTypeRepositoryTestBuilder.*;
import static org.smartparam.engine.test.builder.SmartParamConfigTestBuilder.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigInjectorTest {

    @Test
    public void shouldRegisterMapContentsAsRepositoryItems() {
        // given
        SmartParamConfig config = config().withAnnotationScan()
                .withType("type1", mock(Type.class))
                .withType("type2", mock(Type.class))
                .build();
        SmartTypeRepository typeRepository = typeRepository().withoutAnnotationScan().build();
        ConfigInjector injector = new ConfigInjector(config);

        // when
        injector.injectConfig(typeRepository);

        // then
        assertThat(typeRepository.registeredItems()).hasSize(2).containsKey("type1").containsKey("type2");
    }

    @Test
    public void shouldInjectObjectFromConfigIntoRootObjectIfSetterAvailable() {
        // given
        FunctionCache functionCache = new MapFunctionCache();
        SmartParamConfig config = config().withAnnotationScan()
                .withFunctionCache(functionCache).build();
        SmartFunctionProvider functionProvider = functionProvider().withAnnotationScan().build();

        ConfigInjector injector = new ConfigInjector(config);

        // when
        injector.injectConfig(functionProvider);

        // then
        assertThat(functionProvider.getFunctionCache()).isSameAs(functionCache);
    }

    @Test
    public void shouldRunPostConstructMethodOnRootObject() {
        // given
        SmartParamConfig config = config().build();
        DummyPreparableBean preparableBean = new DummyPreparableBean();

        ConfigInjector injector = new ConfigInjector(config);

        // when
        injector.injectConfig(preparableBean);

        // then
        assertThat(preparableBean.isPrepared()).isTrue();
    }
}
