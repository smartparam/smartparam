package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.type.Type;

import java.util.Map;

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


    @Deprecated
    private Type<?> type;

    @Deprecated
    public Type<?> getType() {
        return type;
    }

    @Deprecated
    public void setType(Type<?> type) {
        this.type = type;
    }

    @Deprecated
    public boolean isMultivalue() {
        return true;
    }

    @Deprecated
    public boolean isArray() {
        return false;
    }

    /**
     * Unique name of parameter.
     */
    private String name;

    /**
     * Prepared (compiled) levels.
     */
    private PreparedLevel[] levels;

    /**
     * Search index built for this parameter.
     */
    private LevelIndex<PreparedEntry> index;

    /**
     * Number of input (criteria) levels.
     * Zero means this is no-criteria parameter.
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
     * Prepared mapping: level name to (1-based) level position
     */
    private Map<String, Integer> levelNameMap;

    /**
     * Znak separatora stosowany, jesli parametr uzywa leveli typu array
     */
    private char arraySeparator;

    /**
     * Returns prepared search index.
     *
     * @return search index
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
     * Zwraca liczbe poziomow.
     *
     * @return liczba poziomow
     */
    public int getLevelCount() {
        return levels != null ? levels.length : 0;
    }

    /**
     * Returns number of input (criteria) levels.
     *
     * @see #getLevelCount()
     *
     * @return number of input levels (k)
     */
    public int getInputLevelsCount() {
        return inputLevelsCount;
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

    public char getArraySeparator() {
        return arraySeparator;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    public Map<String, Integer> getLevelNameMap() {
		return levelNameMap;
	}

	public void setLevelNameMap(Map<String, Integer> levelNameMap) {
		this.levelNameMap = levelNameMap;
	}

    public PreparedLevel getOutputLevel(int k) {
        return levels[inputLevelsCount + k - 1];
    }
}
