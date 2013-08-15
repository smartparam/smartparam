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
package org.smartparam.engine.model;

/**
 * Klasa reprezentuje pojedynczy wiersz parametru {@link Parameter}.
 * Kazdy parametr moze zawierac dowolnie wiele takich wierszy.
 * <p>
 *
 * Kazdy wiersz parametru (ParameterEntry) zawiera:
 * <ul>
 * <li> <b>wzorzec dopasowania</b> (<tt>levels</tt>) - wartosci/wzorce dla poszczegolnych poziomow
 * <li> <b>wartosc wiersza</b> (<tt>value</tt>) - wartosc zwracana jako wartosc parametru, jesli ten wiersz zostanie wybrany
 * <li> <b>funkcja z repozytorium</b> (<tt>function</tt>) - funkcja, ktorej wynik jest zwracany, jesli <tt>value</tt> jest rowne <tt>null</tt>
 * </ul>
 *
 * Wzorzec dopasowania, czyli tablica <tt>String[] levels</tt> to dynamiczna tablica,
 * ktora jest niejawnie rozszerzana w setterach, jesli nastapi odwolanie do nieistniejacego indeksu.
 * <p>
 *
 * <tt>ParameterEntry</tt> moze przechowywac wartosci dla dowolnie wiellu poziomow,
 * ale persystentne jest tylko 8 pol: od <tt>getLevel1()</tt> do <tt>getLevel8()</tt>.
 * Jesli poziomow jest wiecej niz 8, wartosc <tt>getLevel8</tt> zawiera poziom osmy i kolejne
 * skonkatenowane znakiem srednika (<tt>";"</tt>).
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface ParameterEntry {

    /**
     * Get all level patterns for this row.
     * Both input and output levels.
     *
     * @return levels row values
     */
    String[] getLevels();

    @Deprecated
    String getValue();

    @Deprecated
    String getFunction();

    /*
     * getValue() and getFunctions() were used for single-value parameters.
     * now there is no distinction between single-value, multi-value and multi-row.
     * - getValue() behavior can be obtained thru defining 1 output levele
     * - getFunction() cannot be achieved currently
     */
}
