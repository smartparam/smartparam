package org.smartparam.provider.hibernate.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.smartparam.engine.model.Function;

/**
 * Hibernate data source based function implementation.
 *
 * @author Przemek Hertel
 * @since 0.1.0
 */
@Entity
@Table(name = "par_function")
public class HibernateFunction implements Function, ParamModelObject {

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
    private HibernateFunctionImpl implementation;

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
    public HibernateFunctionImpl getImplementation() {
        return implementation;
    }

    /**
     * Setter dla obiektu implementujacego funkcje.
     *
     * @param implementation implementacja
     */
    public void setImplementation(HibernateFunctionImpl implementation) {
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
