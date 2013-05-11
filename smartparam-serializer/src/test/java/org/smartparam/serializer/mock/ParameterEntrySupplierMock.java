package org.smartparam.serializer.mock;

import java.util.LinkedList;
import java.util.List;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.test.mock.ParameterEntryMock;
import org.smartparam.serializer.entries.ParameterEntrySupplier;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ParameterEntrySupplierMock implements ParameterEntrySupplier {

    private int entriesToSupply;

    private int batchSize;

    private int levelCount;

    private int suppliedEntriesCounter = 0;

    private int calledForNextBatchCount = 0;

    public ParameterEntrySupplierMock(int entriesToSupply, int batchSize, int levelCount) {
        this.entriesToSupply = entriesToSupply;
        this.batchSize = batchSize;
        this.levelCount = levelCount;
    }

    public int getCalledForNextBatchCount() {
        return calledForNextBatchCount;
    }

    @Override
    public boolean hasMore() {
        return suppliedEntriesCounter < entriesToSupply - 1;
    }

    @Override
    public Iterable<ParameterEntry> nextBatch() {
        List<ParameterEntry> entries = new LinkedList<ParameterEntry>();

        for (int i = 0; i < batchSize; ++i) {
            entries.add(createEntry(suppliedEntriesCounter));
            suppliedEntriesCounter++;
        }

        calledForNextBatchCount++;
        return entries;
    }

    private ParameterEntry createEntry(int currentIndex) {
        ParameterEntryMock mock = new ParameterEntryMock();

        String[] levels = new String[levelCount];
        for (int i = 0; i < levelCount; ++i) {
            levels[i] = "level_" + currentIndex + "_" + i;
        }
        mock.setLevels(levels);

        return mock;
    }
}
