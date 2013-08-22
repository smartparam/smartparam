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
package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.types.integer.IntegerType;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import static org.mockito.Mockito.mock;

/**
 * @author Przemek Hertel
 */
public class PreparedLevelTest {

    private IntegerType type = new IntegerType();

    private Matcher matcher = mock(Matcher.class);

    private Function levelCreator = mock(Function.class);

    @Test
    public void testConstructor() {

		// given
		String name = "levelName";

        // when
        PreparedLevel pl = new PreparedLevel(name, true, type, matcher, levelCreator);

        // then
		assertSame(name, pl.getName());
        assertSame(type, pl.getType());
        assertTrue(pl.isArray());
        assertSame(matcher, pl.getMatcher());
        assertSame(levelCreator, pl.getLevelCreator());
    }
}
