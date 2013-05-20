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
    Map<KEY, TYPE> registeredItems();

    /**
     * Insert all items to repository.
     *
     * @param objects items
     */
    void setItems(Map<String, TYPE> objects);

}
