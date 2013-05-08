package org.smartparam.engine.core;

import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface OrderedRepository<TYPE> extends ItemsContainer<RepositoryObjectKey, TYPE> {

    void register(String type, int index, TYPE object);
}
