package org.smartparam.provider.jpa.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.smartparam.engine.model.functions.JavaFunction;
import org.smartparam.engine.util.Formatter;

/**
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Entity
@DiscriminatorValue("java")
public class JpaJavaFunction extends JpaFunctionImpl implements JavaFunction {

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
    public JpaJavaFunction() {
    }

    /**
     * Konstruktor inicjalizujacy pola klasy.
     *
     * @param className  nazwa klasy
     * @param methodName nazwa metody
     */
    public JpaJavaFunction(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Konstruktor inicjalizujacy pola klasy.
     *
     * @param clazz      klasa
     * @param methodName nazwa metody
     */
    public JpaJavaFunction(Class<?> clazz, String methodName) {
        this(clazz.getName(), methodName);
    }

    /**
     * Kod jednozancznie identyfikujacy rodzaj funkcji.
     * Uzywany przez {@link org.smartparam.engine.core.config.InvokerProvider}.
     *
     * @return kod <tt>java</tt>
     */
    @Override
    @Transient
    public String getTypeCode() {
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
