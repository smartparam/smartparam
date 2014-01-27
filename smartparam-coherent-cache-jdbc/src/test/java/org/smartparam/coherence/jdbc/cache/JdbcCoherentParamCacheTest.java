package org.smartparam.coherence.jdbc.cache;

import org.smartparam.coherence.jdbc.repository.ParamVersionRepository;
import org.smartparam.engine.cache.MapPreparedParamCache;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;
import static org.smartparam.engine.core.prepared.PreparedParameterTestBuilder.preparedParameter;

public class JdbcCoherentParamCacheTest {

    private ParamVersionRepository versionRepository;

    private CoherentParamCache coherentCache;

    @BeforeMethod
    public void setUp() {
        versionRepository = mock(ParamVersionRepository.class);
        coherentCache = new JdbcCoherentParamCache(new MapPreparedParamCache(), versionRepository);
    }

    @Test
    public void shouldProvideParametersPutIntoCache() {
        // given
        PreparedParameter someParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("someParam", someParam);

        // when
        PreparedParameter provided = coherentCache.get("someParam");

        // then
        assertThat(provided).isEqualTo(someParam);
    }

    @Test
    public void shouldRemoveParameterFromCacheAfterInvalidate() {
        // given
        PreparedParameter someParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("someParam", someParam);
        coherentCache.invalidate("someParam");

        // when
        PreparedParameter provided = coherentCache.get("someParam");

        // then
        assertThat(provided).isNull();
    }

    @Test
    public void shouldInvalidateAllParams() {
        // given
        PreparedParameter someParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("someParam", someParam);
        coherentCache.put("otherParam", someParam);

        // when
        coherentCache.invalidate();

        // then
        assertThat(coherentCache.cachedParameterNames()).isEmpty();
        verify(versionRepository).incrementVersion("someParam");
        verify(versionRepository).incrementVersion("otherParam");
    }

    @Test
    public void shouldNotifyVersionRepositoryAboutParamBeingInvalidated() {
        // given
        PreparedParameter someParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("someParam", someParam);

        // when
        coherentCache.invalidate("someParam");

        // then
        verify(versionRepository).incrementVersion("someParam");
    }

    @Test
    public void shouldInvalidateStaleParametersThatHaveNeverBeenInvalidatedBefore() {
        // given
        PreparedParameter staleParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("staleParam", staleParam);
        when(versionRepository.versionOfAllParams()).thenReturn(singletonMap("staleParam", 1L));

        // when
        coherentCache.invalidateStaleParams();

        // then
        assertThat(coherentCache.get("staleParam")).isNull();
        verify(versionRepository, never()).incrementVersion(anyString());
    }

    @Test
    public void shouldInvalidateStaleParametersThatHaveBeenInvalidatedBefore() {
        // given
        PreparedParameter staleParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("staleParam", staleParam);

        when(versionRepository.incrementVersion("staleParam")).thenReturn(2L);
        coherentCache.invalidate("staleParam");

        coherentCache.put("staleParam", staleParam);
        when(versionRepository.versionOfAllParams()).thenReturn(singletonMap("staleParam", 3L));

        // when
        coherentCache.invalidateStaleParams();

        // then
        assertThat(coherentCache.get("staleParam")).isNull();
        verify(versionRepository, times(1)).incrementVersion(anyString()); // only for the invalidation in "given" clause
    }

    @Test
    public void shouldNotInvalidateParametersThatAreUpToDateWhenInvalidatingStaleParameters() {
        // given
        PreparedParameter staleParam = preparedParameter().forParameter(parameter().build()).build();
        coherentCache.put("staleParam", staleParam);

        when(versionRepository.incrementVersion("staleParam")).thenReturn(2L);
        coherentCache.invalidate("staleParam");

        coherentCache.put("staleParam", staleParam);
        when(versionRepository.versionOfAllParams()).thenReturn(singletonMap("staleParam", 2L));

        // when
        coherentCache.invalidateStaleParams();

        // then
        assertThat(coherentCache.get("staleParam")).isNotNull();
        verify(versionRepository, times(1)).incrementVersion(anyString()); // only for the invalidation in "given" clause
    }


}
