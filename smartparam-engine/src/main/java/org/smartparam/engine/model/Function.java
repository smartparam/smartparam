package org.smartparam.engine.model;

import javax.persistence.*;

/**
 * Klasa reprezentuje funkcje z tzw. <b>repozytorium funkcji</b>.
 * <p>
 *
 * Kazda funkcja z repozytorium ma unikalna nazwe, po ktorej moze byc jednoznacznie rozpoznawana i wczytywana.
 * Funkcje maja rozne zastosowania i sa szeroko stosowane przez silnik parametryczny.
 * O przeznaczeniu funkcji decyduja m.in. nastepujace flagi:
 *
 * <ul>
 * <li><tt>versionSelector</tt> - ustawiona oznacza, ze funkcja moze byc uzywana do wybierania wersji na podstawie daty
 * <li><tt>levelCreator</tt> - ustawiona oznacza, ze funkcja moze byc uzywana do dynamicznego pobierania wartosci poziomu
 * <li><tt>plugin</tt> - ustawiona oznacza, ze funkcja jest dowolnego przeznaczenia i moze byc uzywana jako plugin
 * </ul>
 *
 * Funkcje typu <tt>versionSelector</tt> i <tt>levelCreator</tt> przyjmuja zawsze jeden argument typu <tt>ParamContext</tt>.
 * Funkcje typu <tt>plugin</tt> moga przyjmowac dowolna liczbe argumentow dowolnego typu.
 * <p>
 *
 * Sposob implementacji funkcji jest kwestia wtorna - funkcja moze byc zrealizowana przy pomocy
 * dowolnej implementacji. Dostepne implementacje sa okreslone przez klasy rozszerzajace
 * klase {@link FunctionImpl}.
 * <p>
 *
 * Dodatkowo, w celach informacyjnych, funkcja moze miec okreslony typ zgodny z systemem typow silnika.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Entity
@Table(name = "par_function")
public class Function implements ParamModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Id funkcji.
     */
    private int id;

    /**
     * Unikalna nazwa funkcji.
     */
    private String name;

    /**
     * Opcjonalny typ funkcji zgodny z systemem typow silnika:
     * {@link org.smartparam.engine.core.config.TypeProvider}
     */
    private String type;

    /**
     * Flaga, czy funkcja moze byc uzywana do zwracania daty wybierajacej wersje.
     */
    private boolean versionSelector;

    /**
     * Flaga, czy funkcja moze byc uzywana do dynamicznego okreslania wartosci poziomu.
     */
    private boolean levelCreator;

    /**
     * Flaga, czy funkcja jest pluginowa, czyli ogolnego przeznaczenia.
     */
    private boolean plugin;

    /**
     * Konkretna implementacja funkcji.
     */
    private FunctionImpl implementation;

    /**
     * Getter dla id.
     *
     * @return identyfikator
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_function")
    @SequenceGenerator(name = "seq_function", sequenceName = "seq_function")
    public int getId() {
        return id;
    }

    /**
     * Setter dla id.
     *
     * @param id identyfikator
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter dla levelCreator.
     *
     * @return czy funkcja jest level-creatorem
     */
    @Column
    public boolean isLevelCreator() {
        return levelCreator;
    }

    /**
     * Setter dla levelCreator.
     *
     * @param levelCreator wartosc flagi
     */
    public void setLevelCreator(boolean levelCreator) {
        this.levelCreator = levelCreator;
    }

    /**
     * Getter dla nazwy funkcji.
     *
     * @return unikalna (w ramach repozytorium) nazwa funkcji
     */
    @Column(unique = true)
    public String getName() {
        return name;
    }

    /**
     * Setter dla nazwy funkcji.
     *
     * @param name unikalna (w ramach repozytorium) nazwa funkcji
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter dla typu zwracanego.
     *
     * @return kod typu
     */
    @Column(length = SHORT_COLUMN_LENGTH)
    public String getType() {
        return type;
    }

    /**
     * Setter dla typu zwracanego.
     *
     * @param type kod typu
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Czy funkcja moze pelnic role version-selectora.
     *
     * @return wartosc flagi
     */
    @Column
    public boolean isVersionSelector() {
        return versionSelector;
    }

    /**
     * Setter dla flagi versionSelector.
     *
     * @param versionSelector wartosc flagi
     */
    public void setVersionSelector(boolean versionSelector) {
        this.versionSelector = versionSelector;
    }

    /**
     * Czy funkcja jest pluginowa.
     *
     * @return wartosc flagi
     */
    @Column
    public boolean isPlugin() {
        return plugin;
    }

    /**
     * Setter dla flagi plugin.
     *
     * @param plugin wartosc flagi
     */
    public void setPlugin(boolean plugin) {
        this.plugin = plugin;
    }

    /**
     * Getter dla obiektu implementujacego funkcje.
     *
     * @return obiekt implementujacy
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    public FunctionImpl getImplementation() {
        return implementation;
    }

    /**
     * Setter dla obiektu implementujacego funkcje.
     *
     * @param implementation implementacja
     */
    public void setImplementation(FunctionImpl implementation) {
        this.implementation = implementation;
    }

    /**
     * Dokleja do bufora informacje o flagach, jesli tylko
     * funkcja posiada ktoras z flag.
     */
    private void appendFlags(StringBuilder sb) {
        if (isVersionSelector() || isLevelCreator() || isPlugin()) {
            sb.append(", flags=");
            if (isVersionSelector()) {
                sb.append('V');
            }
            if (isLevelCreator()) {
                sb.append('L');
            }
            if (isPlugin()) {
                sb.append('P');
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Function#").append(id);
        sb.append("[name=").append(name);
        sb.append(", type=").append(type);
        appendFlags(sb);
        sb.append(']');

        return sb.toString();
    }
}
