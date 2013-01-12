package pl.generali.merkury.param.types.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.exception.ParamUsageException;

/**
 * @author Przemek Hertel
 */
public class DateHolderTest {

    Date d1 = d("15-03-2012");

    Date d2 = dlong("15-03-2012 16:55:55");

    DateHolder h1 = new DateHolder(d1);

    DateHolder h2 = new DateHolder(d2);

    DateHolder h3 = new DateHolder(null);

    @Test
    public void testGetValue() {

        assertEquals(d1, h1.getValue());
        assertEquals(d2, h2.getValue());
        assertNull(h3.getValue());
    }

    @Test
    public void testIsNull() {

        assertFalse(h1.isNull());
        assertFalse(h2.isNull());
        assertTrue(h3.isNull());
    }

    @Test
    public void testIsNotNull() {

        assertTrue(h1.isNotNull());
        assertTrue(h2.isNotNull());
        assertFalse(h3.isNotNull());
    }

    @Test(expected = ParamUsageException.class)
    public void testLongValue() {
        h1.longValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testIntValue() {
        h2.intValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testDoubleValue() {
        h3.doubleValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetInteger() {
        h1.getInteger();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetLong() {
        h2.getLong();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetDouble() {
        h3.getDouble();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetBigDecimal() {
        h1.getBigDecimal();
    }

    @Test()
    public void testGetString() {

        DateType.setDefaultOutputPattern("dd-MM-yyyy");

        assertEquals("15-03-2012", h1.getString());
        assertEquals("15-03-2012", h2.getString());
        assertNull(h3.getString());
    }

    @Test(expected = ParamUsageException.class)
    public void testBooleanValue() {
        h1.booleanValue();
    }

    @Test(expected = ParamUsageException.class)
    public void testGetBoolean() {
        h1.getBoolean();
    }

    @Test
    public void testGetDate() {
        assertEquals(d1, h1.getDate());
        assertEquals(d2, h2.getDate());
        assertEquals(null, h3.getDate());
    }

    @Test
    public void testCompareTo() {

        // dane testowe
        DateHolder[] array = {
            new DateHolder(d("07-07-2012")),
            new DateHolder(null),
            new DateHolder(d("01-01-2012")),
            new DateHolder(d("04-04-2012")),
            new DateHolder(d("03-03-2012"))
        };

        // oczekiwany wynik sortowania
        Date[] expectedResult = {
            null,
            d("01-01-2012"),
            d("03-03-2012"),
            d("04-04-2012"),
            d("07-07-2012")
        };

        // wykonanie sortowania
        Arrays.sort(array);

        // weryfikacja
        for (int i = 0; i < array.length; i++) {
            Date value = array[i].getValue();
            Date expected = expectedResult[i];
            assertEquals(expected, value);
        }
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
