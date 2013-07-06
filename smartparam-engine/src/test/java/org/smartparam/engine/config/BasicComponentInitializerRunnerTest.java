package org.smartparam.engine.config;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class BasicComponentInitializerRunnerTest {

    private BasicComponentInitializerRunner basicComponentInitializerRunner;

    @Before
    public void setUp() {
    }

    @Test
    public void shouldRunAllContextInitializersAcceptingObject() {
        // given
        Object initialiedObject = new Object();
        ComponentInitializer initializer = mock(ComponentInitializer.class);
        when(initializer.acceptsObject(initialiedObject)).thenReturn(true);
        basicComponentInitializerRunner = new BasicComponentInitializerRunner(Arrays.asList(initializer));

        // when
        basicComponentInitializerRunner.runInitializers(initialiedObject);

        // then
        verify(initializer, times(1)).initializeObject(initialiedObject);
    }

    @Test
    public void shouldNotRunContextInitializerThatDoesNotAcceptObject() {
        // given
        Object initialiedObject = new Object();
        ComponentInitializer initializer = mock(ComponentInitializer.class);
        when(initializer.acceptsObject(initialiedObject)).thenReturn(false);
        basicComponentInitializerRunner = new BasicComponentInitializerRunner(Arrays.asList(initializer));

        // when
        basicComponentInitializerRunner.runInitializers(initialiedObject);

        // then
        verify(initializer, never()).initializeObject(initialiedObject);
    }

    @Test
    public void shouldIterateThroughGivenListAndTryToInitializeObjects() {
        // given
        ComponentInitializer initializer = mock(ComponentInitializer.class);
        when(initializer.acceptsObject(anyObject())).thenReturn(true);
        basicComponentInitializerRunner = new BasicComponentInitializerRunner(Arrays.asList(initializer));

        // when
        basicComponentInitializerRunner.runInitializersOnList(Arrays.asList(new Object(), new Object()));

        // then
        verify(initializer, times(2)).initializeObject(anyObject());
    }
}