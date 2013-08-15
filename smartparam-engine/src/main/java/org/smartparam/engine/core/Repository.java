package org.smartparam.engine.core;

/**
 * Contract for repository - it can be used to register objects
 * of specific type under string key.
 *
 * @param <TYPE> type of objects kept in repository
 * @author Adam Dubiel
 */
public interface Repository<TYPE> extends ItemsContainer<String, TYPE> {

    /**
     * Register object in repository under given key.
     *
     * @param key object key
     * @param type object
     */
    void register(String key, TYPE type);

}
