package org.smartparam.engine.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.assembler.AssemblerAnnotationScanner;
import org.smartparam.engine.core.assembler.AssemblerMethod;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.util.Printer;

/**
 * Klasa udostepnia usluge (Service Provider) znajdowania metody <b>assemblera</b>,
 * ktory potrawi zmontowac (assemble) obiekt wynikowy (<tt>target</tt>)
 * z podanego obiektu zrodlowego (<tt>source</tt>).
 * <p>
 * Assemblery sa dowolnymi metodami, ktore zostay oznaczone przy pomocy
 * adnotacji {@link org.smartparam.engine.core.assembler.Assembler}.
 * <p>
 * Nazwa <tt>Assembler owner</tt> jest uzywana na okreslenie obiektu,
 * ktory zawiera jedna lub wiecej metod oznaczonych jako <tt>@Assembler</tt>.
 * <p>
 * Uzycie klasy {@link AssemblerProvider} jest 2-etapowe:
 * <ol>
 * <li>
 * Najpierw nalezy skonfigurowac provider poprzez zarejestrowanie <i>assembler ownerow</i>
 * przy pomocy metody {@link #registerAssemblerOwner(java.lang.Object)}
 * lub metody {@link #setAssemblerObjects(java.util.List)}.
 * <li>
 * Nastepnie mozna korzystac z uslugi poprzez metode
 * {@link #findAssembler(java.lang.Class, java.lang.Class)},
 * ktora znajduje najbardziej pasujacy assembler, to znaczy
 * assembler, ktory potrafi z obiektu klasy <tt>source</tt>
 * zbudowac obiekt klasy <tt>target</tt>.
 * Algorym znajdowania najlepiej pasujacego assemblera
 * opisany jest przy metodzie <tt>findAssembler</tt>.
 * </ol>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class AssemblerProvider {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(AssemblerProvider.class);

    /**
     * Wszystkie zarejestrowane assemblery.
     */
    private List<AssemblerMethod> assemblers = new ArrayList<AssemblerMethod>();

    /**
     * Cache, wykorzystywany przez metode {@link #findAssembler(java.lang.Class, java.lang.Class)}.
     */
    private Map<SignatureKey, AssemblerMethod> cache = new ConcurrentHashMap<SignatureKey, AssemblerMethod>();

    /**
     * Rejestruje wszystkie assemblery, ktore znajdzie w obiekcie <tt>owner</tt>.
     * Przeszukuje metody (w klasie owner oraz calej hierarchii dziedziczenia),
     * ktore sa oznaczone przy pomocy adnotacji <tt>@Assembler</tt>.
     * Kolejnosc rejestrowania assemblerow nie musi byc zgodna
     * z kolejnoscia wystepowania metod w kodzie zrodlowym.
     *
     * @param owner obiekt assembler owner
     */
    public void registerAssemblerOwner(Object owner) {
        logger.info("registering owner: {}", owner.getClass());
        AssemblerAnnotationScanner scanner = new AssemblerAnnotationScanner();
        List<AssemblerMethod> methods = scanner.scan(owner);
        for (AssemblerMethod asm : methods) {
            registerAssembler(asm);
        }
    }

    /**
     * Metoda przeszukuje zarejestrowane assemblery w poszukiwaniu takiego,
     * ktory <b>najlepiej</b> spelnia nastepujace kryterium:
     * Jako argument przyjmuje obiekt klasy <tt>source</tt> (lub potomnej)
     * i zwraca obiekt klasy <tt>target</tt> (lub nadrzednej).
     * <p>
     * Algorytm znajdowania metody assemblera jest opisany przy okazji
     * metody {@link #lookupAssembler(java.lang.Class, java.lang.Class)}.
     * <p>
     * Metoda korzysta z cache'a - przeszukuje zarejstrowane assemblery
     * tylko raz dla danej pary <tt>(source, target)</tt>.
     *
     * @see #lookupAssembler(java.lang.Class, java.lang.Class)
     *
     * @param source klasa obiektu podawanego na wejscie assemblera
     * @param target klasa obiektu otrzymywanego na wyjsciu assemblera
     * @return metoda assemblera lub <tt>null</tt>, jesli nie znaleziono pasujacego assemblera
     */
    public AssemblerMethod findAssembler(Class<?> source, Class<?> target) {
        SignatureKey key = new SignatureKey(source, target);

        // odszukanie assemblera w cache'u
        AssemblerMethod asm = cache.get(key);
        if (asm != null) {
            return asm;
        }

        // wyszukania assemblera sposrod wszystkich zarejstrowanych
        asm = lookupAssembler(source, target);
        if (asm != null) {
            cache.put(key, asm);
            return asm;
        }

        // nie znaleziono pasujacego assemblera
        throw new ParamException(SmartParamErrorCode.ASSEMBLER_NOT_FOUND, "matching assembler not found: source=" + source + ", target=" + target);
    }

    /**
     * Metoda przeszukuje zarejestrowane assemblery w poszukiwaniu takiego,
     * ktory <b>najlepiej</b> spelnia nastepujace kryterium:
     * Jako argument przyjmuje obiekt klasy <tt>source</tt> (lub potomnej)
     * i zwraca obiekt klasy <tt>target</tt> (lub nadrzednej).
     * Dodatkowo metoda assemblera moze przyjmowac jako drugi (opcjonalny)
     * argument typu {@link org.smartparam.engine.core.context.ParamContext}
     * lub potomny.
     * <p>
     * Algorytm znajdowania metody assemblera jest nastpujacy.
     * <ol>
     * <li>
     * W pierwszym kroku szukany jest assembler,
     * ktory zwraca obiekt klasy <tt>target</tt> i przyjmuje argument klasy <tt>source</tt> lub nadrzednej.
     * <li>
     * W drugim kroku szukany jest assembler,
     * ktory zwraca obiekt klasy <tt>target</tt> lub potomnej i przyjmuje argument klasy <tt>source</tt>.
     * <li>
     * W trzecim kroku szukany jest assembler,
     * ktory zwraca obiekt klasy <tt>target</tt> lub potomnej i przyjmuje argument klasy <tt>source</tt> lub nadrzednej.
     * <li>
     * W czwartym kroku szukany jest assembler,
     * ktory zwraca obiekt klasy <b>nadrzednej</b> do <tt>target</tt> i przyjmuje argument klasy <tt>source</tt> lub nadrzednej.
     * <li>
     * Jesli assembler nie zostal znaleziony na zadnym etapie, zwracany jest null.
     * </ol>
     *
     * Wynik wyszukania assemblera trafia do cache'a a nastepnie jest uzywany przez
     * metode {@link #findAssembler(java.lang.Class, java.lang.Class)}.
     *
     * @see #findAssembler(java.lang.Class, java.lang.Class)
     *
     * @param source klasa obiektu podawanego na wejscie assemblera
     * @param target klasa obiektu otrzymywanego na wyjsciu assemblera
     * @return metoda assemblera lub <tt>null</tt>, jesli nie znaleziono pasujacego assemblera
     */
    AssemblerMethod lookupAssembler(Class<?> source, Class<?> target) {

        // twarda zgodnosc na target i miekka zgodnosc na source
        for (AssemblerMethod candidate : assemblers) {
            if (candidate.getTarget() == target && candidate.getSource().isAssignableFrom(source)) {
                return candidate;
            }
        }

        // miekka zgodnosc na target i twarda zgodnosc na source
        for (AssemblerMethod candidate : assemblers) {
            if (target.isAssignableFrom(candidate.getTarget()) && candidate.getSource() == source) {
                return candidate;
            }
        }

        // miekka zgodnosc na target i miekka zgodnosc na source
        for (AssemblerMethod candidate : assemblers) {
            if (target.isAssignableFrom(candidate.getTarget()) && candidate.getSource().isAssignableFrom(source)) {
                return candidate;
            }
        }

        // niepewna zgodnosc na target i miekka zgodnosc na source
        for (AssemblerMethod candidate : assemblers) {
            if (candidate.getTarget().isAssignableFrom(target) && candidate.getSource().isAssignableFrom(source)) {
                return candidate;
            }
        }

        // pasujacy assembler nie zostal znaleziony
        return null;
    }

    /**
     * Rejestruje pojedynczy assembler.
     *
     * @param asm assembler
     */
    private void registerAssembler(AssemblerMethod asm) {
        assemblers.add(asm);
        logger.info("registered assembler: {}", asm);
    }

    /**
     * Rejestruje wszystkie assemblery, ktore znajdzie w przekazanych obiektach (owners).
     * Kolejnosc rejestrowania assemblerow jest zgodna z kolejnoscia ownerow na liscie <tt>owners</tt>.
     * Natomiast w ramach danego obiektu ownera kolejnosc jest dowolna i nie musi byc
     * zgodna z kolejnoscia wystepowania metod w kodzie zrodlowym.
     *
     * @param owners lista ownerow
     */
    public void setAssemblerObjects(List<Object> owners) {
        for (Object owner : owners) {
            registerAssemblerOwner(owner);
        }
    }

    /**
     * Loguje wszystkie zarejstrowane assemblery oraz aktualna zawartosc cache'a.
     */
    public void logAssemblers() {
        logger.info(Printer.print(assemblers, "registered assemblers"));
        logger.info(Printer.print(cache.entrySet(), "cache"));
    }

    /**
     * Zwraca zajestrowane assemblery.
     * Metoda nie jest czescia publicznego API.
     *
     * @return lista zarejstrowanych assemblerow
     */
    List<AssemblerMethod> getAssemblers() {
        return assemblers;
    }

    static final class SignatureKey {

        private Class<?> source;

        private Class<?> target;

        SignatureKey(Class<?> source, Class<?> target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SignatureKey) {
                final SignatureKey other = (SignatureKey) obj;
                return this.source == other.source && this.target == other.target;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return source.hashCode() ^ target.hashCode();
        }

        @Override
        public String toString() {
            return "[source=" + source.getSimpleName() + ", target=" + target.getSimpleName() + ']';
        }
    }
}
