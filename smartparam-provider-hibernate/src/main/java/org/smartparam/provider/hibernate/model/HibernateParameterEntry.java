package org.smartparam.provider.hibernate.model;

import java.util.Arrays;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.smartparam.engine.core.exception.ParamDefinitionException;
import org.smartparam.engine.core.exception.ParamException.ErrorCode;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.util.EngineUtil;

/**
 * Hibernate data source implementation of {@link ParameterEntry}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Entity
@Table(name = "par_parameter_entry")
public class HibernateParameterEntry implements ParameterEntry, ParamModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Klucz glowny.
     */
    private int id;

    /**
     * Parametr, do ktorego nalezy ten wiersz.
     */
    private HibernateParameter parameter;

    /**
     * Wzorzec dopasowania - dynamiczna tablica wartosci poziomow.
     */
    private String[] levels = EMPTY_ARRAY;

    /**
     * Wartosc wiersza. Musi byc zgodna z typem parametru.
     */
    private String value;

    /**
     * Funkcja zwracajaca wartosc wiersza. Uzywana, jesli <tt>value</tt> jest <tt>null</tt>.
     */
    private HibernateFunction function;

    /**
     * Konstruktor domyslny.
     */
    public HibernateParameterEntry() {
    }

    /**
     * Konstruktor inicjalizujacy obiekt.
     *
     * @param levels   wartosci poziomow
     * @param value    wartosc wiersza
     * @param function funkcja wartosci
     */
    public HibernateParameterEntry(String[] levels, String value, HibernateFunction function) {
        setLevels(levels);
        this.value = value;
        this.function = function;
    }

    /**
     * Konstruktor inicjalizujacy obiekt.
     *
     * @param levels wartosci poziomow
     * @param value  wartosc wiersza
     */
    public HibernateParameterEntry(String[] levels, String value) {
        this(levels, value, null);
    }

    /**
     * Konstruktor inicjalizujacy obiekt.
     *
     * @param csvLevels wartosci poziomow rozdzielone znakiem srednika
     * @param value     wartosc wiersza
     * @param function  funkcja wartosci
     */
    public HibernateParameterEntry(String csvLevels, String value, HibernateFunction function) {
        this(EngineUtil.split(csvLevels, ';'), value, function);
    }

    /**
     * Konstruktor inicjalizujacy obiekt.
     *
     * @param csvLevels wartosci poziomow rozdzielone znakiem srednika
     * @param value     wartosc wiersza
     */
    public HibernateParameterEntry(String csvLevels, String value) {
        this(csvLevels, value, null);
    }

    /**
     * Konstruktor inicjalizujacy obiekt.
     *
     * @param csvLevels wartosci poziomow rozdzielone znakiem srednika
     * @param function  funkcja wartosci
     */
    public HibernateParameterEntry(String csvLevels, HibernateFunction function) {
        this(csvLevels, null, function);
    }

    /**
     * Konstruktor inicjalizujacy obiekt.
     *
     * @param levels wartosci kolejnych poziomow
     */
    public HibernateParameterEntry(String... levels) {
        this(levels, null, null);
    }

    /**
     * Metoda modyfikuje rozmiar tablicy <tt>levels</tt>,
     * tak by zagwarantowac, ze tablica pomiesci <tt>capacity</tt> elementow.
     *
     * @param capacity potrzebna pojemnosc tablicy
     */
    private void ensureLevelCapacity(int capacity) {
        int oldCapacity = levels.length;
        if (capacity > oldCapacity) {
            String[] oldLevels = levels;
            levels = new String[capacity];
            System.arraycopy(oldLevels, 0, levels, 0, oldCapacity);
        }
    }

    /**
     * Getter dla klucza glownego.
     *
     * @return klucz glowny
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_parameter_entry")
    @SequenceGenerator(name = "seq_parameter_entry", sequenceName = "seq_parameter_entry")
    public int getId() {
        return id;
    }

    /**
     * Setter dla id.
     *
     * @param id klucz glowny
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter dla function.
     *
     * @return funkcja
     */
    @ManyToOne(fetch = FetchType.EAGER)
    public HibernateFunction getFunction() {
        return function;
    }

    /**
     * Setter dla function.
     *
     * @param function funkcja
     */
    public void setFunction(HibernateFunction function) {
        this.function = function;
    }

    /**
     * Zwraca wzorzec dopasowania.
     *
     * @return levels
     */
    @Transient
    public String[] getLevels() {
        return levels;
    }

    /**
     * Zwraca n pierwszych wartosci z wzorca dopasowania.
     * Innymi slowy - zwraca wartosci dla n pierwszych poziomow.
     *
     * @param n liczba zadanych poziomow
     * @return wartosci dla n poziomow
     */
    public String[] getLevels(int n) {
        return Arrays.copyOf(levels, n);
    }

    /**
     * Zwraca wzorzec dla k-tego poziomu. Numerowanie zaczyna sie od 1 (k = 1,...)
     *
     * @param k numer poziomu
     * @return wzorzec dla k-tego poziomu lub <tt>null</tt>, jesli nie ma takiego poziomu
     */
    public String getLevel(int k) {
        return (k >= 1 && k <= levels.length) ? levels[k - 1] : null;
    }

    /**
     * Setter dla wzorca dopasowania.
     *
     * @param levels wzorzec dopasowania
     */
    public final void setLevels(String[] levels) {
        this.levels = levels != null ? levels : EMPTY_ARRAY;
    }

    /**
     * Ustawia wzorzec dopasowania dla k-tego poziomu (k = 1, ..., n).
     *
     * @param k       numer poziomu
     * @param pattern wzorzec dla k-tego poziomu
     *
     * @throws ParamDefinitionException errorCode={@link ErrorCode#INDEX_OUT_OF_BOUNDS} jesli k jest ujemne
     */
    public void setLevel(int k, String pattern) {
        ensureLevelCapacity(k);

        if (k <= 0) {
            throw new ParamDefinitionException(ErrorCode.INDEX_OUT_OF_BOUNDS, "Setting illegal level position: " + k);
        }

        levels[k - 1] = pattern;
    }

    /**
     * Getter dla parametru, do ktorego nalezy ten wiersz.
     *
     * @return parametr
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public HibernateParameter getParameter() {
        return parameter;
    }

    /**
     * Setter dla parametru.
     *
     * @param parameter parametr
     */
    public void setParameter(HibernateParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * Zwraca wartosc wiersza.
     *
     * @return value
     */
    @Lob
    public String getValue() {
        return value;
    }

    /**
     * Setter dla wartosci wiersza.
     *
     * @param value wartosc wiersza
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Zwraca wzorzec dla 1-go poziomu.
     *
     * @return wartosc dla 1-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel1() {
        return getLevel(L1);
    }

    /**
     * Zwraca wzorzec dla 2-go poziomu.
     *
     * @return wartosc dla 2-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel2() {
        return getLevel(L2);
    }

    /**
     * Zwraca wzorzec dla 2-go poziomu.
     *
     * @return wartosc dla 3-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel3() {
        return getLevel(L3);
    }

    /**
     * Zwraca wzorzec dla 4-go poziomu.
     *
     * @return wartosc dla 4-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel4() {
        return getLevel(L4);
    }

    /**
     * Zwraca wzorzec dla 5-go poziomu.
     *
     * @return wartosc dla 5-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel5() {
        return getLevel(L5);
    }

    /**
     * Zwraca wzorzec dla 6-go poziomu.
     *
     * @return wartosc dla 6-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel6() {
        return getLevel(L6);
    }

    /**
     * Zwraca wzorzec dla 7-go poziomu.
     *
     * @return wartosc dla 7-go poziomu
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel7() {
        return getLevel(L7);
    }

    /**
     * Zwraca wartosc dla 8-go poziomu.
     * Jesli poziomow jest wiecej niz 8, zwraca poziom 8 i kolejne skonkatenowane znakiem srednika.
     *
     * @return wartosci dla 8-go poziomu (i ew. kolejnych)
     */
    @Column(length = LEVEL_COLUMN_LENGTH)
    public String getLevel8() {

        /*
         * jesli liczba rzeczywistych poziomow jest wieksza od liczby kolumn (MAX_LEVELS),
         * zlaczamy kolumny 8, ..., N w jeden string (separatorem jest znak srednika)
         */
        if (levels.length > MAX_LEVELS) {
            return StringUtils.join(Arrays.copyOfRange(levels, MAX_LEVELS - 1, levels.length), ';');
        }

        return getLevel(L8);
    }

    /**
     * Setter dla wartosci 1-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel1(String pattern) {
        setLevel(L1, pattern);
    }

    /**
     * Setter dla wartosci 2-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel2(String pattern) {
        setLevel(L2, pattern);
    }

    /**
     * Setter dla wartosci 3-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel3(String pattern) {
        setLevel(L3, pattern);
    }

    /**
     * Setter dla wartosci 4-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel4(String pattern) {
        setLevel(L4, pattern);
    }

    /**
     * Setter dla wartosci 5-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel5(String pattern) {
        setLevel(L5, pattern);
    }

    /**
     * Setter dla wartosci 6-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel6(String pattern) {
        setLevel(L6, pattern);
    }

    /**
     * Setter dla wartosci 7-go poziomu.
     *
     * @param pattern wzorzec
     */
    public void setLevel7(String pattern) {
        setLevel(L7, pattern);
    }

    /**
     * Setter dla wartosci 8-go poziomu.
     * Jesli wzorzec zawiera znaki srednika, jest traktowany jako wzorzec dla 8-go i kolejnych poziomow.
     *
     * @param pattern wzorzec
     */
    public void setLevel8(String pattern) {

        // ostatnia kolumna zawiera co najmniej 2 poziomy
        if (pattern != null && pattern.indexOf(';') >= 0) {
            String[] ext = EngineUtil.split(pattern, ';');
            ensureLevelCapacity(MAX_LEVELS - 1 + ext.length);
            System.arraycopy(ext, 0, levels, MAX_LEVELS - 1, ext.length);

        } // ostatnia kolumna zawiera tylko 1 poziom
        else {
            setLevel(L8, pattern);
        }
    }
    /**
     * Maksymalna liczba poziomow majacych swoje kolumny.
     */
    private static final int MAX_LEVELS = 8;

    /**
     * Numer pierwszego poziomu.
     */
    private static final int L1 = 1;

    /**
     * Number drugiego poziomu.
     */
    private static final int L2 = 2;

    /**
     * Numer trzeciego poziomu.
     */
    private static final int L3 = 3;

    /**
     * Numer czwartego poziomu.
     */
    private static final int L4 = 4;

    /**
     * Number piatego poziomu.
     */
    private static final int L5 = 5;

    /**
     * Numer szostego poziomu.
     */
    private static final int L6 = 6;

    /**
     * Numer siodmego poziomu.
     */
    private static final int L7 = 7;

    /**
     * Numer osmego poziomu.
     */
    private static final int L8 = 8;

    /**
     * Maksymalna liczba znakow dopuszczalnych we wzorcach poziomow.
     */
    private static final int LEVEL_COLUMN_LENGTH = 256;

    /**
     * Pusta tablica poziomow.
     */
    private static final String[] EMPTY_ARRAY = new String[0];

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParameterEntry[#").append(id);
        sb.append(' ');
        sb.append(Arrays.toString(levels));
        sb.append(" v=").append(value);
        sb.append(" f=").append(function != null ? function.getName() : null);
        sb.append(']');
        return sb.toString();
    }
}