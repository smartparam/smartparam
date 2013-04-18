package org.smartparam.provider.jpa.model;

import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;

@Entity
@Table(name = "smartparam_parameter")
@NamedQuery(name=JpaParameter.LOAD_PARAMETER_QUERY, query="from JpaParameter where name = :name")
public class JpaParameter implements Parameter, JpaModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identifier of named query fetching parameter using its name.
     */
    public static final String LOAD_PARAMETER_QUERY = "smartparamLoadParameter";

    /**
     * Domyslny separator wartosci w komorkach typu tablicowego.
     */
    public static final char DEFAULT_ARRAY_SEPARATOR = ',';

    /**
     * Klucz glowny.
     */
    private int id;

    /**
     * Unikalna nazwa parametru.
     */
    private String name;

    /**
     * Krotki opis parametru (labelka).
     */
    private String label;

    /**
     * Szczegolowy opis parametru.
     */
    private String description;

    /**
     * Czy parametr jest zarchiwizowany (logicznie usuniety).
     */
    private boolean archive;

    /**
     * Typ wartosci parametru (zgodny z systemem typow).
     */
    private String type;

    /**
     * Lista definicji poziomow.
     * Kolejnosc ma znaczenie i jest ustalana na podstawie pola {@link JpaLevel#orderNo}.
     *
     * Mozliwe sa dwa warianty:
     * <ol>
     * <li> wszystkie poziomy sa wejsciowe
     * <li> k pierwszych poziomow jest wejsciowych, pozostale sa wyjsciowe (gdy <tt>multivalue</tt>)
     * </ol>
     */
    private List<JpaLevel> levels;

    /**
     * Macierz parametru, czyli zbior wierszy z wzorcami dopasowania i wartosciami.
     */
    private Set<JpaParameterEntry> entries;

    /**
     * Czy parametr wielowartosciowy, definiujacy wartosc na kilku poziomach wyjsciowych.
     */
    private boolean multivalue;

    /**
     * Liczba poziomow wejsciowych okreslna, gdy parametr <tt>multivalue</tt>.
     */
    private int inputLevels;

    /**
     * Czy parametr moze zwracac <tt>null</tt>.
     */
    private boolean nullable;

    /**
     * Czy macierz parametru jest trzymana w pamieci (domyslnie - tak).
     */
    private boolean cacheable = true;

    /**
     * Czy pole <tt>value</tt> wiersza parametru zawiera tablice wartosci.
     */
    private boolean array;

    /**
     * Separator stosowany parsowania <tt>value</tt>, gdy parametru jest typu <tt>array</tt>.
     */
    private char arraySeparator = DEFAULT_ARRAY_SEPARATOR;

    /**
     * Getter dla klucza glownego.
     *
     * @return klucz glowny
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sp_parameter")
    @SequenceGenerator(name = "seq_sp_parameter", sequenceName = "seq_sp_parameter")
    public int getId() {
        return id;
    }

    /**
     * Setter dla klucza glownego.
     *
     * @param id klucz glowny
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter dla archive.
     *
     * @param archive wartosc flagi
     */
    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    /**
     * Getter dla description
     *
     * @return description
     */
    @Column(length = LONG_COLUMN_LENGTH)
    public String getDescription() {
        return description;
    }

    /**
     * Setter dla description
     *
     * @param description opis
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter dla label.
     *
     * @return label
     */
    @Column
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Setter dla label.
     *
     * @param label label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public Level getLevel(int levelNumber) {
        return levels.get(levelNumber);
    }

    /**
     * Zwraca liste definicji poziomow.
     * Kolejnosc poziomow ma znaczenie i jest ustalana na podstawie pola {@link JpaLevel#orderNo}.
     *
     * @return posortowana (orderNo) lista poziomow
     */
    @OneToMany(mappedBy = "parameter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNo")
    public List<JpaLevel> getLevels() {
        return levels;
    }

    /**
     * Setter dla listy poziomow.
     *
     * @param levels lista poziomow
     */
    public void setLevels(List<JpaLevel> levels) {
        this.levels = levels;
        setupLevelOrdering();
    }

    /**
     * Dodaje poziom lub wiele poziomow do juz istniejacej listy poziomow.
     * Renumeruje pola <tt>orderNo</tt> na poziomach wewnatrz listy,
     * tak by (na wyjsciu z metody) byly zgodne z kolejnoscia zajmowana w liscie.
     *
     * @param array poziom lub poziomy
     */
    public void addLevel(JpaLevel... array) {
        if (levels == null) {
            levels = new ArrayList<JpaLevel>();
        }

        for (JpaLevel level : array) {
            level.setParameter(this);
            levels.add(level);
        }

        setupLevelOrdering();
    }

    /**
     * Ustawia pola <tt>orderNo</tt> na poziomach zgodnie z pozycja zajmowana w liscie.
     * Numerowanie zaczyna sie od 0.
     */
    private void setupLevelOrdering() {
        if (levels != null) {
            for (int i = 0; i < levels.size(); ++i) {
                levels.get(i).setOrderNo(i);
            }
        }
    }

    /**
     * Zwraca liczbe poziomow.
     *
     * @return liczba poziomow
     */
    @Transient
    public int getLevelCount() {
        return levels != null ? levels.size() : 0;
    }

    /**
     * Zwraca nazwe parametru.
     *
     * @return nazwa parametru
     */
    @Column(nullable = false, unique = true)
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter dla nazwy parametru
     *
     * @param name nazwa parametru
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca typ wartosci parametru, zgodny z systemem typow.
     *
     * @return kod typu wartosci
     */
    @Column(length = SHORT_COLUMN_LENGTH)
    @Override
    public String getType() {
        return type;
    }

    /**
     * Setter dla typu wartosci.
     *
     * @param type kod typu
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Ustawia kod typu, ktory pobiera z metody <tt>toString</tt> przekazanego enuma.
     *
     * @param e enum, ktorego <tt>toString</tt> jest traktowany jako kod typu
     */
    public void setType(Enum<?> e) {
        setType(e.toString());
    }

    /**
     * Zwraca <b>zbior</b> wierszy parametru reprezentujacy macierz.
     * Ze wzgledu na semantyke zbioru, kolejnosc wierszy jest nieokreslona,
     * zatem jest to luzna reprezentacja macierzy.
     *
     * @return macierz parametru
     */
    @OneToMany(mappedBy = "parameter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Override
    public Set<JpaParameterEntry> getEntries() {
        return entries;
    }

    /**
     * Ustawia macierz parametru.
     *
     * @param entries zbior wierszy parametru
     */
    void setEntries(Set<JpaParameterEntry> entries) {
        this.entries = entries;
    }

    /**
     * Dodaje wiersz parameru do juz istniejacego zbioru wierszy.
     *
     * @param e wiersz parametru
     */
    public void addEntry(JpaParameterEntry e) {
        if (entries == null) {
            entries = new HashSet<JpaParameterEntry>();
        }

        e.setParameter(this);
        entries.add(e);
    }

    /**
     * Dodaje wiersze parametru do juz instejacego zbioru wierszy.
     *
     * @param rows wiersze parametru
     */
    public void addEntries(Collection<JpaParameterEntry> rows) {
        for (JpaParameterEntry pe : rows) {
            addEntry(pe);
        }
    }

    /**
     * Getter dla flagi multivaule.
     *
     * @return multivalue
     */
    @Override
    public boolean isMultivalue() {
        return multivalue;
    }

    /**
     * Setter dla flagi multivalue
     *
     * @param multivalue wartosc flagi
     */
    public void setMultivalue(boolean multivalue) {
        this.multivalue = multivalue;
    }

    /**
     * Zwraca liczba poziomow wejsciowych (k).
     * Liczba ta ma znaczenie, jesli parametr jest typu <tt>multivalue</tt>.
     *
     * @return liczba poziomow wejsciowych
     */
    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    /**
     * Ustawia liczbe poziomow wejsciowych (k).
     * Liczba ta ma znaczenie, jesli parametr jest typu <tt>multivalue</tt>.
     *
     * @param inputLevels liczba poziomow wejsciowych (k)
     */
    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    /**
     * Czy parametr moze zwracac <tt>null</tt>.
     *
     * @return nullable
     */
    @Override
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Setter dla flagi nullable
     *
     * @param nullable wartosc flagi
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Czy macierz parametru jest trzymana w pamieci.
     *
     * @return cacheable
     */
    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    /**
     * Setter dla flagi cacheable
     *
     * @param cacheable wartosc flagi
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
     * Czy wartosc parametru jest traktowana jako tablica wartosci elementarnych.
     *
     * @return array
     */
    @Column(name = "array_flag")
    @Override
    public boolean isArray() {
        return array;
    }

    /**
     * Setter dla array
     *
     * @param array wartosc flagi
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * Zwraca znak bedacy separatorem, gdy <tt>value</tt> reprezentuje tablice
     *
     * @return seperator
     */
    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

    /**
     * Setter dla znaku separatora
     *
     * @param arraySeparator znak separatora
     */
    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parameter#").append(id);
        sb.append('[').append(name);
        sb.append(", type=").append(type);
        sb.append(", levels=").append(getLevelCount());
        sb.append(", inputLevels=").append(getInputLevels());
        sb.append(nullable ? ", nullable" : ", notnull");

        if (multivalue) {
            sb.append(", multivalue");
        }
        if (array) {
            sb.append(", array");
        }
        if (archive) {
            sb.append(", archive");
        }
        if (!cacheable) {
            sb.append(", nocache");
        }

        sb.append(']');
        return sb.toString();
    }
}
