package pl.generali.merkury.param.core.engine;

import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.index.Matcher;
import pl.generali.merkury.param.model.Function;
import pl.generali.merkury.param.types.integer.IntegerType;

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
