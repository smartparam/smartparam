package org.smartparam.coherence.jdbc.cache;

import org.smartparam.engine.core.prepared.PreparedParamCache;

public interface CoherentParamCache extends PreparedParamCache {

    /**
     * Invalidates parameters that have been invalidated in related caches.
     */
    void invalidateStaleParams();

}
