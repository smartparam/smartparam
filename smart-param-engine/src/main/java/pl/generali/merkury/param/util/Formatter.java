package pl.generali.merkury.param.util;

/**
 * @author Przemek Hertel
 */
public interface Formatter {

    String NL = System.getProperty("line.separator");

    int INITIAL_STR_LEN_128 = 128;

    int INITIAL_STR_LEN_256 = 256;

    String format(Object obj);
}
