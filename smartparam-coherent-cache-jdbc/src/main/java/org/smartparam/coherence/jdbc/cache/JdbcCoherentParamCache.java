package org.smartparam.coherence.jdbc.cache;

import org.smartparam.coherence.jdbc.repository.ParamVersionRepository;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.prepared.PreparedParameter;

import java.util.HashMap;
import java.util.Map;

public class JdbcCoherentParamCache implements CoherentParamCache {

    private PreparedParamCache decoratedCache;

    private ParamVersionRepository versionRepository;

    private Map<String, Long> localVersions = new HashMap<String, Long>();

    public JdbcCoherentParamCache(PreparedParamCache decoratedCache, ParamVersionRepository versionRepository) {
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
        decoratedCache.invalidate();
    }

    @Override
    public void invalidate(String paramName) {
        decoratedCache.invalidate(paramName);
        Long newVersion = versionRepository.incrementVersion(paramName);
        localVersions.put(paramName, newVersion);
    }

    @Override
    public void invalidateStaleParams() {
        Map<String, Long> versions = versionRepository.versionOfAllParams();
        for (String param : versions.keySet()) {
            if (!localVersions.containsKey(param) || localVersions.get(param) < versions.get(param)) {
                invalidateWithoutNotifying(param);
                localVersions.put(param, versions.get(param));
            }
        }
    }

    private void invalidateWithoutNotifying(String paramName) {
        decoratedCache.invalidate(paramName);
    }
}
