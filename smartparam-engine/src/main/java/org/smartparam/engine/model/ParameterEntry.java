package org.smartparam.engine.model;

/**
 * Klasa reprezentuje pojedynczy wiersz parametru {@link Parameter}. Kazdy
 * parametr moze zawierac dowolnie wiele takich wierszy. <p>
 *
 * Kazdy wiersz parametru (ParameterEntry) zawiera: <ul> <li> <b>wzorzec
 * dopasowania</b> (<tt>levels</tt>) - wartosci/wzorce dla poszczegolnych
 * poziomow <li> <b>wartosc wiersza</b> (<tt>value</tt>) - wartosc zwracana jako
 * wartosc parametru, jesli ten wiersz zostanie wybrany <li> <b>funkcja z
 * repozytorium</b> (<tt>function</tt>) - funkcja, ktorej wynik jest zwracany,
 * jesli <tt>value</tt> jest rowne <tt>null</tt> </ul>
 *
 * Wzorzec dopasowania, czyli tablica <tt>String[] levels</tt> to dynamiczna
 * tablica, ktora jest niejawnie rozszerzana w setterach, jesli nastapi
 * odwolanie do nieistniejacego indeksu. <p>
 *
 * <tt>ParameterEntry</tt> moze przechowywac wartosci dla dowolnie wiellu
 * poziomow, ale persystentne jest tylko 8 pol: od <tt>getLevel1()</tt> do
 * <tt>getLevel8()</tt>. Jesli poziomow jest wiecej niz 8, wartosc
 * <tt>getLevel8</tt> zawiera poziom osmy i kolejne skonkatenowane znakiem
 * srednika (<tt>";"</tt>).
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface ParameterEntry {

    /**
     * Get parent parameter.
     *
     * @return parameter
     */
    Parameter getParameter();

    /**
     * Get all level patterns for this row.
     *
     * @return levels row values
     */
    String[] getLevels();

    /**
     * Returns patterns for first N levels.
     *
     * @param n number of levels
     *
     * @return values
     */
    String[] getLevels(int n);

    /**
     * Returns pattern (level value) for k level (numbering of levels is
     * 1-based). Method is arrayindex-safe, if no k level exists null is
     * returned.
     *
     * @param k level number (k = 1....n)
     *
     * @return k level pattern or null if k > number of levels
     */
    String getLevel(int k);

    /**
     * Returns value for row (if single value parameter).
     *
     * @return value value
     */
    public String getValue();

    /**
     * Get function, that will be used to evaluate row value if no value is set (value == null).
     * Function has to be stored in function repository.
     *
     * @return value resolving function
     */
    public Function getFunction();
}
