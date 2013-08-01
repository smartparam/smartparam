package org.smartparam.serializer.config;

import org.junit.Before;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializerTest {

    private JsonParameterConfigSerializer serializer;

    @Before
    public void initialize() {
        serializer = new JsonParameterConfigSerializer();
    }

//    @Test
//    public void testSerialize() {
//        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
//                .multivalue(true).nullable(false).withInputLevels(3)
//                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
//                new LevelMock("creator2", "type", true, "matcher2"),
//                new LevelMock("creator3", "type", true, "matcher3"))
//                .withEntries(new ParameterEntryMock("v1", "v2", "v3")).get();
//
//        String serializedConfig = serializer.serialize(parameter);
//
//        assertNotNull(serializedConfig);
//        assertFalse(serializedConfig.contains("entries"));
//        assertTrue(serializedConfig.contains("levels"));
//        assertTrue(serializedConfig.contains("creator1"));
//    }
}
