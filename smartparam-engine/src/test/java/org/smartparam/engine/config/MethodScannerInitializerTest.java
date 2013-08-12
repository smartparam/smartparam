package org.smartparam.engine.config;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.annotations.scanner.MethodScanner;
import org.smartparam.engine.core.repository.MethodScanningRepository;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class MethodScannerInitializerTest {

    private MethodScannerInitializer methodScannerInitializer;

    @BeforeMethod
    public void setUp() {
        methodScannerInitializer = new MethodScannerInitializer();
    }

    @Test
    public void shouldRunInitializationMethodOnProvidedObject() {
        // given
        MethodScanningRepository repository = mock(MethodScanningRepository.class);

        // when
        methodScannerInitializer.initializeObject(repository);

        // then
        verify(repository, times(1)).scanMethods(any(MethodScanner.class));
    }

    @Test
    public void shouldAcceptTypeScanningRepositoryImplementations() {
        // given
        MethodScanningRepository repository = mock(MethodScanningRepository.class);

        // when
        boolean accepted = methodScannerInitializer.acceptsObject(repository);

        // then
        assertThat(accepted).isTrue();
    }

    @Test
    public void shouldNotAcceptOtherObjectsThanTypeScanningRepositoryImplementations() {
        // given
        Object someObject = new Object();

        // when
        boolean accepted = methodScannerInitializer.acceptsObject(someObject);

        // then
        assertThat(accepted).isFalse();
    }
}