/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.repository.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.smartparam.engine.core.parameter.level.LevelKey;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryParameterTest {

    @Test
    public void shouldReturnLevelWhenLookingForLevelByKey() {
        // given
        InMemoryLevel levelToFind = new InMemoryLevel();
        InMemoryParameter parameter = new InMemoryParameter();
        parameter.addLevel(new InMemoryLevel());
        parameter.addLevel(levelToFind);

        // when
        InMemoryLevel foundLevel = parameter.findLevel(levelToFind.getRawKey());

        // then
        assertThat(foundLevel).isSameAs(levelToFind);
    }

    @Test
    public void shouldReturnNullIfNoLevelWithKeyFound() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();

        // when
        InMemoryLevel foundLevel = parameter.findLevel(new InMemoryLevelKey());

        // then
        assertThat(foundLevel).isNull();
    }

    @Test
    public void shouldRemoveLevelWhenRemovingByLevelKey() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();
        InMemoryLevelKey levelKeyToRemove = parameter.addLevel(new InMemoryLevel());

        // when
        parameter.removeLevel(levelKeyToRemove);

        // then
        assertThat(parameter.findLevel(levelKeyToRemove)).isNull();
    }

    @Test
    public void shouldSetNewOrderOfLevels() {
        // given
        List<LevelKey> newOrder = new ArrayList<LevelKey>(3);
        InMemoryParameter parameter = new InMemoryParameter();
        newOrder.add(parameter.addLevel(new InMemoryLevel()));
        newOrder.add(parameter.addLevel(new InMemoryLevel()));
        newOrder.add(parameter.addLevel(new InMemoryLevel()));

        Collections.shuffle(newOrder);

        // when
        parameter.reorderLevels(newOrder);

        // then
        assertThat(parameter.getLevels()).isSortedAccordingTo(new LevelOrderComparator(newOrder));
    }

    @Test
    public void shouldReturnParameterEntryWhenAskingUsingParameterEntryKey() {
        // given
        InMemoryParameterEntry entryToFind = new InMemoryParameterEntry();
        InMemoryParameter parameter = new InMemoryParameter();
        parameter.addEntry(new InMemoryParameterEntry());
        parameter.addEntry(entryToFind);

        // when
        InMemoryParameterEntry foundEntry = parameter.findEntry(entryToFind.getRawKey());

        // then
        assertThat(foundEntry).isSameAs(entryToFind);
    }

    @Test
    public void shouldRemoveEntryWhenRemovingByEntryKey() {
        // given
        InMemoryParameter parameter = new InMemoryParameter();
        InMemoryParameterEntryKey entryKeyToRemove = parameter.addEntry(new InMemoryParameterEntry());

        // when
        parameter.removeEntry(entryKeyToRemove);

        // then
        assertThat(parameter.findEntry(entryKeyToRemove)).isNull();
    }
}
