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
package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.model.function.Function;

/**
 * Przetworzony (skompilowany) poziom parametru.
 * Jest tworzony jako kopia obiektu poziomu wczytanego z bazy danych,
 * dzieki temu jest uwolniony od ewentualnych referencji do obiektow JPA.
 * <p>
 *
 * Przetworzony poziom zawiera m.in.:
 * <ol>
 * <li> obiekt typu poziomu (type),
 * <li> obiekt matchera,
 * <li> obiekt funkcji typu <i>levelCreator</i>
 * </ol>
 *
 * @see org.smartparam.engine.model.Level
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedLevel {

	/**
	 * Optional level name.
	 */
	private String name;

    /**
     * Typ wartosci dla tego poziomu (zgodny z systemem typow silnika).
     * Musi byc <tt>not null</tt> jesli uzywamy niestandardowego matchera dla tego poziomu.
     */
    private Type<?> type;

    /**
     * Flaga oznaczajaca, czy zawartosc tego poziomu moze byc traktowana jako tablica wartosci.
     */
    private boolean array;

    /**
     * Matcher uzywany dla tego poziomu (jesli nie jest uzywany standardowy).
     */
    private Matcher matcher;

    /**
     * Funkcja ustalajaca dynamicznie wartosc poziomu.
     */
    private Function levelCreator;

    /**
	 * Creates immutable instance.
     *
	 * @param name         level's name
     * @param type         level's type code
     * @param array        whether this level contains array
     * @param matcher      level's matcher code
     * @param levelCreator function resolving actual level value
     */
    public PreparedLevel(String name, Type<?> type, boolean array, Matcher matcher, Function levelCreator) {
		this.name = name;
        this.type = type;
        this.array = array;
        this.matcher = matcher;
        this.levelCreator = levelCreator;
    }

	public String getName() {
		return name;
	}

	/**
     * Getter dla flagi array.
     *
     * @return flaga array
     */
    public boolean isArray() {
        return array;
    }

    /**
     * Getter dla matchera.
     *
     * @return matcher
     */
    public Matcher getMatcher() {
        return matcher;
    }

    /**
     * Getter dla typu poziomu.
     *
     * @return typ poziomu
     */
    public Type<?> getType() {
        return type;
    }

    /**
     * Getter dla funkcji levelCreatora.
     *
     * @return funkcja wyznaczajaca wartosc poziomu
     */
    public Function getLevelCreator() {
        return levelCreator;
    }
}
