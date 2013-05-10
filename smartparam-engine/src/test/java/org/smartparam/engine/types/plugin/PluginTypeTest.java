package org.smartparam.engine.types.plugin;

import java.util.Date;
import org.junit.Test;
import org.smartparam.engine.test.builder.FunctionMockBuilder;
import org.smartparam.engine.model.function.Function;

import static org.junit.Assert.*;

/**
 * @author Przemek Hertel
 */
public class PluginTypeTest {

    private PluginType type = new PluginType();

    @Test
    public void testEncode() {

        // przypadki testowe
        Object[][] testCases = {
            {new PluginHolder(null), null},
            {new PluginHolder("fun"), "fun"}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            PluginHolder value = (PluginHolder) testCase[0];
            String expectedResult = (String) testCase[1];

            assertEquals(expectedResult, type.encode(value));
        }
    }

    @Test
    public void testDecode() {

        // przypadki testowe
        Object[][] testCases = {
            {"fun.calc.1", new PluginHolder("fun.calc.1")},
            {"  ", new PluginHolder(null)},
            {"", new PluginHolder(null)},
            {null, new PluginHolder(null)}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            PluginHolder expected = (PluginHolder) testCase[1];

            assertEquals(expected, type.decode(text));
        }
    }

    @Test
    public void testConvert() {

        // zaleznosci
        Function f = FunctionMockBuilder.function().withName("f.calc").get();

        // przypadki testowe: [argument (Object)][oczekiwana wartosc holdera (String)]
        Object[][] testCases = {
            {f, "f.calc"},
            {"f.calc.2", "f.calc.2"},
            {"  ", null},
            {"", null},
            {null, null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            String expectedFunctionName = (String) testCase[1];

            assertEquals(expectedFunctionName, type.convert(obj).getString());
            assertEquals(expectedFunctionName, type.convert(obj).getValue());
        }
    }

    @Test
    public void testConvert__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac skonwertowane do NumberHoldera
        Object[] illegals = {new Date(), new int[0], 8L};

        // wykonanie testow, oczekujemy wyjatku
        for (Object obj : illegals) {
            try {
                type.convert(obj);
                fail();
            } catch (RuntimeException e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }

    @Test
    public void testNewArray() {
        PluginHolder[] arr1 = type.newArray(3);
        PluginHolder[] arr2 = type.newArray(3);

        assertNotNull(arr1);
        assertNotNull(arr2);
        assertNotSame(arr1, arr2);
    }
    
    //TODO #ph: remove pluginType
}
