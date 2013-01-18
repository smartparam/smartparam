package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.engine.PreparedLevel;
import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.model.Function;
import org.smartparam.engine.types.integer.IntegerType;

/**
 * @author Przemek Hertel
 */
public class PreparedLevelTest {

    IntegerType type = new IntegerType();
    Matcher matcher = mock(Matcher.class);
    Function levelCreator = new Function();

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
