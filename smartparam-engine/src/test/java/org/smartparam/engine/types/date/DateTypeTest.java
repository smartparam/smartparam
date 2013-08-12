package org.smartparam.engine.types.date;

import org.smartparam.engine.types.date.DateHolder;
import org.smartparam.engine.types.date.DateType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*; 
import org.testng.annotations.BeforeMethod;

/**
 * @author Przemek Hertel
 */
public class DateTypeTest {

    DateType type;

    @BeforeMethod
    public void init() {
        type = new DateType();
    }

    @Test
    public void testEncode() {

        DateType.setDefaultOutputPattern("dd-MM-yyyy");

        // przypadki testowe
        Object[][] testCases = {
            {d("01-01-1900"), "01-01-1900"},
            {d("28-02-2000"), "28-02-2000"},
            {null, null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Date date = (Date) testCase[0];
            DateHolder holder = new DateHolder(date);
            String expectedResult = (String) testCase[1];

            assertEquals(expectedResult, type.encode(holder));
        }
    }

    @Test
    public void testEncode__outputPattern() {

        // zmiana outputPattern z defaultowego na inny
        DateType.setDefaultOutputPattern("yyyy/MM/dd");

        // przypadki testowe
        Object[][] testCases = {
            {d("01-01-1900"), "1900/01/01"},
            {d("28-02-2000"), "2000/02/28"},
            {null, null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Date date = (Date) testCase[0];
            DateHolder holder = new DateHolder(date);
            String expectedResult = (String) testCase[1];

            assertEquals(expectedResult, type.encode(holder));
        }
    }

    @Test
    public void testDecode() {

        // oczekiwany wynik dekodowania stringa
        Date date = d("27-04-2012");

        // przypadki testowe
        Object[][] testCases = {
            {"27-04-2012", date},
            {"27.04.2012", date},
            {"27/04/2012", date},

            {"2012-04-27", date},
            {"2012.04.27", date},
            {"2012/04/27", date},

            {" 2012/04/27 ", date},
            {"  ", null},
            {null, null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            String text = (String) testCase[0];
            Date expectedDate = (Date) testCase[1];

            DateHolder result = type.decode(text);
            assertEquals(expectedDate, result.getValue());
            assertEquals(expectedDate, result.getDate());
            assertEquals(new DateHolder(expectedDate), result);
        }
    }

    @Test
    public void testDecode__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac zdekodowane do DateHoldera
        String[] illegals = {
            "30-02-2012", "32-01-2012", "27 kwietnia 2012",
            "01-01.2012", "01-01/2012",
            "01.01-2012", "01.01/2012",
            "01/01.2012", "01/01-2012",
            "2012-01.01", "2012-01/01",
            "2012.01-01", "2012.01/01",
            "2012/01-01", "2012/01.01"
        };

        // wykonanie testow, oczekujemy wyjatku
        for (String text : illegals) {
            try {
                type.decode(text);
                fail();
            } catch (RuntimeException e) {
                System.out.println("OK: " + e.getMessage());
            }
        }
    }

    @Test
    public void testConvert() {

        // przykladowa data
        Date d = d("27-04-2012");
        Date d2 = dlong("27-04-2012 16:45:55");

        // przypadki testowe: [argument (Object)][oczekiwana wartosc holdera (Date)]
        Object[][] testCases = {
            {d("27-04-2012"), d},
            {dlong("27-04-2012 16:45:55"), d2},
            {new Timestamp(d.getTime()), d},
            {new java.sql.Date(d.getTime()), d},
            {new GregorianCalendar(2012, 4-1, 27), d},
            {"2012/04/27", d},
            {"27.04.2012", d},
            {"  ", null},
            {null, null}
        };

        // wykonanie testow
        for (Object[] testCase : testCases) {
            Object obj = testCase[0];
            Date expectedValue = (Date) testCase[1];

            assertEquals(expectedValue, type.convert(obj).getDate());
        }
    }

    @Test
    public void testConvert__illegalArgument() {

        // przypadki testowe, ktore nie moga zostac skonwertowane do NumberHoldera
        Object[] illegals = {new BigDecimal(7), new Integer(1), "abc"};

        // wykonanie testow, oczekujemy wyjatku
        for (Object obj : illegals) {
            try {
                type.convert(obj);
                fail();
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
    }

    @Test
    public void testNewArray() {
        DateHolder[] arr1 = type.newArray(3);
        DateHolder[] arr2 = type.newArray(3);

        assertNotNull(arr1);
        assertNotNull(arr2);
        assertNotSame(arr1, arr2);
    }

    private Date d(String dmy) {
        try {
            return sdf("dd-MM-yyyy").parse(dmy);
        } catch (ParseException ex) {
            return null;
        }
    }
    private Date dlong(String dmy) {
        try {
            return sdf("dd-MM-yyyy HH:mm:ss").parse(dmy);
        } catch (ParseException ex) {
            return null;
        }
    }

    private SimpleDateFormat sdf(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf;
    }

}
