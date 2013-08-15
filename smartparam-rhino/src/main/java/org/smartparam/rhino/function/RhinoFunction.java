/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.rhino.function;

import org.smartparam.engine.model.function.FunctionImpl;

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
