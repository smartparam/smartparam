package pl.generali.merkury.param.model.functions;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import pl.generali.merkury.param.model.FunctionImpl;
import pl.generali.merkury.param.util.Formatter;

/**
 * Implementacja funkcji (z repozytorium funkcji modulu parametrycznego) oparta na metodzie dowolnego obiektu Java.
 * <p>
 *
 * Definicja tej funkcji sklada sie z:
 * <ol>
 * <li><tt>className</tt> - pelnopakietowa nazwa klasy
 * <li><tt>methodName</tt> - nazwa metody o dostepie <b>publicznym</b>, metoda moze byc zarowno instancyjna jak i statyczna
 * </ol>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Entity
@DiscriminatorValue("java")
public class JavaFunction extends FunctionImpl {

    /**
     * SUID.
     */
    static final long serialVersionUID = 1L;

    /**
     * Pelnopakietowa nazwa klasy.
     */
    private String className;

    /**
     * Nazwa metody. Metoda moze byc zarowno instancyjna jak i statyczna.
     * Z dostepem pakietowym, private, protected lub public.
     */
    private String methodName;

    /**
     * Konstruktor domyslny.
     */
    public JavaFunction() {
    }

    /**
     * Konstruktor inicjalizujacy pola klasy.
     *
     * @param className  nazwa klasy
     * @param methodName nazwa metody
     */
    public JavaFunction(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Konstruktor inicjalizujacy pola klasy.
     *
     * @param clazz      klasa
     * @param methodName nazwa metody
     */
    public JavaFunction(Class<?> clazz, String methodName) {
        this(clazz.getName(), methodName);
    }

    /**
     * Kod jednozancznie identyfikujacy rodzaj funkcji.
     * Uzywany przez {@link pl.generali.merkury.param.core.config.InvokerProvider}.
     *
     * @return kod <tt>java</tt>
     */
    @Override
    @Transient
    public String getImplCode() {
        return "java";
    }

    /**
     * @return pelnopakietowa nazwa klasy
     */
    @Column
    public String getClassName() {
        return className;
    }

    /**
     * Setter dla className.
     *
     * @param className pelnopakietowa nazwa klasy
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return nazwa metody
     */
    @Column(length = MEDIUM_COLUMN_LENGTH)
    public String getMethodName() {
        return methodName;
    }

    /**
     * Setter dla methodName.
     *
     * @param methodName nazwa metody
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(Formatter.INITIAL_STR_LEN_128);
        sb.append("JavaFunction#").append(getId());
        sb.append("[class=").append(className);
        sb.append(", method=").append(methodName);
        sb.append(']');
        return sb.toString();
    }
}
