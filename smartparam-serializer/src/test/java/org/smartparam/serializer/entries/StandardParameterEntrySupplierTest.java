package org.smartparam.serializer.entries;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardParameterEntrySupplierTest {

//    @Test
//    public void testSupplying() {
//        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
//                .multivalue(true).nullable(false).withInputLevels(3)
//                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
//                new LevelMock("creator2", "type", true, "matcher2"),
//                new LevelMock("creator3", "type", true, "matcher3")).withEntries(
//                new ParameterEntryMock("v1_1", "v1_2", "v1_3"),
//                new ParameterEntryMock("v2_1", "v2_2", "v2_3")).get();
//
//        StandardParameterEntrySupplier supplier = new StandardParameterEntrySupplier(parameter);
//
//        List<String> header = supplier.header();
//        assertEquals(3, header.size());
//        assertEquals("level0", header.get(0));
//
//        assertTrue(supplier.hasMore());
//        List<ParameterEntry> parameterEntries = new ArrayList<ParameterEntry>();
//        while(supplier.hasMore()) {
//            parameterEntries.addAll(supplier.nextBatch());
//        }
//
//        assertEquals(2, parameterEntries.size());
//    }
}
