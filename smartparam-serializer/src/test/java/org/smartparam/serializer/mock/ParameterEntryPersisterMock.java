package org.smartparam.serializer.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.serializer.entries.ParameterEntryPersister;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterEntryPersisterMock implements ParameterEntryPersister {

    private List<ParameterEntry> entries = new ArrayList<ParameterEntry>();

    private int batchSize;

    private int writeBatchCallCount = 0;

    public ParameterEntryPersisterMock(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public void writeBatch(Collection<ParameterEntry> entries) {
        this.entries.addAll(entries);
        writeBatchCallCount++;
    }

    @Override
    public int batchSize() {
        return batchSize;
    }

    public List<ParameterEntry> getEntries() {
        return entries;
    }

    public int getWriteBatchCallCount() {
        return writeBatchCallCount;
    }
}
