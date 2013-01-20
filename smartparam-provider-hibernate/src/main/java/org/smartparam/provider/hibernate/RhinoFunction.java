package org.smartparam.provider.hibernate;

/**
 * FIXME #ad implement in some module..
 *
 * Implementacja funkcji (z repozytorium funkcji modulu parametrycznego) oparta na Rhino.
 * <p>
 *
 * Sklada sie z:
 * <ol>
 * <li><tt>args</tt> - argumentow formalnych przekazywanych do funkcji (nazwy argumentow oddzielone przecinkiem)
 * <li><tt>body</tt> - ciala funkcji, czyli kodu rhino
 * </ol>
 *
 * Kod rhino (body) moze uzywac argumentow przekazanych przez wolajacego (args) a takze obiektow globalnych,
 * jesli takie zostaly zdefiniowane w obiekcie {@link org.smartparam.engine.core.function.RhinoFunctionInvoker}.
 * <p>
 *
 * Przykladowo, funkcja o nastepujacej zawartosci:
 * <pre>
 * id    : 17
 * args  : a,b
 * body  : if (a==b) return a; else return b;
 * </pre>
 *
 * zostanie wykonana jako funkcja rhino o nastepujacej definicji:
 * <pre>
 * function F_17(a,b) {
 *   if (a==b) return a; else return b;
 * }
 * </pre>
 *
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
//@Entity
//@DiscriminatorValue("rhino")
public class RhinoFunction { //extends HibernateFunctionImpl {
//    /**
//     * SUID.
//     */
//    static final long serialVersionUID = 1L;
//
//    /**
//     * Argumenty formalne oddzielone przecinkiem.
//     */
//    private String args;
//
//    /**
//     * Cialo funkcji, bez nawiasu otwierajacego i zamykajacego.
//     */
//    private String body;
//
//    /**
//     * Konstruktor domyslny.
//     */
//    public RhinoFunction() {
//    }
//
//    /**
//     * Konstruktor inicjalizujacy argumenty i cialo funkcji.
//     *
//     * @param args nazwy argumentow oddzielone przecinkami
//     * @param body cialo funkcji
//     */
//    public RhinoFunction(String args, String body) {
//        this.args = args;
//        this.body = body;
//    }
//
//    /**
//     * Kod jednozancznie identyfikujacy rodzaj funkcji.
//     * Uzywany przez {@link org.smartparam.engine.core.config.InvokerProvider}.
//     *
//     * @return kod <tt>rhino</tt>
//     */
//    @Override
//    @Transient
//    public String getImplCode() {
//        return "rhino";
//    }
//
//    /**
//     * Getter dla args.
//     *
//     * @return args
//     */
//    @Column(length = MEDIUM_COLUMN_LENGTH)
//    public String getArgs() {
//        return args;
//    }
//
//    /**
//     * Setter dla args.
//     *
//     * @param args nazwy argumentow oddzielone przecinkami
//     */
//    public void setArgs(String args) {
//        this.args = args;
//    }
//
//    /**
//     * Getter dla body.
//     *
//     * @return zwraca cialo funkcji
//     */
//    @Lob
//    public String getBody() {
//        return body;
//    }
//
//    /**
//     * Setter dla body.
//     *
//     * @param body cialo funkcji, bez nawiasu otwierajacego i zamykajacego
//     */
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    /**
//     * Ekstrahuje <tt>args</tt> i <tt>body</tt> z pelnego kodu funkcji rhino.
//     * Zaklada, ze <tt>fullcode</tt> pasuje do schematu:
//     * <pre>
//     * function $name($args) {$body}
//     * </pre>
//     * Podczas ekstrahowania, biale znaki nie maja znaczenia.
//     *
//     * @param fullcode pelny kod funkcji rhino
//     * @throws IllegalArgumentException jesli <tt>fullcode</tt> ma niepoprawny schemat
//     */
//    public void setFullCode(String fullcode) {
//
//        if (fullcode != null) {
//
//            int openingBracket = fullcode.indexOf('{');
//            int closingBracket = fullcode.lastIndexOf('}');
//
//            if (openingBracket > 0 && closingBracket > openingBracket) {
//
//                parseHeader(fullcode.substring(0, openingBracket));                 // fragment do pierwszego nawiasu definiuje header
//                parseBody(fullcode.substring(openingBracket + 1, closingBracket));  // fragment po pierwszym nawiasie to body
//
//                return;
//            }
//        }
//
//        throw new IllegalArgumentException("Rhino function bad syntax: " + fullcode);
//    }
//
//    /**
//     * Ustawia pole <tt>args</tt> na podstawie headera, o ile header jest zgodny ze schematem.
//     *
//     * @param header naglowek pelnego kodu funkcji
//     * @see #setFullCode(java.lang.String)
//     */
//    private void parseHeader(String header) {
//        Matcher matcher = Pattern.compile("^function\\s+(.+)\\((.*)\\)$").matcher(header.trim());
//
//        if (matcher.find()) {
//            setArgs(matcher.group(2).trim());
//
//        } else {
//            throw new IllegalArgumentException("Bad syntax - failed to parse function header: " + header);
//        }
//    }
//
//    /**
//     * Ustawia body.
//     *
//     * @param body cialo funkcji
//     */
//    private void parseBody(String body) {
//        setBody(body);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(Formatter.INITIAL_STR_LEN_128);
//        sb.append("RhinoFunction#").append(getId());
//        sb.append("[body=").append(body);
//        sb.append(", args=").append(args);
//        sb.append(']');
//        return sb.toString();
//    }
}
