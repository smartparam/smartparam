package org.smartparam.engine.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Klasa reprezenuje definicje poziomu na poziomie parametru.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Entity
@Table(name = "par_level")
public class Level implements ParamModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Klucz glowny.
     */
    private int id;

    /**
     * Parametr, do ktorego nalezy ten poziom.
     */
    private Parameter parameter;

    /**
     * Numer porzadkowy poziomu. Wyznacza kolejnosc poziomow w ramach parametru.
     */
    private int orderNo;

    /**
     * Funkcja dynamicznie wyznaczajaca wartosc poziomu dla danego kontekstu.
     */
    private Function levelCreator;

    /**
     * Typ wartosci dla tego poziomu (zgodny z systemem typow silnika).
     * Musi byc <tt>not null</tt> jesli uzywamy niestandardowego matchera dla tego poziomu.
     */
    private String type;

    /**
     * Flaga oznaczajaca, czy zawartosc tego poziomu moze byc traktowana jako tablica wartosci.
     * Domyslnie <tt>false</tt>
     */
    private boolean array;

    /**
     * Kod matchera uzywanego dla tego poziomu.
     * Domyslnie <tt>null</tt> - uzywany jest wtedy domyslny sposob matchowania wartosci do wzorca:
     * wartosc pasuje do wzorca, gdy jest rowna wzorcowi w sensie <tt>equals</tt>.
     */
    private String matcherCode;

    /**
     * Funkcja (z repozytorium), ktora pelni role walidatora.
     * Moze byc uzywana prze GUI do walidacji wartosci wprowadzonych przez uzytkownika.
     */
    private Function validator;

    /**
     * Skrotowy opis (label) poziomu w jezyku naturalnym.
     */
    private String label;

    /**
     * Kod opisu poziomu, spod ktorego jest brany opis z pliku <i>message bundle</i>.
     */
    private String labelKey;

    /**
     * Konstruktor domyslny.
     */
    public Level() {
    }

    /**
     * Konstruktor inicjalizujacy typ poziomu.
     *
     * @param type typ poziomu (zgodny z systemem typow silnika)
     */
    public Level(String type) {
        this.type = type;
    }

    /**
     * Getter dla id.
     *
     * @return id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_level")
    @SequenceGenerator(name = "seq_level", sequenceName = "seq_level")
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
     * Getter dla parametru, do ktorego nalezy ten poziom.
     *
     * @return parametr
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Setter dla parametru.
     *
     * @param parameter parametr
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    /**
     * Zwraca numer porzadkowy poziomu.
     *
     * @return numer porzadkowy
     */
    @Column
    public int getOrderNo() {
        return orderNo;
    }

    /**
     * Setter dla orderNo.
     *
     * @param orderNo numer porzadkowy poziomu
     */
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
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
     * @param label opis
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter dla labelKey.
     *
     * @return klucz labelki
     */
    @Column
    public String getLabelKey() {
        return labelKey;
    }

    /**
     * Setter dla labelKey.
     *
     * @param labelKey klucz labelki
     */
    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    /**
     * Getter dla funkcji typu levelCrator.
     *
     * @return funkcja levelCreator
     */
    @ManyToOne(fetch = FetchType.EAGER)
    public Function getLevelCreator() {
        return levelCreator;
    }

    /**
     * Setter dla funkcji levelCreator.
     *
     * @param levelCreator funkcja
     */
    public void setLevelCreator(Function levelCreator) {
        this.levelCreator = levelCreator;
    }

    /**
     * Getter dla kodu matchera.
     *
     * @return matcherCode
     */
    @Column(length = SHORT_COLUMN_LENGTH)
    public String getMatcherCode() {
        return matcherCode;
    }

    /**
     * Setter dla kodu matchera.
     *
     * @param matcherCode kod matchera
     *
     * @return budowany level
     */
    public Level setMatcherCode(String matcherCode) {
        this.matcherCode = matcherCode;
        return this;
    }

    /**
     * Ustawia kod matchera pobierajac go z metody <tt>toString</tt> przekazanego enuma.
     *
     * @param e enum, ktorego <tt>toString</tt> zostanie uzyty jako kod matchera
     *
     * @return budowany level
     */
    public Level setMatcherCode(Enum<?> e) {
        this.matcherCode = e.toString();
        return this;
    }

    /**
     * Getter dla typu poziomu.
     *
     * @return kod typu
     */
    @Column(length = SHORT_COLUMN_LENGTH)
    public String getType() {
        return type;
    }

    /**
     * Setter dla typu poziomu
     *
     * @param type kod typu
     * @return
     */
    public Level setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Ustawia kod typu, ktory pobiera z metody <tt>toString</tt> przekazanego enuma.
     *
     * @param e enum, ktorego <tt>toString</tt> jest traktowany jako kod typu
     * @return
     */
    public Level setType(Enum<?> e) {
        return setType(e.toString());
    }

    /**
     * Czy zawartosc poziomu ma byc traktowana jako tablica.
     *
     * @return wartosc flagi array
     */
    @Column(name = "array_flag")
    public boolean isArray() {
        return array;
    }

    /**
     * Setter dla array.
     *
     * @param array flaga array
     *
     * @return konstruowany level
     */
    public Level setArray(boolean array) {
        this.array = array;
        return this;
    }

    /**
     * Getter dla funkcji validator.
     *
     * @return funkcja validator
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Function getValidator() {
        return validator;
    }

    /**
     * Setter dla funkcji validator.
     *
     * @param validator funkcja validator
     */
    public void setValidator(Function validator) {
        this.validator = validator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level[");
        sb.append("id=").append(id);
        sb.append(", cre=").append(levelCreator != null ? levelCreator.getName() : null);
        sb.append(", type=").append(type);

        if (matcherCode != null) {
            sb.append(", matcher=").append(matcherCode);
        }

        if (validator != null) {
            sb.append(", validator=").append(validator.getName());
        }

        sb.append(']');
        return sb.toString();
    }
}
