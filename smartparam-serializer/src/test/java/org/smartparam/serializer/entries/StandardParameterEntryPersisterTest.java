package org.smartparam.serializer.entries;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.test.mock.LevelMock;
import org.smartparam.engine.test.mock.ParameterEntryMock;
import org.smartparam.engine.test.mock.ParameterMockBuilder;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardParameterEntryPersisterTest {

    @Test
    public void testPersisting() {
        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
                .multivalue(true).nullable(false).withInputLevels(3)
                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
                new LevelMock("creator2", "type", true, "matcher2"),
                new LevelMock("creator3", "type", true, "matcher3")).withEntries().get();

        StandardParameterEntryPersister entryPersister = new StandardParameterEntryPersister(parameter);
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        entries.add(new ParameterEntryMock("v1_1", "v1_2", "v1_3"));
        entries.add(new ParameterEntryMock("v2_1", "v2_2", "v2_3"));

        entryPersister.writeBatch(entries);

        assertEquals(2, parameter.getEntries().size());
    }
}
