package org.smartparam.serializer.entries;

import java.util.Collection;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntryPersister {

    void writeBatch(Collection<ParameterEntry> entries);

    int batchSize();
}
