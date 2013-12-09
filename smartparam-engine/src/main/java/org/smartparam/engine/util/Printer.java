/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public final class Printer {

    private Printer() {
    }

    private static final int MIN_OUTPUT_LINES = 3;

    private static final int EXPECTED_LINE_LENGTH = 32;

    private static final int LINE_NUMBER_WIDTH = 3;

    /**
     * Tworzy string reprezentujacy zawartosc kolekcji <tt>list</tt> w czytelnej postaci.
     * Kazdy element kolekcji wypisywany jest w kolejnej linii.
     * Uzytkownik moze okreslic maksymalna liczbe linii, ktore zostana wydrukowane.
     * Dodatkowo mozna podac <tt>formatter</tt>, ktory zostanie uzyty do sformatowania
     * kazdego elementu w kolekcji.
     *
     * @param list      kolekcja obiektow
     * @param title     opcjonalny tytul kolekcji
     * @param maxLines  maksymalna liczba linii, ktore zostana wypisane, lub 0 gdy brak ograniczen
     * @param formatter obiekt formatujacy kazdy element kolekcji
     *
     * @return reprezentacja kolekcji, w ktorej kazdy element jest przedstawiony w osobnej linii
     */
    public static String print(Collection<?> list, String title, int maxLines, Formatter formatter) {
        if (list == null) {
            return null;
        }

        // ograniczenie liczby linii od dolu (co najmniej 3 linie)
        int max = Math.max(maxLines, MIN_OUTPUT_LINES);

        int lines = Math.min(list.size(), maxLines);
        boolean printAll = maxLines == 0 || list.size() <= max;

        StringBuilder sb = new StringBuilder(lines * EXPECTED_LINE_LENGTH);
        sb.append(Formatter.NL);

        if (title != null) {
            sb.append(title).append(" (").append(list.size()).append(')').append(Formatter.NL);
        }

        int c = 0;
        for (Object e : list) {
            c++;
            String value = format(e, formatter);

            if (printAll || c <= max - 2 || c == list.size()) {
                sb.append(padNumber(c)).append(". ").append(value);
                sb.append(Formatter.NL);
            } else if (c == max - 1) {
                sb.append("  ...");
                sb.append(Formatter.NL);
            }
        }

        return sb.toString();
    }

    /**
     * Wypisuje zawartosc kolekcji bez ograniczenia liczby linii.
     *
     * @see #print(java.util.Collection, java.lang.String, int, org.smartparam.engine.util.Formatter)
     */
    public static String print(Collection<?> list, String title) {
        return print(list, title, 0, null);
    }

    /**
     * Wypisuje zawartosc tablicy bez ograniczenia liczby linii.
     *
     * @see #print(java.util.Collection, java.lang.String, int, org.smartparam.engine.util.Formatter)
     * @param array tablica obiektow
     * @param title opcjonalny tytul
     *
     * @return string reprezentujacy zawartosc tablicy, linia po linii
     */
    public static String print(Object[] array, String title) {
        List<Object> list = array != null ? Arrays.asList(array) : null;
        return print(list, title, 0, null);
    }

    /**
     * Formatuje obiekt, uzywajac formattera, jesli jest podany.
     * Formatowanie odbywa sie wg schematu:
     * <ol>
     * <li>jesli formatter jest podany, uzywa tego formattera,
     * <li>jesli obiekt <tt>e</tt> jest tablica, zostanie zwrocona zawartosc tablicy (Arrays.toString),
     * <li>w przeciwnym razie zostanie uzyte: String.valueOf(e).
     * </ol>
     *
     * @param e         obiekt, ktory ma zostac sformatowany
     * @param formatter opcjonalny formatter uzywany do formatowania
     *
     * @return stringowa reprezentacja obiektu <tt>e</tt>
     */
    static String format(Object e, Formatter formatter) {
        if (formatter != null) {
            return formatter.format(e);
        }
        if (e instanceof Object[]) {
            return Arrays.toString((Object[]) e);
        }
        return String.valueOf(e);
    }

    /**
     * Wyrownuje liczba <tt>n</tt> do prawej strony,
     * tak by zajmowala {@link #LINE_NUMBER_WIDTH} znakow.
     */
    private static String padNumber(int n) {
        String numberString = String.valueOf(n);

        if (LINE_NUMBER_WIDTH > numberString.length()) {
            StringBuilder builder = new StringBuilder(LINE_NUMBER_WIDTH);
            for (int i = 0; i < LINE_NUMBER_WIDTH - numberString.length(); ++i) {
                builder.append(" ");
            }
            builder.append(numberString);
            return builder.toString();
        } else {
            return numberString;
        }
    }
}
