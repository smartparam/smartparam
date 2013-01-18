package org.smartparam.engine.model.functions;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.smartparam.engine.model.FunctionImpl;
import org.smartparam.engine.util.Formatter;

/**
 * Implementacja funkcji (z repozytorium funkcji modulu parametrycznego) oparta na metodzie dowolnego beana springowego.
 * <p>
 *
 * Definicja tej funkcji sklada sie z:
 * <ol>
 * <li><tt>beanName</tt> - nazwa beana springowego
 * <li><tt>methodName</tt> - nazwa metody o dostepie <b>publicznym</b>, metoda moze byc zarowno instancyjna jak i statyczna
 * </ol>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
@Entity
@DiscriminatorValue("spring")
public class SpringFunction extends FunctionImpl {

    /**
     * SUID.
     */
    static final long serialVersionUID = 1L;

    /**
     * Nazwa beana springowego.
     */
    private String beanName;

    /**
     * Nazwa metody. Metoda moze byc zarowno instancyjna jak i statyczna.
     * Z dostepem pakietowym, private, protected lub public.
     */
    private String methodName;

    /**
     * Konstruktor domyslny.
     */
    public SpringFunction() {
    }

    /**
     * Konstruktor inicjalizujacy pola klasy.
     *
     * @param beanName   nazwa beana
     * @param methodName nazwa metody
     */
    public SpringFunction(String beanName, String methodName) {
        this.beanName = beanName;
        this.methodName = methodName;
    }

    /**
     * Kod jednozancznie identyfikujacy rodzaj funkcji.
     * Uzywany przez {@link org.smartparam.engine.core.config.InvokerProvider}.
     *
     * @return kod <tt>spring</tt>
     */
    @Override
    @Transient
    public String getImplCode() {
        return "spring";
    }

    /**
     * Getter dla beanName.
     *
     * @return nazwa beana springowego
     */
    @Column
    public String getBeanName() {
        return beanName;
    }

    /**
     * Setter dla beanName.
     *
     * @param beanName nazwa beana
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * Getter dla methodName.
     *
     * @return nazwa metody
     */
    @Column
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
        sb.append("SpringFunction#").append(getId());
        sb.append("[bean=").append(beanName);
        sb.append(", method=").append(methodName);
        sb.append(']');
        return sb.toString();
    }
}
