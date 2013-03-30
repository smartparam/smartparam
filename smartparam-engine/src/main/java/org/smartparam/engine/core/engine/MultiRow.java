package org.smartparam.engine.core.engine;

import java.util.Arrays;
import java.util.List;
import org.smartparam.engine.core.exception.ParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

/**
 * Obiekt reprezentuje wartosc <i>wielowierszowego</i> parametru typu <i>multivalue</i>.
 * Innymi slowy reprezentuje <i>podmacierz</i> parametru.
 * Kazdy wiersz tej podmacierzy jest typu {@link MultiValue}.
 * <p>
 *
 * Dla przykladu, jesli parametr definiuje 5 poziomow, w tym 2 wejsciowe i 3 wyjsciowe, wowczas wartoscia
 * parametru moze byc tablica obiektow MultiValue (kazdy ma 3 poziomy wyjsciowe).
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class MultiRow {

    /**
     * Wiersze podmacierzy parametru.
     */
    private MultiValue[] rows;

    /**
     * Konstruuje obiekt inicjalizujac rozmiar tablicy wierszy.
     *
     * @param rowNumber rozmiar tablicy wierszy
     */
    public MultiRow(int rowNumber) {
        rows = new MultiValue[rowNumber];
    }

    /**
     * Ustawia i-ty wiersz w podmacierzy wynikowej.
     *
     * @param i   indeks wiersza (numerowany od 0)
     * @param row wiersz
     */
    public void setRow(int i, MultiValue row) {
        rows[i] = row;
    }

    /**
     * Liczba wierszy podmacierzy.
     *
     * @return rozmiar tablicy
     */
    public int length() {
        return rows.length;
    }

    /**
     * Zwraca wszystkie wiersze podmacierzy.
     *
     * @return wszystkie wiersze
     */
    public MultiValue[] getRows() {
        return rows;
    }

    /**
     * Zwraca wszystkie wiersze macierzy w postaci listy.
     *
     * @return lista wierszy
     */
    public List<MultiValue> getRowsAsList() {
        return Arrays.asList(rows);
    }

    /**
     * Zwraca k-ty wiersz podmacierzy wynikowej.
     *
     * @param k numer wiersza (numerowanie od 1)
     * @return k-ty wiersz
     *
     * @throws ParamUsageException errorcode={@link ErrorCode#INDEX_OUT_OF_BOUNDS}, jesli k jest niepoprawnym numerem poziomu wyjsciowego
     */
    public MultiValue getRow(int k) {
        if (k >= 1 && k <= rows.length) {
            return rows[k - 1];
        }

        throw new ParamUsageException(
                SmartParamErrorCode.INDEX_OUT_OF_BOUNDS,
                "Getting element from non-existing position: " + k);
    }

    @Override
    public String toString() {
        return Printer.print(Arrays.asList(rows), "MultiRow", 0, new MultiValueInlineFormatter());
    }

    static final class MultiValueInlineFormatter implements Formatter {

        @Override
        public String format(Object obj) {
            MultiValue mv = (MultiValue) obj;
            return mv.toStringInline();
        }
    }
}
