package org.smartparam.engine.config;

import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.annotations.scanner.TypeScanner;
import org.smartparam.engine.core.repository.TypeScanningRepository;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class TypeScannerInitializerTest {

    private TypeScannerInitializer typeScannerInitializer;

    @Before
    public void setUp() {
        typeScannerInitializer = new TypeScannerInitializer();
    }

    @Test
    public void shouldRunInitializationMethodOnProvidedObject() {
        // given
        TypeScanningRepository repository = mock(TypeScanningRepository.class);

        // when
        typeScannerInitializer.initializeObject(repository);

        // then
        verify(repository, times(1)).scanAnnotations(any(TypeScanner.class));
    }

    @Test
    public void shouldAcceptTypeScanningRepositoryImplementations() {
        // given
        TypeScanningRepository repository = mock(TypeScanningRepository.class);

        // when
        boolean accepted = typeScannerInitializer.acceptsObject(repository);

        // then
        assertThat(accepted).isTrue();
    }

    @Test
    public void shouldNotAcceptOtherObjectsThanTypeScanningRepositoryImplementations() {
        // given
        Object someObject = new Object();

        // when
        boolean accepted = typeScannerInitializer.acceptsObject(someObject);

        // then
        assertThat(accepted).isFalse();
    }
}