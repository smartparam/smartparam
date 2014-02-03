package org.smartparam.coherence.jdbc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.coherence.jdbc.repository.JdbcParamVersionRepository;
import org.smartparam.engine.config.initialization.InitializableComponent;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.prepared.PreparedParameter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JdbcCoherentParamCache implements CoherentParamCache, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCoherentParamCache.class);

    private final PreparedParamCache decoratedCache;

    private final JdbcParamVersionRepository versionRepository;

    private final Map<String, Long> localVersions = new HashMap<String, Long>();

    public JdbcCoherentParamCache(PreparedParamCache decoratedCache, JdbcParamVersionRepository versionRepository) {
        this.decoratedCache = decoratedCache;
        this.versionRepository = versionRepository;
    }

    @Override
    public void put(String paramName, PreparedParameter pp) {
        decoratedCache.put(paramName, pp);
    }

    @Override
    public PreparedParameter get(String paramName) {
        return decoratedCache.get(paramName);
    }

    @Override
    public void invalidate() {
        for (String cachedParamName : cachedParameterNames()) {
            invalidate(cachedParamName);
        }
    }

    @Override
    public void invalidate(String paramName) {
        decoratedCache.invalidate(paramName);
        Long newVersion = versionRepository.incrementVersion(paramName);
        logger.debug("Invalidated {}. Local version was {}, new version is {}.",
                     paramName, localVersions.get(paramName), newVersion);
        localVersions.put(paramName, newVersion);
    }

    @Override
    public void invalidateStaleParams() {
        Map<String, Long> versions = versionRepository.versionOfAllParams();
        for (String param : versions.keySet()) {
            if (!localVersions.containsKey(param) || localVersions.get(param) < versions.get(param)) {
                invalidateWithoutNotifying(param);
                logger.debug("Invalidated stale {}. Local version was {}, new version is {}.",
                             param, localVersions.get(param), versions.get(param));
                localVersions.put(param, versions.get(param));
            }
        }
    }

    private void invalidateWithoutNotifying(String paramName) {
        decoratedCache.invalidate(paramName);
    }

    @Override
    public Collection<String> cachedParameterNames() {
        return decoratedCache.cachedParameterNames();
    }

    @Override
    public void initialize() {
        versionRepository.initialize();
    }
}
