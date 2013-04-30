package org.smartparam.engine.core;

import java.util.Map;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface Repository<TYPE> {

    void register(String key, TYPE type);

    Map<String, TYPE> registeredItems();

    void setItems(Map<String, TYPE> objects);
}
