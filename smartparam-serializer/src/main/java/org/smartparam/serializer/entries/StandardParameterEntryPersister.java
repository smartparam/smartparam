package org.smartparam.serializer.entries;

import java.util.Collection;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardParameterEntryPersister implements ParameterEntryPersister {

    private static final int DEFAULT_BATCH_SIZE = 100;

    private Parameter parameter;

    public StandardParameterEntryPersister(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public void writeBatch(Collection<ParameterEntry> entries) {
        parameter.getEntries().addAll(entries);
    }

    @Override
    public int batchSize() {
        return DEFAULT_BATCH_SIZE;
    }
}
