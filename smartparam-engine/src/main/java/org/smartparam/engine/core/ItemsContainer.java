package org.smartparam.engine.core;

import java.util.Map;

/**
 * General item container contract.
 *
 * @param <KEY> object key type
 * @param <TYPE> object type
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ItemsContainer<KEY, TYPE> {

    /**
     * Return registered items.
     *
     * @return registered items
     */
    Map<String, TYPE> registeredItems();

    /**
     * Insert all items to repository.
     *
     * @param objects items
     */
    void registerAll(Map<String, TYPE> objects);

}
