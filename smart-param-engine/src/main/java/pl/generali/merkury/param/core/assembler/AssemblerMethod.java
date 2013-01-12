package pl.generali.merkury.param.core.assembler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.generali.merkury.param.core.context.ParamContext;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.exception.ParamException.ErrorCode;
import pl.generali.merkury.param.core.type.AbstractHolder;

/**
 * Reprezentuje metode assemblera oznaczona przez adnotacje <tt>@Assembler</tt>.
 * Na przyklad dowolna klasa moze zawierac nastpujaca metode:
 * <pre>
 *   &#64;Assembler
 *   private RiskType createRiskType(StringHolder value) {
 *      return RiskType.valueOf(value.getString());
 *   }
 * </pre>
 * Dla takiej definicji metody zostanie stworzony assembler,
 * czyli obiekt {@link AssemblerMethod} z nastepujaca zawartoscia:
 * <ul>
 * <li>source = StringHolder.class - klasa zrodlowa
 * <li>target = RiskType.class - klasa docelowa
 * <li>method = java.lang.reflect.Method - metoda createRiskType
 * <li>owner = obiekt, ktory zawiera metode createRiskType
 * <li>passingContext = false - metoda nie przyjmuje kontekstu jako drugiego argumentu
 * </ul>
 *
 * Obiekty {@link AssemblerMethod} sa rejestrowane i przechowywane
 * w obiekcie {@link pl.generali.merkury.param.core.config.AssemblerProvider}.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class AssemblerMethod {

    /**
     * Logger.
     */
    private Logger logger = LoggerFactory.getLogger(AssemblerMethod.class);

    /**
     * Klasa zrodlowa. Assembler potrafi przyjmowac na wejsciu obiekty
     * tej klasy lub klas pochodnych.
     */
    private Class<?> source;

    /**
     * Klasa docelowa. Assembler potrafi zamienic obiekt klasy source
     * na obiekt klasy docelowej lub klasy nadrzednej.
     */
    private Class<?> target;

    /**
     * Metoda, ktora jest implementacja assemblera.
     */
    private Method method;

    /**
     * Obiekt, ktory zawiera metode assemblera i na ktorym mozna ja wykonywac.
     */
    private Object owner;

    /**
     * Flaga, czy metoda przyjmuje {@link ParamContext} jako drugi argument.
     */
    private boolean passingContext;

    /**
     * Jedyny sposob budowania obiektu. Obiekt jest niezmienny (immutable).
     * Metoda <tt>method</tt> musi miec nastepujaca postac:
     * <pre>
     *   private|protected|public {? extends Object} methodName( {? extends AbstractHolder} [, {? extends ParamContext}] )
     * </pre>
     * Na przyklad poprawne definicje wygladaja tak:
     * <pre>
     *   private RiskType createRiskType(StringHolder value)
     *   private RiskData createRiskData(IntegerHolder value, DefaultContext ctx)
     *   public Enum buildEnum(AbstractHolder value, ParamContext ctx)
     * </pre>
     *
     * @param owner  obiekt assembler owner
     * @param method metoda reprezentujaca assembler
     * @throws ParamException jesli metoda nie spelnia specyfikacji assemblera
     */
    public AssemblerMethod(Object owner, Method method) {
        this.owner = owner;
        this.method = method;
        this.target = method.getReturnType();

        // argumenty metody
        Class<?>[] argTypes = method.getParameterTypes();
        int args = argTypes.length;

        // weryfikacja ksztaltu metody
        if (args == 1 && validHolder(argTypes)) {
            source = argTypes[0];
        } else if (args == 2 && validHolder(argTypes) && validContext(argTypes)) {
            source = argTypes[0];
            passingContext = true;
        } else {
            throw new ParamException(ErrorCode.ILLEGAL_ASSEMBLER_DEFINITION, "method: " + method);
        }

        // udostepnienie metody, jesli nie jest publiczna
        setAccessible();
    }

    /**
     * Sprawdza, czy pierwszy argument jest typu {@link AbstractHolder}.
     *
     * @param argTypes argumenty metody
     * @return wynik sprawdzenia
     */
    private boolean validHolder(Class<?>[] argTypes) {
        return AbstractHolder.class.isAssignableFrom(argTypes[0]);
    }

    /**
     * Sprawdza, czy drugi argument jest typu {@link ParamContext}.
     *
     * @param argTypes argumenty metody
     * @return wynik sprawdzenia
     */
    private boolean validContext(Class<?>[] argTypes) {
        return ParamContext.class.isAssignableFrom(argTypes[1]);
    }

    /**
     * Ustawia na metodzie flage dostepnosci, co powoduje,
     * ze metoda moze byc wolana przez refleksje,
     * nawet jesli nie jest publiczna.
     */
    private void setAccessible() {
        AccessController.doPrivileged(new AccessibleSetter());
    }

    /**
     * Uruchamia assembler, czyli buduje obiekt wynikowy na podstawie
     * przekazanego obiektu wejsciowego i ewentualnie obiektu kontekstu.
     *
     * @param holder obiekt wejsciowy
     * @param ctx    kontekst
     *
     * @return obiekt wynikowy, zbudowany przez assembler
     * @throws ParamException jesli uruchomienie assemblera zakonczylo sie bledem
     */
    public Object assemble(AbstractHolder holder, ParamContext ctx) {

        try {

            if (passingContext) {
                return method.invoke(owner, holder, ctx);
            } else {
                return method.invoke(owner, holder);
            }

        } catch (IllegalAccessException e) {
            throw raiseAssembleException(e);

        } catch (IllegalArgumentException e) {
            throw raiseAssembleException(e);

        } catch (InvocationTargetException e) {
            throw raiseAssembleException(e);

        }

    }

    private ParamException raiseAssembleException(Throwable cause) {
        logger.error("failed to invoke assembler", cause);
        return new ParamException(ErrorCode.ASSEMBLER_INVOKE_ERROR, cause, "failed to invoke assembler: " + this);
    }

    /**
     * Getter dla method.
     *
     * @return metoda assemblera
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Getter dla source.
     *
     * @return klasa zrodlowa
     */
    public Class<?> getSource() {
        return source;
    }

    /**
     * Getter dla target.
     *
     * @return klasa docelowa
     */
    public Class<?> getTarget() {
        return target;
    }

    /**
     * Getter dla owner.
     *
     * @return obiekt assembler ownera
     */
    public Object getOwner() {
        return owner;
    }

    /**
     * Getter dla passingContext.
     *
     * @return czy metoda assemblera przyjmuje {@link ParamContext}
     */
    public boolean isPassingContext() {
        return passingContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssemblerMethod[");
        sb.append(source.getSimpleName());
        sb.append(" -> ");
        sb.append(target.getSimpleName());
        sb.append("] (").append(owner.getClass().getSimpleName()).append('#').append(method.getName()).append(')');
        return sb.toString();
    }

    /**
     * Klasa callbackowa, ktora ustawia flage dostepnosci na metodzie.
     */
    private final class AccessibleSetter implements PrivilegedAction<Object> {

        @Override
        public Object run() {
            method.setAccessible(true);
            return null;
        }
    }
}
