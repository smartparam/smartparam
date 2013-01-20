package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.type.AbstractType;

/**
 * Przetworzony (skompilowany) parametr.
 * Jest tworzony jako kopia obiektu parametru wczytanego z bazy danych,
 * dzieki temu jest uwolniony od ewentualnych referencji do obiektow JPA.
 * <p>
 *
 * Przetworzony parametr zawiera zbudowany indeks wyszukiwania.
 *
 * @see org.smartparam.engine.model.Parameter
 * @see org.smartparam.engine.model.ParameterEntry
 * @see org.smartparam.engine.model.Level
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedParameter {

    /**
     * Unikalna nazwa parametru.
     */
    private String name;

    /**
     * Typ parametru.
     */
    private AbstractType<?> type;

    /**
     * Definicje poziomow.
     */
    private PreparedLevel[] levels;

    /**
     * Indeks wyszukiwania zbudowany dla tego parametru.
     */
    private LevelIndex<PreparedEntry> index;

    /**
     * Flaga <tt>multivalue</tt> parametru.
     */
    private boolean multivalue;

    /**
     * Liczba poziomow wejsciowych (jesli multivalue).
     */
    private int inputLevelsCount;

    /**
     * Flaga <tt>nullable</tt> parametru.
     */
    private boolean nullable;

    /**
     * Flaga <tt>cacheable</tt> parametru.
     */
    private boolean cacheable;

    /**
     * Flaga <tt>array</tt> parametru.
     */
    private boolean array;

    /**
     * Znak separatora, jesli parametr typu <tt>array</tt>.
     */
    private char arraySeparator;

    /**
     * Zwraca zbudowany indeks wyszukiwania.
     *
     * @return index
     */
    public LevelIndex<PreparedEntry> getIndex() {
        return index;
    }

    /**
     * Ustawia index wyszukiwania.
     *
     * @param index index
     */
    public void setIndex(LevelIndex<PreparedEntry> index) {
        this.index = index;
    }

    /**
     * Zwraca przygotowane definicje poziomow.
     *
     * @return poziomy
     */
    public PreparedLevel[] getLevels() {
        return levels;
    }

    /**
     * Setter dla poziomow.
     *
     * @param levels poziomy
     */
    public void setLevels(PreparedLevel[] levels) {
        this.levels = levels;
    }

    /**
     * Zwraca unikalna nazwe parametru.
     *
     * @return nazwa parametru
     */
    public String getName() {
        return name;
    }

    /**
     * Setter dla nazwy parametru.
     *
     * @param name nazwa parametru
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca typ parametru.
     *
     * @return typ parametru
     */
    public AbstractType<?> getType() {
        return type;
    }

    /**
     * Setter dla typu parametru
     *
     * @param type kod typu
     */
    public void setType(AbstractType<?> type) {
        this.type = type;
    }

    /**
     * Zwraca liczbe poziomow.
     *
     * @return liczba poziomow
     */
    public int getLevelCount() {
        return levels != null ? levels.length : 0;
    }

    /**
     * Zwraca liczbe poziomow wejsciowych - jesli parametr multivalue.
     * W przeciwnym przypadku zwraca liczbe wszystkich poziomow.
     *
     * @see #getLevelCount()
     *
     * @return liczba poziomow wejsciowych (k)
     */
    public int getInputLevelsCount() {
        return multivalue ? inputLevelsCount : getLevelCount();
    }

    /**
     * Setter dla liczby poziomow wejsciowych.
     *
     * @param inputLevelsCount liczba poziomow wejsciowych
     */
    public void setInputLevelsCount(int inputLevelsCount) {
        this.inputLevelsCount = inputLevelsCount;
    }

    /**
     * Czy parametru jest nullowalny.
     *
     * @return flaga nullable
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Setter dla flagi nullable.
     *
     * @param nullable flaga
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Czy macierz parametru jest trzymana w pamieci.
     *
     * @return flaga cacheable
     */
    public boolean isCacheable() {
        return cacheable;
    }

    /**
     * Setter dla flagi cacheable.
     *
     * @param cacheable flaga
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
     * Czy parametr <b>nie</b> jest nullowalny.
     *
     * @return czy parametr nie jest nullable
     */
    public boolean isNotNull() {
        return !isNullable();
    }

    /**
     * Czy parametru jest typu multivalue.
     *
     * @return wartosc flagi
     */
    public boolean isMultivalue() {
        return multivalue;
    }

    /**
     * Setter dla multivalue.
     *
     * @param multivalue wartosc flagi
     */
    public void setMultivalue(boolean multivalue) {
        this.multivalue = multivalue;
    }

    /**
     * Getter dla flagi array.
     *
     * @return array
     */
    public boolean isArray() {
        return array;
    }

    /**
     * Setter dla array.
     *
     * @param array wartosc flagi
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * Getter dla znaku separatora, jesli parametr jest <tt>array</tt>.
     *
     * @return znak separatora
     */
    public char getArraySeparator() {
        return arraySeparator;
    }

    /**
     * Setter dla znaku separatora.
     *
     * @param arraySeparator znak separatora
     */
    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
