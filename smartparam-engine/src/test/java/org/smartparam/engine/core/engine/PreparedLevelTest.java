package org.smartparam.engine.core.engine;

import org.junit.*;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.model.Function;
import org.smartparam.engine.types.integer.IntegerType;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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

        // testowany obiekt
        PreparedLevel pl = new PreparedLevel(type, true, matcher, levelCreator);

        // weryfikacja
        assertSame(type, pl.getType());
        assertTrue(pl.isArray());
        assertSame(matcher, pl.getMatcher());
        assertSame(levelCreator, pl.getLevelCreator());
    }
}
