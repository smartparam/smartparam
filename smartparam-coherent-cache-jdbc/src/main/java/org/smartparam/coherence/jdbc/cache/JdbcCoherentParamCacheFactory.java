package org.smartparam.coherence.jdbc.cache;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.coherence.jdbc.repository.JdbcParamVersionRepository;
import org.smartparam.engine.cache.MapPreparedParamCache;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.engine.core.prepared.PreparedParamCache;

import javax.sql.DataSource;

public class JdbcCoherentParamCacheFactory {

    public JdbcCoherentParamCache createCache(DataSource dataSource, JdbcConfig config) {
        return createCache(new JdbcCoherentParamCacheConfig(dataSource, config), new MapPreparedParamCache());
    }

    public JdbcCoherentParamCache createCache(DataSource dataSource, JdbcConfig config, MapPreparedParamCache decoratedCache) {
        return createCache(new JdbcCoherentParamCacheConfig(dataSource, config), decoratedCache);
    }

    public JdbcCoherentParamCache createCache(JdbcCoherentParamCacheConfig config, PreparedParamCache decoratedCache) {
        PicoContainer container = createContainer(config);
        return new JdbcCoherentParamCache(decoratedCache, container.getComponent(JdbcParamVersionRepository.class));
    }

    public PicoContainer createContainer(JdbcCoherentParamCacheConfig config) {
        MutablePicoContainer container = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(container, JdbcParamVersionRepository.class,
                                                config.getConfiguration(), config.getConfiguration().dialect(), config.getDataSource());
        PicoContainerUtil.injectImplementations(container, config.getComponents());

        return container;
    }
}
