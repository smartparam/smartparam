package pl.generali.merkury.param.core.index;

import java.util.List;
import pl.generali.merkury.param.core.type.AbstractType;
import pl.generali.merkury.param.util.EngineUtil;
import pl.generali.merkury.param.util.Formatter;

/**
 * Podstawowa struktura danych pozwalajaca na efektywne wyszukiwanie
 * wartosci parametru dla zadanych wartosci poziomow. Ma zastosowanie
 * do parametrow przechowywanych w pamieci, czyli dla wiekszosci.
 * <p>
 * Struktura reprezentuje macierz parametru w postaci drzewa
 * o wysokosci rownej liczbie poziomow zdefiniowanych w parametrze.
 * <p>
 * Wyszukiwanie wartosci parametru dla zadanych wartosci levelow (poziomow)
 * jest realizowane przez rekurencyjny algorytm z powrotami.
 * Algorytm uwzglednia istnienie symbolu specjalnego (gwiazdki)
 * w macierzy parametru. Symbol ten oznacza dopasowanie dowolnej wartosci
 * poziomu, jednak sprawdzany jest dopiero, jesli zawiedzie jawne dopasowanie.
 * <p>
 * Dla przykladu wezmy parametr o 3 poziomach i wartosci typu Integer.
 * Macierz parametru sklada sie z 6 wierszy i 3 kolumn. Kazdy wiersz
 * jest wzorcem dopasowania (pattern). Budujemy index na podstawie
 * wzorcow z macierzy:
 * <pre>
 *      LevelIndex[Integer] ix = new LevelIndex[Integer](3);
 *      ix.add("A;B;C", 11);
 *      ix.add("A;B;A", 12);
 *      ix.add("A;*;*", 13);
 *      ix.add("*;*;*", 22);
 *      ix.add("*;*;E", 33);
 *      ix.add("*;E;*", 44);
 * </pre>
 * Zbudowany index ma nastepujaca strukture {@link #printTree()}:
 * <pre>
 * path :
 *    path : /A
 *       path : /A/B
 *          path : /A/B/A   (leaf=12)
 *          path : /A/B/C   (leaf=11)
 *       path : /A/?
 *          path : /A/?/?   (leaf=13)
 *    path : /?
 *       path : /?/E
 *          path : /?/E/?   (leaf=44)
 *       path : /?/?
 *          path : /?/?/E   (leaf=33)
 *          path : /?/?/?   (leaf=22)
 * </pre>
 * Wyszukiwanie realizuje metoda {@link #find(java.lang.String[])},
 * ktora w kilku krokach, znajduje wzorzec najlepiej pasujacy do
 * przekazanych wartosci poziomow. Dla znalezionego wzorca (czyli
 * sciezki w drzewie) zwraca skojarzona z nim wartosc (czyli lisc).
 * <p>
 * Zlozonosc metody <tt>find</tt> dla domyslnego Matchera (wiekszosc
 * przypadkow) jest niezalezna od rozmiaru parametru (liczby wierszy
 * macierzy parametru) i jest rzedu O(1). Zatem metoda find
 * wyszukuje wartosc w tzw. czasie stalym, o ile wszystkie poziomy
 * korzystaja z domyslnego Matchera.
 * <p>
 * Przyklad dzialania metody <tt>find</tt>:
 * <pre>
 *      ix.find("A", "B", "C") == 11        // wzorzec ABC
 *      ix.find("A", "B", "A") == 12        // wzorzec ABA
 *      ix.find("A", "D", "A") == 13        // wzorzec A**
 *      ix.find("B", "B", "A") == 22        // wzorzec ***
 *      ix.find("B", "B", "E") == 33        // wzorzec **E
 *      ix.find("B", "E", "E") == 44        // wzorzec *E*
 *      ix.find("A", "E", "E") == 13        // wzorzec A**
 * </pre>
 *
 * @param <T> typ indeksowanych wartosci, czyli typ liscia
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class LevelIndex<T> {

    /**
     * liczba poziomow w parametrze reprezentowanym przez ten index
     */
    private int levelCount;

    /**
     * korzen drzewa (indeksu)
     */
    private LevelNode<T> root;

    /**
     * Tablica Matcherow dla kolejnych poziomow. Zawsze rozna od null.
     * Element matchers[i] jest Matcherem dla poziomu o indeksie <tt>i+1</tt>.
     * Element matchers[i] moze byc null, jesli dla danego poziomu matcher jest domyslny.
     */
    private Matcher[] matchers;

    /**
     * Typy wartosci dla kolejnych poziomow. Tablica zawsze rozna od null.
     * Element types[i] jest typem dla poziomu <tt>i+1</tt>.
     * Element types[i] moze byc null, jesli dla danego poziomu typ nie jest okreslony.
     */
    private AbstractType<?>[] types;

    /**
     * Tworzy pusty index (bez drzewa) zainicjalizowany metadanymi.
     * Tablica typow jest opcjonalna. Jesli zostanie podana, moze byc krotsza niz <tt>levelCount</tt>.
     * Tablica matcherow jest opcjonalna. Jesli zostanie podana, moze byc krotsza niz <tt>levelCount</tt>.
     *
     * @param levelCount liczba poziomow
     * @param types      typy dla kolejnych poziomow, tablica moze byc krotsza od levelCount, moze zawierac nulle
     * @param matchers   matchery dla kolejnych poziomow, tablica moze byc krotsza, moze zawierac nulle
     */
    public LevelIndex(int levelCount, AbstractType<?>[] types, Matcher... matchers) {
        this.levelCount = levelCount;
        this.types = new AbstractType<?>[levelCount];
        this.matchers = new Matcher[levelCount];

        if (types != null) {
            System.arraycopy(types, 0, this.types, 0, types.length);
        }

        if (matchers != null) {
            System.arraycopy(matchers, 0, this.matchers, 0, matchers.length);
        }

        this.root = new LevelNode<T>(this);
    }

    /**
     * Tworzy pusty index z pustymi typami i domyslnymi matcherami.
     *
     * @param levelCount liczba poziomow
     */
    public LevelIndex(int levelCount) {
        this(levelCount, null);
    }

    /**
     * Tworzy pusty index z podanymi typami i domyslnymi matcherami.
     * Liczba poziomow bedzie rowna liczbie podanych typow.
     *
     * @param types typy dla kolejnych poziomow
     */
    public LevelIndex(AbstractType<?>[] types) {
        this(types.length, types);
    }

    /**
     * Tworzy pusty index z podanymi matcherami i pustymi typami.
     * Liczba poziomow bedzie rowna liczbie matcherow.
     *
     * @param matchers matchery dla kolejnych poziomow
     */
    public LevelIndex(Matcher... matchers) {
        this(matchers.length, null, matchers);
    }

    /**
     * Dodaje wzorzec dopasowania (levelValues) do indeksu
     * i kojarzy go z wartoscia wzorca (leaf).
     * <p>
     * Innymi slowy buduje sciezke w drzewie wyznaczona przez kolejne elementy wzoraca,
     * na koncu sciezki tworzy lisc o podanej wartosci (leaf).
     *
     * @param levelValues wzorzec dopasowania (wiersz z macierzy parametru)
     * @param leaf        wartosc parametru dla podanego wzoraca dopasowania
     */
    public void add(String[] levelValues, T leaf) {
        root.add(levelValues, leaf, matchers, 0);
    }

    /**
     * Convenience method. Dodaje wzorzec dopasowania do indeksu.
     * Kolejne wartosci wzorca sa oddzielone srednikiem i podane w jednym stringu.
     *
     * @param csvLevelValues oddzielone srednikiem wartosci wzoraca dopasowania
     * @param leaf           wartosc parametru dla podanego wzorca
     * @see #add(java.lang.String[], java.lang.Object)
     */
    public void add(String csvLevelValues, T leaf) {
        String[] levelValues = EngineUtil.split(csvLevelValues, ';');
        add(levelValues, leaf);
    }

    /**
     * Przeszukuje drzewo indeksu w poszukiwaniu sciezki (wzorca),
     * ktora najlepiej pasuje do podanych wartosci poziomow (levelValues).
     * <p>
     * Jesli znajdzie taka sciezke, zwraca obiekt liscia,
     * czyli wartosc skojarzona z wzorcem parametru.
     * W przeciwnym przypadku zwraca null.
     * <p>
     * Bardziej szczegolowy opis przeszukiwania indeksu znajduje sie w opisie klasy.
     *
     * @param levelValues wartosci poziomow, dla ktorych przeszukiwane jest drzewo indeksu
     * @return obiekt liscia skojarzonego ze znaleziona sciezka (wzorcem parametru)
     */
    public T find(String... levelValues) {
        LevelNode<T> node = root.findNode(levelValues, 0);
        return node != null ? node.getLeafValue() : null;
    }

    public List<T> findAll(String... levelValues) {
        LevelNode<T> node = root.findNode(levelValues, 0);
        return node != null ? node.getLeafList() : null;
    }

    /**
     * Zwraca string przedstawiajacy drzewo indeksu.
     * Przykladowy wynik:
     * <pre>
     * path :
     *    path : /A
     *       path : /A/B   (leaf=100)
     *       path : /A/?   (leaf=200)
     *    path : /?
     *       path : /?/E   (leaf=300)
     *       path : /?/?   (leaf=400)
     * </pre>
     *
     * @return graficzna reprezentacja drzewa indeksu
     */
    public String printTree() {
        StringBuilder sb = new StringBuilder(Formatter.INITIAL_STR_LEN_256);
        root.printNode(sb, 0);
        return sb.toString();
    }

    /**
     * Zwraca tablice matcherow dla kolejnych poziomow.
     * Dlugosc tablicy jest zawsze rowna {@link #getLevelCount()}.
     * Nie jest czescia publiczengo API.
     */
    Matcher[] getMatchers() {
        return matchers;
    }

    /**
     * Zwraca tablice typow dla kolejnych poziomow.
     * Dlugosc tablicy jest zawsze rowna {@link #getLevelCount()}.
     * Nie jest czescia publiczengo API.
     */
    AbstractType<?>[] getTypes() {
        return types;
    }

    /**
     * Zwraca matcher dla podanej glebokosci drzewa, numerowanej od 0.
     *
     * @param depth glebokosc numerowana od 0, czyli numer poziomu - 1
     * @return matcher dla zadanej glebokosci
     */
    public Matcher getMatcher(int depth) {
        return matchers[depth];
    }

    /**
     * Zwraca typ wartosci dla podanej glebokosci drzewa, numerowanej od 0.
     *
     * @param depth glebokosc numerowana od 0, czyli numer poziomu - 1
     * @return typ dla zadanej glebokosci
     */
    public AbstractType<?> getType(int depth) {
        return types[depth];
    }

    /**
     * Zwraca liczbe poziomow w drzewie (parametrze).
     *
     * @return liczba poziomow
     */
    public int getLevelCount() {
        return levelCount;
    }
}
