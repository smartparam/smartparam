package org.smartparam.serializer.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
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
    public List<String> header() {
        return Arrays.asList("some", "stupid", "header");
    }

    @Override
    public boolean hasMore() {
        return suppliedEntriesCounter < entriesToSupply - 1;
    }

    @Override
    public Collection<ParameterEntry> nextBatch() {
        List<ParameterEntry> entries = new LinkedList<ParameterEntry>();

        for (int i = 0; i < batchSize; ++i) {
            entries.add(createEntry(suppliedEntriesCounter));
            suppliedEntriesCounter++;
        }

        calledForNextBatchCount++;
        return entries;
    }

    private ParameterEntry createEntry(int currentIndex) {
        SimpleEditableParameterEntry entry = new SimpleEditableParameterEntry();

        String[] levels = new String[levelCount];
        for (int i = 0; i < levelCount; ++i) {
            levels[i] = "level_" + currentIndex + "_" + i;
        }
        entry.setLevels(levels);

        return entry;
    }
}
