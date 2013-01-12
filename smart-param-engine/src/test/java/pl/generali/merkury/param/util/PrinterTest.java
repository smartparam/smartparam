package pl.generali.merkury.param.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Przemek Hertel
 */
public class PrinterTest {

    @Test
    public void testPrint() {

        // kolekcja wejsciowa
        List<Integer> list = Arrays.asList(123, 234, 345, 456);

        // wykonanie testu
        String result = Printer.print(list, "Integer list");

        // oczekiwany wynik
        String expectedResult =
                Formatter.NL
                + "Integer list (4)" + Formatter.NL
                + "  1. 123" + Formatter.NL
                + "  2. 234" + Formatter.NL
                + "  3. 345" + Formatter.NL
                + "  4. 456" + Formatter.NL;

        // weryfikacja
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPrint__maxLines() {

        // kolekcja wejsciowa
        List<Integer> list = Arrays.asList(123, 234, 345, 456, 567, 678);

        // dane testowe
        Integer[] maxArray = {
            2, 3, 4, 7
        };

        // oczekiwane wartosci
        String[] expectedResults = {
            // maxLines = 2
            Formatter.NL
            + "Integer list (6)" + Formatter.NL
            + "  1. 123" + Formatter.NL
            + "  ..." + Formatter.NL
            + "  6. 678" + Formatter.NL,
            // maxLines = 3
            Formatter.NL
            + "Integer list (6)" + Formatter.NL
            + "  1. 123" + Formatter.NL
            + "  ..." + Formatter.NL
            + "  6. 678" + Formatter.NL,
            // maxLines = 4
            Formatter.NL
            + "Integer list (6)" + Formatter.NL
            + "  1. 123" + Formatter.NL
            + "  2. 234" + Formatter.NL
            + "  ..." + Formatter.NL
            + "  6. 678" + Formatter.NL,
            // maxLines = 7
            Formatter.NL
            + "Integer list (6)" + Formatter.NL
            + "  1. 123" + Formatter.NL
            + "  2. 234" + Formatter.NL
            + "  3. 345" + Formatter.NL
            + "  4. 456" + Formatter.NL
            + "  5. 567" + Formatter.NL
            + "  6. 678" + Formatter.NL
        };

        // wykonanie testow
        for (int i = 0; i < maxArray.length; i++) {
            int maxLines = maxArray[i];
            String expectedResult = expectedResults[i];

            String result = Printer.print(list, "Integer list", maxLines, null);
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testPrint__null() {

        // kolekcja wejsciowa
        List<Integer> list = null;
        Integer[] array = null;

        // wykonanie testu
        String result1 = Printer.print(list, "Integer list");
        String result2 = Printer.print(array, "Integer array");

        // weryfikacja
        assertNull(result1);
        assertNull(result2);
    }

    @Test
    public void testPrint__noTitle() {

        // kolekcja wejsciowa
        Long[] array = {11223344L, 55667788L};

        // wykonanie testu
        String result = Printer.print(array, null);

        // oczekiwany wynik
        String expectedResult =
                Formatter.NL
                + "  1. 11223344" + Formatter.NL
                + "  2. 55667788" + Formatter.NL;

        // weryfikacja
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormat() {

        // formatowane obiekty
        Object[] objects = {
            new Integer("123"),
            new Integer("123"),
            new String[]{"A", "B", "C"},
            new BigDecimal("1.23")
        };

        // formattery
        Formatter[] formatters = {
            null,
            new Formatter() {

                @Override
                public String format(Object obj) {
                    return "#" + obj + "#";
                }
            },
            null,
            null
        };

        // oczekiwane wyniki
        String[] expectedResults = {
            "123",
            "#123#",
            "[A, B, C]",
            "1.23"
        };

        // wykonanie testow
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            Formatter formatter = formatters[i];
            String expectedResult = expectedResults[i];

            String result = Printer.format(obj, formatter);
            assertEquals(expectedResult, result);
        }
    }

    private class NonAbstractPrinter extends Printer {
    }
    Printer instance = new NonAbstractPrinter();

}
