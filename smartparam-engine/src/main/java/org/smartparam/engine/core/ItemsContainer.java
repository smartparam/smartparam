package org.smartparam.engine.core;

import java.util.Map;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ItemsContainer<KEY, TYPE> {

    Map<KEY, TYPE> registeredItems();

    void setItems(Map<String, TYPE> objects);

}
