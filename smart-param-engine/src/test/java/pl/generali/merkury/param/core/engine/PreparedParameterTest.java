package pl.generali.merkury.param.core.engine;

import org.junit.*;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.index.LevelIndex;
import pl.generali.merkury.param.core.type.AbstractType;
import pl.generali.merkury.param.types.integer.IntegerType;
import pl.generali.merkury.param.types.number.NumberType;

/**
 * @author Przemek Hertel
 */
public class PreparedParameterTest {

    PreparedParameter pp;

    @Before
    public void init() {
        pp = new PreparedParameter();
    }

    @Test
    public void testId() {

        // konfiguracja testu
        int expectedId = 123456;

        // test
        pp.setId(expectedId);
        int result = pp.getId();

        // sprawdzenie wynikow testu
        assertEquals(expectedId, result);
    }

    @Test
    public void testName() {

        // konfiguracja testu
        String name = "par.name";

        // test
        pp.setName(name);
        String result = pp.getName();

        // sprawdzenie wynikow testu
        assertEquals(name, result);
    }

    @Test
    public void testType() {

        // konfiguracja testu
        AbstractType<?> type = new NumberType();

        // test
        pp.setType(type);
        AbstractType<?> result = pp.getType();

        // sprawdzenie wynikow testu
        assertSame(type, result);
    }

    @Test
    public void testLevels() {

        // zaleznosci
        PreparedLevel l1 = new PreparedLevel(new IntegerType(), false, null, null);
        PreparedLevel l2 = new PreparedLevel(new NumberType(), true, null, null);

        // konfiguracja testu
        PreparedLevel[] levels = {l1, l2};

        // test
        pp.setLevels(levels);
        PreparedLevel[] result = pp.getLevels();

        // sprawdzenie wynikow testu
        assertArrayEquals(result, levels);
    }

    @Test
    public void testIndex() {

        // konfiguracja testu
        LevelIndex<PreparedEntry> index = new LevelIndex<PreparedEntry>(3);

        // test
        pp.setIndex(index);
        LevelIndex<PreparedEntry> result = pp.getIndex();

        // sprawdzenie wynikow testu
        assertSame(index, result);

    }

    @Test
    public void testMultivalue() {

        // test
        pp.setMultivalue(true);

        // sprawdzenie wynikow testu
        assertTrue(pp.isMultivalue());
    }

    @Test
    public void testInputLevelsCount() {

        // test 1
        pp.setInputLevelsCount(3);
        assertEquals(0, pp.getInputLevelsCount());

        // test 2
        pp.setMultivalue(true);
        assertEquals(3, pp.getInputLevelsCount());
    }

    @Test
    public void testNullable() {

        // test 1
        assertFalse(pp.isNullable());
        assertTrue(pp.isNotNull());

        // test 2
        pp.setNullable(true);
        assertTrue(pp.isNullable());
        assertFalse(pp.isNotNull());
    }

    @Test
    public void testArray() {

        // test
        pp.setArray(true);

        // sprawdzenie wynikow testu
        assertTrue(pp.isArray());
    }

    @Test
    public void testArraySeparator() {

        // test
        pp.setArraySeparator('/');

        // sprawdzenie wynikow testu
        assertEquals('/', pp.getArraySeparator());
    }
}
