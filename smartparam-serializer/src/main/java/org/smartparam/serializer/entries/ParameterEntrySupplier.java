package org.smartparam.serializer.entries;

import java.util.Collection;
import java.util.List;
import org.smartparam.engine.model.ParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParameterEntrySupplier {

    List<String> header();

    boolean hasMore();

    Collection<ParameterEntry> nextBatch();

}
