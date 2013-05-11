package org.smartparam.serializer.entries;

import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntryPersister {

    void writeBatch(Iterable<ParameterEntry> entries);

    int batchSize();
}
