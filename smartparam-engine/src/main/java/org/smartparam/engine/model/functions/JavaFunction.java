package org.smartparam.engine.model.functions;

import org.smartparam.engine.model.FunctionImpl;

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
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface JavaFunction extends FunctionImpl {

    /**
     * Get class name containing method to call. Class has to be available in current classpath.
     *
     * @return class name
     */
    String getClassName();

    /**
     * Get name of method to call.
     *
     * @return method name
     */
    String getMethodName();
}
