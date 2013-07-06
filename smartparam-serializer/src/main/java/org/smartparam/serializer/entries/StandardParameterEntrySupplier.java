package org.smartparam.serializer.entries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardParameterEntrySupplier implements ParameterEntrySupplier {

    private Parameter parameter;

    private boolean allEntriesReturned = false;

    public StandardParameterEntrySupplier(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public List<String> header() {
        List<String> header = new ArrayList<String>();

        String levelHeader;
        int levelIndex = 0;
        for (Level level : parameter.getLevels()) {
            if (level instanceof EditableLevel) {
                levelHeader = ((EditableLevel) level).getName();
            } else {
                levelHeader = "level" + levelIndex;
            }
            header.add(levelHeader);
        }

        return header;
    }

    @Override
    public boolean hasMore() {
        return !allEntriesReturned;
    }

    @Override
    public Collection<ParameterEntry> nextBatch() {
        allEntriesReturned = true;
        return parameter.getEntries();
    }
}
