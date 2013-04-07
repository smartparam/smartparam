package org.smartparam.rhino.function;

import org.smartparam.engine.model.FunctionImpl;

/**
 *
 * Implementacja funkcji (z repozytorium funkcji modulu parametrycznego) oparta
 * na Rhino.
 * <p>
 *
 * Sklada sie z:
 * <ol>
 * <li><tt>args</tt> - argumentow formalnych przekazywanych do funkcji (nazwy
 * argumentow oddzielone przecinkiem)
 * <li><tt>body</tt> - ciala funkcji, czyli kodu rhino
 * </ol>
 *
 * Kod rhino (body) moze uzywac argumentow przekazanych przez wolajacego (args)
 * a takze obiektow globalnych, jesli takie zostaly zdefiniowane w obiekcie
 * {@link org.smartparam.engine.core.function.RhinoFunctionInvoker}.
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
 * @since 0.1.0
 */
public interface RhinoFunction extends FunctionImpl {

    // FIXME #ad temporary - how to cache rhinoFunctions
    public int getId();

    public String getArgs();

    public String getBody();
}
