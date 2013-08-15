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

import java.util.List;
import java.util.Set;

/**
 * Klasa reprezentuje parametr obslugiwany przez silnik parametryczny. Parametr
 * sklada sie z 2 logicznych czesci: <ol> <li> metadane - czyli wszelkie dane
 * opisujace specyfike parametru, <li> macierz parametru - czyli zbior wzorcow
 * dopasowania wraz z wartosciami skojarzonymi z tymi wzorcami. </ol>
 *
 * W sklad <b>metadanych</b> wchodza m.in.: <ol> <li> name - unikalna nazwa
 * parametru, <li> type - typ wartosci zwracanej przez parametr <li> levels -
 * definicje poziomow parametru <li> multivalue - czy wartosc parametru jest
 * wielokomorkowa <li> inputLevels - liczba poziomow wejsciowych (jesli parametr
 * jest multivalue) <li> array - czy komorka z wartoscia parametru jest tablica
 * wartosci <li> nullable - czy parametr moze zwracac wartosci <tt>null</tt>
 * <li> cacheable - czy macierz parametru jest wczytywana do pamieci <li>
 * archive - czy parametr jest logicznie usuniety (niedostepny) </ol>
 *
 * <b>Macierz parametru</b> to zbior wierszy {@link ParameterEntry}, ktore
 * zawieraja m.in.: <ol> <li> kolumny (poziomy) wejsciowe, ktore definiuja
 * wzorzec dopasowania <li> kolumny (poziomy) wyjsciowe, ktore definiuja wartosc
 * parametru (<tt>multivalue</tt>) <li> kolumne <tt>value</tt>, ktora zawiera
 * wartosc parametru (pojedyncza lub tablicowa jesli <tt>array</tt>) <li>
 * kolumne <tt>function</tt>, ktora wyznacza wartosc parametru, jesli nie jest
 * okreslona <tt>value</tt> </ol>
 *
 * @see ParameterEntry
 * @see Level
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.0.1
 */
public interface Parameter {

    /**
     * Returns unique name of parameter.
     *
     * @return parameter name
     */
    String getName();

    /**
     * Returns ordered list of levels.
     *
     * @return list of levels
     */
    List<Level> getLevels();

    /**
     * Returns number of input levels (criteria levels).
     *
     * @return number of input levels
     */
    int getInputLevels();

    /**
     * Returns set of parameter entries representing (unordered) parameter matrix.
     *
     * @return parameter matrix
     */
    Set<ParameterEntry> getEntries();

    /**
     * Whether parameter's search index is stored in cache.
     *
     * @return is cacheable
     */
    boolean isCacheable();

    /**
     * Can parameter return null-value, which means that there might be no
     * matching parameter row for given input levels values.
     *
     * @return is nullable
     */
    boolean isNullable();

    char getArraySeparator();   // still in use
}
