package org.smartparam.engine.core;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface Repository<TYPE> extends ItemsContainer<String, TYPE> {

    void register(String key, TYPE type);

}
