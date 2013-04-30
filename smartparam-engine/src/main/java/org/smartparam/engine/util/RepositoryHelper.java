package org.smartparam.engine.util;

import java.util.Map;
import org.smartparam.engine.core.OrderedRepository;
import org.smartparam.engine.core.Repository;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RepositoryHelper {

    public static <T> void registerItems(OrderedRepository<T> repository, Map<String, T> items) {
        int index = 0;
        for (Map.Entry<String, T> itemEntry : items.entrySet()) {
            repository.register(itemEntry.getKey(), index, itemEntry.getValue());
            index++;
        }
    }

    public static <T> void registerItems(Repository<T> repository, Map<String, T> items) {
        for (Map.Entry<String, T> itemEntry : items.entrySet()) {
            repository.register(itemEntry.getKey(), itemEntry.getValue());
        }
    }

}
