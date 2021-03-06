/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.serializer.entries;

/**
 *
 * @author Adam Dubiel
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
