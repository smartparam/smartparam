package org.smartparam.engine.core.engine;

import org.junit.*;
import static org.junit.Assert.*;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.number.NumberType;
import static org.smartparam.engine.test.builder.LevelIndexTestBuilder.levelIndex;

/**
 * @author Przemek Hertel
 */
public class PreparedParameterTest {

    private PreparedParameter pp;

    @Before
    public void init() {
        pp = new PreparedParameter();
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
        Type<?> type = new NumberType();

        // test
        pp.setType(type);
        Type<?> result = pp.getType();

        // sprawdzenie wynikow testu
        assertSame(type, result);
    }

    @Test
    public void testLevels() {

        // zaleznosci
        PreparedLevel l1 = new PreparedLevel("L1", new IntegerType(), false, null, null);
        PreparedLevel l2 = new PreparedLevel("L2", new NumberType(), true, null, null);

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
        LevelIndex<PreparedEntry> index = levelIndex().withLevelCount(3).build();

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
