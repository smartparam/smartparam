package org.smartparam.coherence.jdbc.cache;

import org.smartparam.coherence.jdbc.config.JdbcCoherentParamCacheFactory;
import org.smartparam.coherence.jdbc.config.JdbcConfig;
import org.smartparam.coherence.jdbc.DatabaseTest;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.testng.annotations.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;
import static org.smartparam.engine.core.prepared.PreparedParameterTestBuilder.preparedParameter;

public class JdbcCoherentParamCacheIntegrationTest extends DatabaseTest {

    @Test
    public void shouldPropagateParamInvalidationToRelatedCaches() {
        // given
        PreparedParameter someParam = preparedParameter().forParameter(parameter().build()).build();
        JdbcConfig jdbcConfig = get(JdbcConfig.class);
        DataSource dataSource = get(DataSource.class);

        CoherentParamCache originallyInvalidatedCache = new JdbcCoherentParamCacheFactory().createCache(dataSource, jdbcConfig);
        CoherentParamCache relatedCache = new JdbcCoherentParamCacheFactory().createCache(dataSource, jdbcConfig);

        originallyInvalidatedCache.put("someParam", someParam);
        relatedCache.put("someParam", someParam);

        // when
        originallyInvalidatedCache.invalidate("someParam");
        relatedCache.invalidateStaleParams();

        // then
        assertThat(relatedCache.get("someParam")).isNull();
    }

    @Test
    public void shouldInvalidateStaleCacheWhenCallingInvalidateStaleParamsIsDelayed() {
        // given
        PreparedParameter someParam = preparedParameter().forParameter(parameter().build()).build();
        JdbcConfig jdbcConfig = get(JdbcConfig.class);
        DataSource dataSource = get(DataSource.class);

        CoherentParamCache staleCache = new JdbcCoherentParamCacheFactory().createCache(dataSource, jdbcConfig);
        CoherentParamCache freshCache = new JdbcCoherentParamCacheFactory().createCache(dataSource, jdbcConfig);

        staleCache.put("someParam", someParam);
        staleCache.invalidate("someParam");
        staleCache.invalidate("someParam");
        staleCache.put("someParam", someParam);

        freshCache.put("someParam", someParam);
        freshCache.invalidate("someParam");
        freshCache.put("someParam", someParam);

        // when
        staleCache.invalidateStaleParams();
        freshCache.invalidateStaleParams();

        // then
        assertThat(staleCache.get("someParam")).isNull();
        assertThat(freshCache.get("someParam")).isNotNull();
    }

}
