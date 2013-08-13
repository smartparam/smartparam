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
        PreparedLevel pl = new PreparedLevel(name, type, true, matcher, levelCreator);

        // then
		assertSame(name, pl.getName());
        assertSame(type, pl.getType());
        assertTrue(pl.isArray());
        assertSame(matcher, pl.getMatcher());
        assertSame(levelCreator, pl.getLevelCreator());
    }
}
