package org.smartparam.engine.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.repository.ParamRepository;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamEngineFactoryTest {

    private SmartParamEngineFactory factory;
    
    @Before
    public void initialize() {
        factory = new SmartParamEngineFactory();
    }
    
    @Test
    public void testFactory_emptyConfig() {
        SmartParamConfig config = new SmartParamConfig();
        ParamRepository paramRepository = mock(ParamRepository.class);
        config.setParamRepository(paramRepository);

        ParamEngine engine = factory.getParamEngine(config);

        assertNotNull(engine.getParamPreparer());
        assertNotNull(engine.getParamPreparer().getMatcherRepository());
        assertNotNull(engine.getParamPreparer().getTypeRepository());
        assertNotNull(engine.getParamPreparer().getParamCache());
        assertSame(paramRepository, engine.getParamPreparer().getParamRepository());
        assertNotNull(engine.getFunctionManager());
        assertNotNull(engine.getFunctionManager().getFunctionProvider());
        assertNotNull(engine.getFunctionManager().getFunctionProvider().getFunctionCache());
        assertNotNull(engine.getFunctionManager().getInvokerRepository());

        // populated by scanned defaults - all shouldnt be empty
        assertFalse(engine.getFunctionManager().getInvokerRepository().registeredItems().isEmpty());
        assertFalse(engine.getFunctionManager().getFunctionProvider().registeredItems().isEmpty());
        assertFalse(engine.getParamPreparer().getTypeRepository().registeredItems().isEmpty());
        assertFalse(engine.getParamPreparer().getMatcherRepository().registeredItems().isEmpty());
    }

    @Test
    public void testFactory_withoutAnnotationScanning() {
        SmartParamConfig config = new SmartParamConfig();
        config.setScanAnnotations(false);
        ParamRepository paramRepository = mock(ParamRepository.class);
        config.setParamRepository(paramRepository);

        ParamEngine engine = factory.getParamEngine(config);

        assertNotNull(engine.getParamPreparer());
        assertNotNull(engine.getParamPreparer().getMatcherRepository());
        assertNotNull(engine.getParamPreparer().getTypeRepository());
        assertNotNull(engine.getParamPreparer().getParamCache());
        assertSame(paramRepository, engine.getParamPreparer().getParamRepository());
        assertNotNull(engine.getFunctionManager());
        assertNotNull(engine.getFunctionManager().getFunctionProvider());
        assertNotNull(engine.getFunctionManager().getFunctionProvider().getFunctionCache());
        assertNotNull(engine.getFunctionManager().getInvokerRepository());

        // no scan so they should be empty
        assertTrue(engine.getFunctionManager().getInvokerRepository().registeredItems().isEmpty());
        assertTrue(engine.getFunctionManager().getFunctionProvider().registeredItems().isEmpty());
        assertTrue(engine.getParamPreparer().getTypeRepository().registeredItems().isEmpty());
        assertTrue(engine.getParamPreparer().getMatcherRepository().registeredItems().isEmpty());
    }

}
