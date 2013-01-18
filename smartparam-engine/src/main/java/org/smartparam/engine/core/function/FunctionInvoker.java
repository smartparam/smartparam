package org.smartparam.engine.core.function;

import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.model.FunctionImpl;

/**
 * Klasa implementujaca ten interfejs jest odpowiedzialna za wykonywanie funkcji (z repozytorium)
 * zaimplementowanych jako funkcje typu <tt>T</tt>.
 * <p>
 * FunctionInvoker wykonuje funkcje (FunctionImpl) przekazujac odpowiednie argumenty
 * a nastepnie zwraca obiekt zwrocony przez te funkcje.
 * <p>
 * Przykladowe typy implementacji i ich FunctionInvokery:
 * <ul>
 * <li>JavaFunction - JavaFunctionInvoker
 * <li>RhinoFunction - RhinoFunctionInvoker
 * <li>SpringFunction - SpringFunctionInvoker
 * </ul>
 *
 * @param <T> typ implementacji funkcji, ktory potrafi wykonac ten FunctionInvoker
 *
 * @see org.smartparam.engine.core.function.JavaFunctionInvoker
 * @see org.smartparam.engine.core.function.RhinoFunctionInvoker
 * @see org.smartparam.engine.core.function.SpringFunctionInvoker
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface FunctionInvoker<T extends FunctionImpl> {

    /**
     * Wykonuje funkcje <tt>function</tt> przekazujac do niej 1 argument: <tt>ctx</tt>.
     * Zwraca obiekt zwrocony przez te funkcje.
     *
     * @param function funkcja typu T
     * @param ctx      kontekst uzycia parametru
     *
     * @return obiekt zwrocony przez funkcje
     */
    Object invoke(T function, ParamContext ctx);

    /**
     * Wykonuje funkcje <tt>function</tt> przekazujac do niej argumenty <tt>args</tt>.
     * Zwraca obiekt zwrocony przez te funkcje.
     *
     * @param function funkcja typu T
     * @param args     dowolne obiekty bedace argumentami funkcji
     *
     * @return obiekt zwrocony przez funkcje
     */
    Object invoke(T function, Object... args);
}
