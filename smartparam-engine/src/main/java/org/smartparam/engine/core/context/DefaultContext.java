package org.smartparam.engine.core.context;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * Domyslna implementacja kontekstu (ParamContext).
 * Klasa zawiera jeden, generyczny konstruktor (vargs),
 * ktory wypelnia pola klasy zaleznie od przekazanych argumentow.
 * <p>
 * Kontekst jest zbiorem danych, ktore uzytkownik moze przekazac podczas
 * wywolywania metod silnika parametrycznego. Z danych kontekstu moga
 * nastepnie korzystac pozostale fragmenty silnika lub funkcje,
 * na przyklad: <i>level creatory</i>, <i>version selectory</i> itp.
 * <p>
 * Klasa zawiera 4 podstawowe struktury/koncepcje:
 * <ol>
 *
 * <li> <b>levelValues</b> - tablica stringow, ktore zostana uzyte jako wartosci
 * kolejnych poziomow w trakcie szukania pasujacego wzorca w macierzy parametru.
 * Jesli <tt>levelValues</tt> bedzie rowne <tt>null</tt>, silnik parametryczny
 * skorzysta z funkcji (<i>level creator</i>) do wyznaczenia wartosci dla kolejnych poziomow.
 * Wartosci poziomow mozna przekazac w konstruktorze jako obiekt typu <tt>String[]</tt>
 * lub skorzystac z settera {@link #withLevelValues(java.lang.Object[])}
 * lub wykorzystac dziedziczaca klase kontekstu {@link LevelValues}.
 *
 * <li> <b>resultClass</b> - oczekiwana klasa zwracanego przez silnik obiektu.
 * Uzytkownik moze zawolac metode API silnika (np. <tt>engine.get[param, ...]</tt>)
 * i oczekiwac, ze silnik zwroci obiekt klasy <tt>resultClass</tt>.
 * Oczekiwana klase mozna przekazac w konstruktorze jako jeden z argumentow
 * lub podac w setterze {@link #withResultClass(java.lang.Class)}.
 * Silnik odnajdzie wartosc parametru ({@link org.smartparam.engine.core.type.AbstractHolder})
 * a nastepnie uruchomi odpowiedni {@link org.smartparam.engine.core.assembler.Assembler},
 * zeby stworzyc obiekt klasy <tt>resultClass</tt>
 *
 * <li> <b>userContext</b> - mapa, do ktorej uzytkownik moze wstawic wszelkie
 * obiekty pod dowolnymi kluczami. Z tych obiektow moze nastepnie skorzystac
 * w funkcjach silnika (<i>level creatory</i>, <i>version selectory</i> itp)
 * przy pomocy metody {@link #get(java.lang.String)} lub {@link #get(java.lang.Class)}.
 * Elementy <tt>userContextu</tt> mozna przekazac w konstruktorze (zob. opis konstruktora)
 * lub poprzez settery {@link #set(java.lang.String, java.lang.Object)} i podobne.
 *
 * <li> <b>kontekst specjalizowany</b> - uzytkownik moze stworzyc wlasna klase
 * kontekstu (np. MyBankContext), ktora dziedziczy po tej klasie i zawiera
 * rozne pola z modelu dziedzinowego uzytkownika. Wystarczy zeby uzytkownik
 * zapewnil settery dla tych pol, a generyczny konstruktor kontekstu
 * bedzie potrafil je rozpoznac i wypelnic. Innymi slowy, kazdy obiekt przekazany
 * do konstruktora trafi do odpowiedniego settera, jesli tylko taki setter
 * bedzie dostepny w klasie kontekstu.
 * </ol>
 *
 * Sercem klasy {@link DefaultContext} jest algorytm inicjalizacji
 * zastosowany w konstruktorze.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class DefaultContext implements ParamContext {

    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory.getLogger(DefaultContext.class);

    /**
     * Cache setterow. Przechowuje pobrana przez refleksje metode settera okreslonej klasy.
     * Jesli danej metody nie ma, przechowuje informacje o braku okreslonego settera.
     * Przyspiesza inicjalizowanie obiektu ok. 3-4 razy.
     */
    private static Map<Class<?>, Map<Class<?>, Setter>> setterCache = new ConcurrentHashMap<Class<?>, Map<Class<?>, Setter>>();

    /**
     * Kontekst uzytkownika. Dowolne obiekty pod dowolnymi kluczami.
     * Jesli uzytkownik nie przekazal wlasnych obiektow, mapa userContext jest rowna null.
     */
    private Map<String, Object> userContext;

    /**
     * Wartosci poziomow dostarczone przez uzytkownika.
     * Jesli uzytkownik nie dostarczy tych wartosci, silnik sam je wyznaczy
     * przy pomocy level creatorow podpietych do poszczegonych leveli.
     */
    private String[] levelValues;

    /**
     * Oczekiwana przez uzytkownika klasa obiektu zwracanego przez silnik.
     */
    private Class<?> resultClass;

    /**
     * Wypelnia kontekst na podstawie luzno przekazanych argumentow (inline arguments).
     * Algorytm rozpoznawania i interpretowania kolejnych argumentow jest nastepujacy:
     * <ol>
     * <li>jesli <tt>arg[i]</tt> jest typu <tt>String[]</tt>, to jest przekazywany do metody {@link #setLevelValues(java.lang.String[])}.
     * <li>jesli <tt>arg[i]</tt> jest typu <tt>Object[]</tt>, to jest przekazywany do metody {@link #setLevelValues(java.lang.Object[])}.
     * <li>jesli <tt>arg[i]</tt> jest typu <tt>Class</tt>, to jest przekazywany do metody {@link #withResultClass(java.lang.Class)}.
     * <li>jesli <tt>arg[i]</tt> jest typu <tt>String</tt> i istnieje <tt>arg[i+1]</tt>, to <tt>arg[i+1]</tt>
     * jest wstawiany do <tt>userContext</tt> pod kluczem <tt>arg[i]</tt>, czyli: <tt>userContext.put(arg[i], arg[i+1])</tt>.
     * <li>jesli istnieje setter dla typu <tt>arg[i]</tt>, to jest uzywany ten setter.
     * <li>ostatecznie <tt>arg[i]</tt> jest wstawiany do <tt>userContext</tt> pod kluczem bedacym nazwa klasy argumenty,
     * czyli: <tt>userContext.put(arg[i].getClass().getSimpleName(), arg[i])</tt>.
     * </ol>
     *
     * @throws ParamUsageException jesli wystapi blad podczas interpretowania ciagu argumentow
     */
    public DefaultContext(Object... args) {
        initialize(args);
    }

    /**
     * Tworzy pusty konktekst.
     * Po stworzeniu mozna go wypelnic odpowiednimi metodami ustawiajacymi.
     *
     * @see #setLevelValues(java.lang.String[])
     * @see #setLevelValues(java.lang.Object[])
     * @see #setResultClass(java.lang.Class)
     * @see #set(java.lang.String, java.lang.Object)
     * @see #set(java.lang.Object)
     *
     * @see #DefaultContext(java.lang.Object[])
     */
    public DefaultContext() {
    }

    /**
     * Algorytm inicjalizacji opisany jest przy okazji konstruktora.
     *
     * @see #DefaultContext(java.lang.Object[])
     * @param args luzno przekazane argumenty
     */
    protected final void initialize(Object... args) {

        for (int i = 0; i < args.length; ++i) {
            Object arg = get(args, i);

            if (arg instanceof String[]) {
                setLevelValues((String[]) arg);

            } else if (arg instanceof Object[]) {
                setLevelValues((Object[]) arg);

            } else if (arg instanceof Class) {
                setResultClass((Class<?>) arg);

            } else if (arg instanceof String) {
                // arg jest kluczem dla kolejnego argumentu, arg[i+1] wstawiamy do userContext pod kluczem arg
                set((String) arg, get(args, ++i));

            } else if (arg != null) {
                // sprawdzamy, czy jest setter dla klasy arg (metoda cache'owana)
                Method setter = findSetter(arg);

                if (setter != null) {
                    // istnieje setter dla tego argumentu, uzywamy tego settera
                    setArg(setter, arg);
                } else {
                    // wstawiamy argument pod kluczem bedacym nazwa klasy
                    set(arg);
                }
            }
        }
    }

    /**
     * Wstawia do kontekstu uzytkownika obiekt <tt>value</tt> pod kluczem <tt>lowercase(key)</tt>.
     *
     * @param key   klucz, ktory zostanie zamieniony na lowercase(key)
     * @param value wartosc
     * @throws ParamUsageException jesli jest juz obiekt pod takim kluczem
     */
    public final DefaultContext set(String key, Object value) {
        return set(key, value, false);
    }

    /**
     * Wstawia do kontekstu uzytkownika obiekt <tt>value</tt> pod kluczem <tt>lowercase(key)</tt>.
     * Uzycie drugi raz tego samego klucza:
     * <ul>
     * <li>nadpisuje poprzednia wartosc, gdy <tt>allowOverwrite=true</tt>,
     * <li>rzuca wyjatek ParamUsageException, gdy <tt>allowOverwrite=false</tt>.
     * </ul>
     *
     * @param key            klucz, pod ktorym wstawiamy obiekt
     * @param value          wstawiany obiekt
     * @param allowOverwrite czy dopuszczalne jest nadpisywanie juz istniejacego klucza
     * @throws ParamUsageException jesli duplikujemy klucz a nie pozwalamy na nadpisywanie
     */
    public final DefaultContext set(String key, Object value, boolean allowOverwrite) {
        if (userContext == null) {
            userContext = new TreeMap<String, Object>();
        }

        String k = lowercase(key);
        if (userContext.containsKey(k) && !allowOverwrite) {
            throw new SmartParamUsageException(SmartParamErrorCode.ERROR_FILLING_CONTEXT,
                    "Trying to set duplicate key on userContext: key=" + key);
        }

        userContext.put(k, value);
        return this;
    }
    /**
     * Wstepnie zainicjalizowane domyslne Locale, na potrzeby przyspieszenia
     * metody {@link #lowercase(java.lang.String)}
     */
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    /**
     * Zwraca lowercase(str).
     */
    private String lowercase(final String str) {
        return str.toLowerCase(DEFAULT_LOCALE);
    }

    /**
     * Wstawia do kontekstu uzytkownika obiekt <tt>value</tt> pod kluczem <tt>lowercase(value.class.getSimpleName())</tt>.
     *
     * @param value wstawiany obiekt
     * @throws ParamUsageException jesli duplikujemy klucz, czy gdy wstawiamy drugi obiekt tej samej klasy
     */
    public final DefaultContext set(Object value) {
        return set(value.getClass().getSimpleName(), value);
    }

    /**
     * Pobiera z kontekstu uzytkownika obiekt o kluczu <tt>key</tt>.
     *
     * @param key - wielkosc liter nie ma znaczenia
     * @return obiekt spod klucza <tt>key</tt> lub <tt>null</tt>, gdy nie ma obiektu pod takim kluczem
     */
    public Object get(String key) {
        return userContext != null ? userContext.get(lowercase(key)) : null;
    }

    /**
     * Pobiera z kontekstu uzytkownika obiekt klasy <tt>clazz</tt> lub pochodnej.
     * Algorytm:
     * <ol>
     * <li>Szukany jest obiekt pod kluczem bedacym nazwa klasy, jesli obiekt jest i jest klasy <tt>clazz</tt>, to zostaje zwrocony.
     * <li>Szukany jest obiekt o klasie rownej <tt>clazz</tt> lub pochodnej.
     * </ol>
     *
     * @param clazz klasa szukanego obiektu
     * @return obiekt klasy clazz lub pochodnej
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {

        if (userContext != null) {

            Object obj = get(clazz.getSimpleName());
            if (obj != null && obj.getClass() == clazz) {
                return (T) obj;
            }

            for (Object x : userContext.values()) {
                if (x == null) {
                    continue;
                }

                if (clazz.isAssignableFrom(x.getClass())) {
                    return (T) x;
                }
            }
        }

        return null;
    }

    /**
     * Zwraca argument o indeksie ix.
     *
     * @param args tablica argumentow
     * @param ix   indeks argumentu (0-based)
     * @return args[ix]
     * @throws ParamUsageException jesli tablica jest krotsza
     */
    private Object get(Object[] args, int ix) {
        if (ix < args.length) {
            return args[ix];
        }

        throw new SmartParamUsageException(
                SmartParamErrorCode.ERROR_FILLING_CONTEXT,
                "args[" + ix + "] expected, but not found");
    }

    /**
     * Znajduje setter dla klasy takiej jak klasa argumentu <tt>arg</tt>.
     * Pobiera metode z cache'a lub szuka jej przez refleksje.
     *
     * @param arg obiekt, dla ktorego szukay settera
     * @return metoda bedace setterm lub <tt>null</tt>, jesli nie ma settera dla obiektu tej klasy
     */
    private Method findSetter(Object arg) {
        Class<?> argClass = arg.getClass();
        Map<Class<?>, Setter> settersMap = setterCache.get(getClass());
        if (settersMap == null) {
            settersMap = new ConcurrentHashMap<Class<?>, Setter>();
            setterCache.put(getClass(), settersMap);
        }

        Setter setter = settersMap.get(argClass);

        if (setter == null) {
            Method method = lookupSetter(getClass(), argClass);
            setter = new Setter(method);
            settersMap.put(argClass, setter);
        }

        return setter.getMethod();
    }

    /**
     * Przeszukuje klase <tt>clazz</tt>, a nastepnie cala hierarchie dziedziczenia,
     * w poszukiwaniu 1-argumentowej metody <tt>void</tt>, ktora przyjmuje
     * argument klasy <tt>clazz</tt> lub nadrzednej.
     *
     * @param objClazz klasa, w ktorej szukamy settera
     * @param argClazz klasa argumentu, dla ktorego szukamy settera
     * @return metoda, ktora jest setterem wg. konwencji javabeans
     */
    private Method lookupSetter(Class<?> objClazz, Class<?> argClazz) {
        Class<?> clazz = objClazz;

        // przeszukujemy hierarchie dziedziczenia az do znalezienia metody
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                Class<?>[] argTypes = m.getParameterTypes();
                if (m.getReturnType() == Void.TYPE && argTypes.length == 1 && argTypes[0].isAssignableFrom(argClazz)) {

                    AccessController.doPrivileged(new AccessibleSetter(m));
                    return m;
                }
            }

            // przechodzimy do klasy bazowej
            clazz = clazz.getSuperclass();
        }

        // setter nie zostal znaleziony mimo przeszukania calej hierarchii dziedziczenia
        return null;
    }

    /**
     * Ustawia obiekt <tt>arg</tt> przy pomocy settera <tt>setter</tt>.
     *
     * @param setter metoda settera
     * @param arg    argument settera
     * @throws ParamUsageException jesli wywolanie settera sie nie powiodlo
     */
    private void setArg(Method setter, Object arg) {
        try {
            setter.invoke(this, arg);
        } catch (Exception e) {
            logger.error("", e);
            throw new SmartParamUsageException(
                    SmartParamErrorCode.ERROR_FILLING_CONTEXT, e,
                    "Unable to set arg on context, arg=" + arg + ", setter=" + setter);
        }
    }

    @Override
    public String[] getLevelValues() {
        return levelValues;
    }

    /**
     * Setter dla tablicy levelValues.
     *
     * @param levelValues przygotowane wartosci poziomow
     */
    @Override
    public final void setLevelValues(String... levelValues) {
        this.levelValues = levelValues;
    }

    /**
     * Ustawia tablice przygotowanych wartosci poziomow.
     * Kazdy element tej tablicy bedzie wynikiem metody <tt>toString</tt>
     * odpowiedniego elementu z tablicy obiektow <tt>levelValues</tt>.
     * Wartosc <tt>null</tt> jest przepisywana jako <tt>null</tt>.
     *
     * @param levelValues tablica obiektow
     */
    public final void setLevelValues(Object... levelValues) {
        this.levelValues = new String[levelValues.length];
        for (int i = 0; i < levelValues.length; ++i) {
            Object v = levelValues[i];
            this.levelValues[i] = v != null ? v.toString() : null;
        }
    }

    /**
     * Odpowiednik {@link #setLevelValues(java.lang.String[])},
     * z tym, ze zwraca referencje do biezacego obiektu pozwalajac tym samym na chaining.
     */
    public DefaultContext withLevelValues(String... levelValues) {
        setLevelValues(levelValues);
        return this;
    }

    /**
     * Odpowiednik {@link #setLevelValues(java.lang.Object[])},
     * z tym, ze zwraca referencje do biezacego obiektu pozwalajac tym samym na chaining.
     */
    public DefaultContext withLevelValues(Object... levelValues) {
        setLevelValues(levelValues);
        return this;
    }

    @Override
    public Class<?> getResultClass() {
        return resultClass;
    }

    /**
     * Zwraca mape reprezentujaca kontekst uzytkownika.
     *
     * @return userContext
     */
    protected Map<String, Object> getUserContext() {
        return userContext;
    }

    /**
     * Setter dla <tt>resultClass</tt>.
     *
     * @param resultClass klasa oczekiwanego obiektu wynikowego
     */
    @Override
    public final void setResultClass(Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    /**
     * Odpowiednik {@link #setResultClass(java.lang.Class)},
     * z tym, ze zwraca referencje do biezacego obiektu pozwalajac tym samym na chaining.
     */
    public DefaultContext withResultClass(Class<?> resultClass) {
        setResultClass(resultClass);
        return this;
    }

    /**
     * Wrapper dla obiektu <tt>Method</tt>.
     */
    static final class Setter {

        private Method method;

        Setter(Method method) {
            this.method = method;
        }

        Method getMethod() {
            return method;
        }
    }

    @Override
    public String toString() {
        return "DefaultContext[levelValues=" + Arrays.toString(levelValues) + ", userContext=" + userContext + ']';
    }

    /**
     * Klasa callbackowa, ktora ustawia flage dostepnosci na metodzie.
     */
    static final class AccessibleSetter implements PrivilegedAction<Object> {

        private Method method;

        private AccessibleSetter(Method method) {
            this.method = method;
        }

        @Override
        public Object run() {
            method.setAccessible(true);
            return null;
        }
    }

    protected void append(StringBuilder sb, String property, Object value) {
        sb.append(property).append('=').append(value).append(", ");
    }

    protected void append(StringBuilder sb, String property) {
        sb.append(property).append(", ");
    }
}
