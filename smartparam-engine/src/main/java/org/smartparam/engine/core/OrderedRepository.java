package org.smartparam.engine.core;

import java.util.Map;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface OrderedRepository<TYPE> {

    void register(String type, int index, TYPE object);

    Map<RepositoryObjectKey, TYPE> registeredItems();

    void setItems(Map<String, TYPE> items);
}
