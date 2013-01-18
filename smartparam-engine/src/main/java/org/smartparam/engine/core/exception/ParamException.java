package org.smartparam.engine.core.exception;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class ParamException extends RuntimeException {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Kody bledow.
     */
    public enum ErrorCode {

        /**
         * proba pobrania niewlasciwego typu z holdera, np. NumberHolder.getDate().
         */
        GETTING_WRONG_TYPE,
        /**
         * niepoprawna definicja assemblera.
         * metoda assemblera musi przyjmowac dokladnie 1 argument: klasa zrodlowa.
         */
        ILLEGAL_ASSEMBLER_DEFINITION,
        /**
         * Nie znaleziono pasujacego assemblera.
         */
        ASSEMBLER_NOT_FOUND,
        /**
         * Blad podczas wykonywania metody assemblera.
         */
        ASSEMBLER_INVOKE_ERROR,
        /**
         * proba zarejestrowania typu pod kodem, ktory jest juz zarejestrowany.
         */
        NON_UNIQUE_TYPE_CODE,
        /**
         * nieudana konwersja obiektu java na obiekt AbstractHolder.
         */
        TYPE_CONVERSION_FAILURE,
        /**
         * Nieudana probra zdekodowania wartosci tekstowej w obiekt holdera:
         * {@link org.smartparam.engine.core.type.AbstractType#decode(java.lang.String)}.
         */
        TYPE_DECODING_FAILURE,
        /**
         * Parametr nieznany, np. brak parametru o podanej nazwie.
         */
        UNKNOWN_PARAMETER,
        /**
         * Funkcja nieznana, np. w repozytorium funkcji brak funkcji o podanej nazwie.
         */
        UNKNOWN_FUNCTION,
        /**
         * Nie znaleziono wartosci parametru dla podanego kontekstu,
         * a parametr nie zezwala na zwracanie wartosci <tt>null</tt>.
         */
        PARAM_VALUE_NOT_FOUND,
        /**
         * niepoprawna tablica z wartosciami poziomow przekazana przez uzytkownika.
         * na przyklad - parametr ma 3 poziomy, a uzytkownik przekazal 2-elementowa tablice wartosci.
         */
        ILLEGAL_LEVEL_VALUES,
        /**
         * blad podczas wywolywania funkcji z repozytorium funkcji (Function), np:
         * - nie mozna znalezc odpowiedniej metody lub klasy (javafunction)
         * - funkcja rzucila wyjatek
         */
        FUNCTION_INVOKE_ERROR,
        /**
         * Blad podczas wypelniania kontekstu. Oczekiwano kolejnego argumentu.
         *
         * @see org.smartparam.engine.core.context.DefaultContext#initialize(java.lang.Object[]).
         */
        ERROR_FILLING_CONTEXT,
        /**
         * Niepoprawne uzycie metody API. Najprawdopodobniej przekazano do metody
         * argumenty, ktore sa ze soba sprzeczne.
         */
        ILLEGAL_API_USAGE,
        /**
         * Level nie ma podpietej funkcji typu levelCreator,
         * a funkcja ta jest wymagana przez kontekst uzycia - uzycie bez podawania przygotowanych wartosci poziomow.
         */
        UNDEFINED_LEVEL_CREATOR,
        /**
         * Uzycie funkcji (FunctionImpl), dla ktorej nie jest skonfigurowany FunctionInvoker.
         */
        UNDEFINED_FUNCTION_INVOKER,
        /**
         * Uzycie typu (AbstractType), ktory nie jest zarejstrowany w ramach TypeProvidera.
         */
        UNKNOWN_PARAM_TYPE,
        /**
         * Uzycie kodu matchera, ktory nie jest zarejestrowany w ramach MatcherProvidera.
         */
        UNKNOWN_MATCHER,
        /**
         * Wartosc parametru <tt>null</tt> nie jest dozwolona dla danego parametru.
         */
        NULL_NOT_ALLOWED,
        /**
         * Proba pobrania wartosci spod nieistniejacej pozycji.
         * Korzysta z tego wiele metod. W wiekszosci metod numer pozycji zaczyna sie od 1.
         */
        INDEX_OUT_OF_BOUNDS

    }
    /**
     * Kod bledu.
     */
    private ErrorCode errorCode;

    /**
     * Konstruktor.
     *
     * @param t przyczyna wyjatku
     */
    public ParamException(Throwable t) {
        super(t.getMessage(), t);
    }

    /**
     * Konstruktor.
     *
     * @param message komunikat o przyczynie wyjatku
     */
    public ParamException(String message) {
        super(message);
    }

    /**
     * Konstruktor.
     *
     * @param errorCode kod bledu
     * @param message   komunikat o przyczynie wyjatku
     */
    public ParamException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Konstruktor.
     *
     * @param errorCode kod bledu
     * @param t         przyczyna wyjatku
     * @param message   komunikat o przyczynie wyjatku
     */
    public ParamException(ErrorCode errorCode, Throwable t, String message) {
        super(message, t);
        this.errorCode = errorCode;
    }

    /**
     * Zwraca kod bledu.
     *
     * @return kod bledu
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getMessage());
        if (errorCode != null) {
            sb.append(" [errorcode=").append(errorCode).append(']');
        }
        return sb.toString();
    }
}
