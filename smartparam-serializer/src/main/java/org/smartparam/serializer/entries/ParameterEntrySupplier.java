package org.smartparam.serializer.entries;

import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntrySupplier {

    boolean hasMore();

    Iterable<ParameterEntry> nextBatch();

}
