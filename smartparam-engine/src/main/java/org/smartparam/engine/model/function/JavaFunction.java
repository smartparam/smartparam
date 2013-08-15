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
package org.smartparam.engine.model.function;

import java.lang.reflect.Method;

/**
 * Implementacja funkcji (z repozytorium funkcji modulu parametrycznego) oparta
 * na metodzie dowolnego obiektu Java.
 * <p>
 *
 * Definicja tej funkcji sklada sie z:
 * <ol>
 * <li><tt>className</tt> - pelnopakietowa nazwa klasy
 * <li><tt>methodName</tt> - nazwa metody o dostepie <b>publicznym</b>, metoda
 * moze byc zarowno instancyjna jak i statyczna
 * </ol>
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class JavaFunction extends Function {

    private Method method;

    public JavaFunction(String name, String type, Method method) {
        super(name, type);
        this.method = method;
    }

    /**
     * Get name of method to call.
     *
     * @return method name
     */
    public Method getMethod() {
        return method;
    }
}
