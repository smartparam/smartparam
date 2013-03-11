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
     * Returns label. TODO #ad write something more..
     *
     * @return label
     */
    String getLabel();

    /**
     * Returns parameter description. Description eases parameter maintenance,
     * should be clear and comprehensive.
     *
     * @return description
     */
//    String getDescription();
//TODO #ph remove getDescription from interface

    /**
     * Returns parameter return type (for single-value parameters).
     *
     * @return parameter return value type
     */
    String getType();

    /**
     * Returns ordered list of levels.
     *
     * @return list of levels
     */
    List<? extends Level> getLevels();

    /**
     * Returns level at given number.
     *
     * @param levelNumber level number
     *
     * @return sorted level value resolver list
     */
    Level getLevel(int levelNumber);
    //TODO #ph remove from interface and from impl ParProvImpl

    /**
     * Returns number of levels (level list length).
     *
     * @return number of parameter levels
     */
    int getLevelCount();
    //TODO #ph remove from interface

    /**
     * Returns number of input levels (k). Meaningful only for
     * <tt>MultiValue</tt> parameters.
     *
     * @return number of input levels (k)
     */
    int getInputLevels();

    /**
     * Returns set of parameter entries representing (unordered) parameter
     * matrix.
     *
     * @return parameter matrix
     */
    Set<? extends ParameterEntry> getEntries();

    /**
     * Is parametr in archive. TODO #ad what does it really mean?
     *
     * @return is archive
     */
    boolean isArchive();
    //TODO #ph maybe remove isArchive?

    /**
     * Should parameter return value be treated as an array of values.
     *
     * @return is it an array
     */
    boolean isArray();

    /**
     * Returns char used as value separator for list of values.
     *
     * @return separator
     */
    char getArraySeparator();

    /**
     * Can parameter be stored in cache.
     *
     * @return is cacheable
     */
    boolean isCacheable();

    /**
     * Can parameter return more than one value at a time.
     *
     * @return is multivalue
     */
    boolean isMultivalue();

    /**
     * Can parameter return null-value, which means that there might be no
     * matching parameter row for given input levels values.
     *
     * @return is nullable
     */
    boolean isNullable();
}
