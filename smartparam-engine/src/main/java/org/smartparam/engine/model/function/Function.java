package org.smartparam.engine.model.function;

/**
 * Klasa reprezentuje funkcje z tzw. <b>repozytorium funkcji</b>.
 * <p>
 *
 * Kazda funkcja z repozytorium ma unikalna nazwe, po ktorej moze byc
 * jednoznacznie rozpoznawana i wczytywana. Funkcje maja rozne zastosowania i sa
 * szeroko stosowane przez silnik parametryczny. O przeznaczeniu funkcji
 * decyduja m.in. nastepujace flagi:
 *
 * <ul>
 * <li><tt>versionSelector</tt> - ustawiona oznacza, ze funkcja moze byc uzywana
 * do wybierania wersji na podstawie daty
 * <li><tt>levelCreator</tt> - ustawiona oznacza, ze funkcja moze byc uzywana do
 * dynamicznego pobierania wartosci poziomu
 * <li><tt>plugin</tt> - ustawiona oznacza, ze funkcja jest dowolnego
 * przeznaczenia i moze byc uzywana jako plugin
 * </ul>
 *
 * Funkcje typu <tt>versionSelector</tt> i <tt>levelCreator</tt> przyjmuja
 * zawsze jeden argument typu <tt>ParamContext</tt>. Funkcje typu
 * <tt>plugin</tt> moga przyjmowac dowolna liczbe argumentow dowolnego typu.
 * <p>
 *
 * Sposob implementacji funkcji jest kwestia wtorna - funkcja moze byc
 * zrealizowana przy pomocy dowolnej implementacji. Dostepne implementacje sa
 * okreslone przez klasy rozszerzajace klase {@link FunctionImpl}.
 * <p>
 *
 * Dodatkowo, w celach informacyjnych, funkcja moze miec okreslony typ zgodny z
 * systemem typow silnika.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class Function {

    private String name;

    private String type;

    public Function(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns unique name of function.
     *
     * @return name of function
     */
    public String getName() {
        return name;
    }

    /**
     * Function type, compatible with one of registered
     * {@link org.smartparam.engine.core.config.TypeProvider}.
     *
     * @return function type
     */
    public String getType() {
        return type;
    }
}
