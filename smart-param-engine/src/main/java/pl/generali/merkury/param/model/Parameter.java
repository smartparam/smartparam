package pl.generali.merkury.param.model;

import java.util.*;
import javax.persistence.*;

/**
 * Klasa reprezentuje parametr obslugiwany przez silnik parametryczny.
 * Parametr sklada sie z 2 logicznych czesci:
 * <ol>
 * <li> metadane - czyli wszelkie dane opisujace specyfike parametru,
 * <li> macierz parametru - czyli zbior wzorcow dopasowania wraz z wartosciami skojarzonymi z tymi wzorcami.
 * </ol>
 *
 * W sklad <b>metadanych</b> wchodza m.in.:
 * <ol>
 * <li> name - unikalna nazwa parametru,
 * <li> type - typ wartosci zwracanej przez parametr
 * <li> levels - definicje poziomow parametru
 * <li> multivalue - czy wartosc parametru jest wielokomorkowa
 * <li> inputLevels - liczba poziomow wejsciowych (jesli parametr jest multivalue)
 * <li> array - czy komorka z wartoscia parametru jest tablica wartosci
 * <li> nullable - czy parametr moze zwracac wartosci <tt>null</tt>
 * <li> cacheable - czy macierz parametru jest wczytywana do pamieci
 * <li> archive - czy parametr jest logicznie usuniety (niedostepny)
 * </ol>
 *
 * <b>Macierz parametru</b> to zbior wierszy {@link ParameterEntry}, ktore zawieraja m.in.:
 * <ol>
 * <li> kolumny (poziomy) wejsciowe, ktore definiuja wzorzec dopasowania
 * <li> kolumny (poziomy) wyjsciowe, ktore definiuja wartosc parametru (<tt>multivalue</tt>)
 * <li> kolumne <tt>value</tt>, ktora zawiera wartosc parametru (pojedyncza lub tablicowa jesli <tt>array</tt>)
 * <li> kolumne <tt>function</tt>, ktora wyznacza wartosc parametru, jesli nie jest okreslona <tt>value</tt>
 * </ol>
 *
 * @see ParameterEntry
 * @see Level
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Entity
@Table(name = "par_parameter")
public class Parameter implements ParamModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

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
     * Kolejnosc ma znaczenie i jest ustalana na podstawie pola {@link Level#orderNo}.
     *
     * Mozliwe sa dwa warianty:
     * <ol>
     * <li> wszystkie poziomy sa wejsciowe
     * <li> k pierwszych poziomow jest wejsciowych, pozostale sa wyjsciowe (gdy <tt>multivalue</tt>)
     * </ol>
     */
    private List<Level> levels;

    /**
     * Macierz parametru, czyli zbior wierszy z wzorcami dopasowania i wartosciami.
     */
    private Set<ParameterEntry> entries;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_parameter")
    @SequenceGenerator(name = "seq_parameter", sequenceName = "seq_parameter")
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
     * Getter dla archive.
     *
     * @return czy archive
     */
    public boolean isArchive() {
        return archive;
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

    /**
     * Zwraca liste definicji poziomow.
     * Kolejnosc poziomow ma znaczenie i jest ustalana na podstawie pola {@link Level#orderNo}.
     *
     * @return posortowana (orderNo) lista poziomow
     */
    @OneToMany(mappedBy = "parameter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNo")
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Setter dla listy poziomow.
     *
     * @param levels lista poziomow
     */
    public void setLevels(List<Level> levels) {
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
    public void addLevel(Level... array) {
        if (levels == null) {
            levels = new ArrayList<Level>();
        }

        for (Level level : array) {
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
    public Set<ParameterEntry> getEntries() {
        return entries;
    }

    /**
     * Ustawia macierz parametru.
     *
     * @param entries zbior wierszy parametru
     */
    void setEntries(Set<ParameterEntry> entries) {
        this.entries = entries;
    }

    /**
     * Dodaje wiersz parameru do juz istniejacego zbioru wierszy.
     *
     * @param e wiersz parametru
     */
    public void addEntry(ParameterEntry e) {
        if (entries == null) {
            entries = new HashSet<ParameterEntry>();
        }

        e.setParameter(this);
        entries.add(e);
    }

    /**
     * Dodaje wiersze parametru do juz instejacego zbioru wierszy.
     *
     * @param rows wiersze parametru
     */
    public void addEntries(Collection<ParameterEntry> rows) {
        for (ParameterEntry pe : rows) {
            addEntry(pe);
        }
    }

    /**
     * Getter dla flagi multivaule.
     *
     * @return multivalue
     */
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
