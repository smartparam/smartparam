package org.smartparam.engine.config;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.repository.ParamRepository;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.SmartParamConfigTestBuilder.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamEngineFactoryTest {

    private SmartParamEngineFactory factory;

    @Before
    public void setUp() {
        factory = new SmartParamEngineFactory();
    }

    @Test
    public void shouldProduceFullParamEngineTree() {
        // given
        SmartParamConfig config = config().withoutAnnotationScan().withRepository(mock(ParamRepository.class)).build();

        // when
        ParamEngine engine = factory.getParamEngine(config);

        // then
        assertThat(engine).hasInitializedTree();
    }

    @Test
    public void shouldProduceFullParamEngineTreeWithScannedRepositoryItems() {
        // given
        SmartParamConfig config = config().withAnnotationScan().withRepository(mock(ParamRepository.class)).build();

        // when
        ParamEngine engine = factory.getParamEngine(config);

        // then
        assertThat(engine).hasInitializedTreeWithScannedItems();
    }
}
