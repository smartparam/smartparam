package pl.generali.merkury.param.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa zawiera metody pomocnicze i narzedziowe wykorzystywane przez silnik.
 * <p>
 * Niektore metody maja swoje odpowiedniki w bibliotece standardowej
 * lub roznych bibliotekach narzedziowych, ale sa zaimplementowane
 * w sposob zwiekszajacy ich wydajnosc pod katem uzycia w silniku.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class EngineUtil {

    /**
     * Splituje podany string i zwraca tablice tokenow. Separatorem jest
     * pojedynczy znak <tt>delim</tt>. Zwraca maksymalnie <tt>max</tt>
     * tokenow nawet, jesli wejsciowy string zawiera wiecej tokenow.
     * <p>
     * Metoda jest ponad 4 razy szybsza od String.split()
     * wywolanej dla 1-znakowego separatora.
     *
     * @see #split(String, char)
     * @param str       wejsciowy string
     * @param delim     znak traktowany jako separator
     * @param maxTokens maksymalna liczba tokenow, wartosc -1 zwraca tablice wszystkich tokenow (bez ograniczen)
     * @return tablica tokenow, nigdy nie zwraca nulla
     */
    public static String[] split(final String str, final char delim, final int maxTokens) {
        int max = maxTokens;
        List<String> result = new ArrayList<String>(max);
        if (max == 0) {
            max = -1;
        }

        int curr = -1;
        int prev = 0;
        while (true) {
            ++curr;
            if (curr == str.length()) {
                result.add(str.substring(prev, str.length()));
                --max;
                break;
            }
            if (str.charAt(curr) == delim) {
                result.add(str.substring(prev, curr));
                --max;
                prev = curr + 1;
            }

            if (max == 0) {
                break;
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Splituje podany string i zwraca tablice tokenow. Separatorem jest
     * pojedynczy znak <tt>delim</tt>. Zwraca tablice z wszystkimi
     * tokenami znalezionymi w wejsciowym stringu.
     * <p>
     * Metoda jest ponad 4 razy szybsza od String.split()
     * wywolanej dla 1-znakowego separatora.
     *
     * @see #split(String, char, int)
     * @param str   wejsciowy string
     * @param delim znak traktowany jako separator
     * @return tablica wszystkich tokenow, nigdy nie zwraca nulla
     */
    public static String[] split(final String str, final char delim) {
        return split(str, delim, 0);
    }

    /**
     * Dzieli string na dokladnie 2 tokeny.
     * Separatorem jest pierwsze wystapienie znaku <tt>delim</tt>.
     * <p>
     * Metoda jest 6 razy szybsza od {@link #split(java.lang.String, char, int))
     * i okolo 24 razy szybsza od String.split.
     * Ma zastosowanie w typowych hotspotach.
     *
     * @param str   string, ktory zostanie podzielony na dokladnie 2 tokeny
     * @param delim znak, ktory bedzie separatorem
     *
     * @return 2 elementowa tablica tokenow, nigdy nie zwraca nulla
     */
    public static String[] split2(final String str, final char delim) {
        String[] result = {"", ""};
        if (str != null) {
            int ix = str.indexOf(delim);
            if (ix >= 0) {
                result[0] = str.substring(0, ix);
                result[1] = str.substring(ix + 1);
            } else {
                result[0] = str;
            }
        }
        return result;
    }
}
