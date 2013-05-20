package org.smartparam.engine.core;

import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 * Contract for repository, that guarantees order of items. Ordering is based on
 * provided indexes, not registration order.
 *
 * @param <TYPE> type of objects in repository
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface OrderedRepository<TYPE> extends ItemsContainer<RepositoryObjectKey, TYPE> {

    /**
     * Register object in repository of type under given index.
     *
     * @param type object type (key)
     * @param index order number (simply: priority)
     * @param object object
     */
    void register(String type, int index, TYPE object);
}
