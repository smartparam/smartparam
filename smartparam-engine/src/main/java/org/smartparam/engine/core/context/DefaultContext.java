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
package org.smartparam.engine.core.context;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.util.reflection.ReflectionSetterFinder;
import org.smartparam.engine.util.reflection.ReflectionsHelper;

/**
 * Domyslna implementacja kontekstu (ParamContext).
 * Klasa zawiera jeden, generyczny konstruktor (vargs),
 * ktory wypelnia pola klasy zaleznie od przekazanych argumentow.
 * <p>
 * Kontekst jest zbiorem danych, ktore uzytkownik moze przekazac podczas
 * wywolywania metod silnika parametrycznego. Z danych kontekstu moga
 * nastepnie korzystac pozostale fragmenty silnika lub funkcje,
 * na przyklad: <i>level creatory</i>, <i>version selectory</i> itp.
 * <p>
 * Klasa zawiera 4 podstawowe struktury/koncepcje:
 * <ol>
 *
 * <li> <b>levelValues</b> - tablica stringow, ktore zostana uzyte jako wartosci
 * kolejnych poziomow w trakcie szukania pasujacego wzorca w macierzy parametru.
 * Jesli <tt>levelValues</tt> bedzie rowne <tt>null</tt>, silnik parametryczny
 * skorzysta z funkcji (<i>level creator</i>) do wyznaczenia wartosci dla kolejnych poziomow.
 * Wartosci poziomow mozna przekazac w konstruktorze jako obiekt typu <tt>String[]</tt>
 * lub skorzystac z settera {@link #withLevelValues(java.lang.Object[])}
 * lub wykorzystac dziedziczaca klase kontekstu {@link LevelValues}.

 * <li> <b>userContext</b> - mapa, do ktorej uzytkownik moze wstawic wszelkie
 * obiekty pod dowolnymi kluczami. Z tych obiektow moze nastepnie skorzystac
 * w funkcjach silnika (<i>level creatory</i>, <i>version selectory</i> itp)
 * przy pomocy metody {@link #get(java.lang.String)} lub {@link #get(java.lang.Class)}.
 * Elementy <tt>userContextu</tt> mozna przekazac w konstruktorze (zob. opis konstruktora)
 * lub poprzez settery {@link #set(java.lang.String, java.lang.Object)} i podobne.
 *
 * <li> <b>kontekst specjalizowany</b> - uzytkownik moze stworzyc wlasna klase
 * kontekstu (np. MyBankContext), ktora dziedziczy po tej klasie i zawiera
 * rozne pola z modelu dziedzinowego uzytkownika. Wystarczy zeby uzytkownik
 * zapewnil settery dla tych pol, a generyczny konstruktor kontekstu
 * bedzie potrafil je rozpoznac i wypelnic. Innymi slowy, kazdy obiekt przekazany
 * do konstruktora trafi do odpowiedniego settera, jesli tylko taki setter
 * bedzie dostepny w klasie kontekstu.
 * </ol>
 *
 * Sercem klasy {@link DefaultContext} jest algorytm inicjalizacji
 * zastosowany w konstruktorze.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class DefaultContext implements ParamContext {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    /**
     * Setter cache. Keeps reference to setter methods extracted via reflection
     * mechanisms. Speeds up initialization of context up to 3-4 times.
     */
    private static ReflectionSetterFinder setterCache = new ReflectionSetterFinder();

    private Map<String, Object> userContext;

    private String[] levelValues;

    /**
     * Puts provided values into context using algorithm:
     * <ol>
     * <li>if <tt>args[i]</tt> is <tt>String[]</tt> level values are set using {@link #setLevelValues(java.lang.String[]) } </li>
     * <li>if <tt>args[i]</tt> is <tt>Object[]</tt> level values are set using {@link #setLevelValues(java.lang.Object[]) } </li>
     * <li>if <tt>args[i]</tt> is <tt>String</tt> <tt>args[i+1]</tt> value is taken and put into context under <tt>args[i]</tt> key using {@link #set(java.lang.String, java.lang.Object) }</li>
     * <li>else, setter lookup is performed using {@link ReflectionSetterFinder} to find any setter of current context object that accepts <tt>args[i]</tt></li>
     * <li>eventually, <tt>args[i]</tt> is put into context under its class name using {@link #set(java.lang.Object) }</li>
     * </ol>.
     *
     * @param args
     */
    public DefaultContext(Object... args) {
        initialize(args);
    }

    /**
     * Create empty context, use setter methods to initialize it.
     *
     * @see #setLevelValues(java.lang.String[])
     * @see #setLevelValues(java.lang.Object[])
     * @see #set(java.lang.String, java.lang.Object)
     * @see #set(java.lang.Object)
     * @see #DefaultContext(java.lang.Object[])
     */
    public DefaultContext() {
    }

    /**
     * Implementation of value initializing algorithm.
     *
     * @param args
     *
     * @see #DefaultContext(java.lang.Object[])
     */
    protected final void initialize(Object... args) {
        for (int argumentIndex = 0; argumentIndex < args.length; ++argumentIndex) {
            Object arg = getArgumentAt(args, argumentIndex);

            if (arg instanceof String[]) {
                setLevelValues((String[]) arg);
            } else if (arg instanceof Object[]) {
                setLevelValues((Object[]) arg);
            } else if (arg instanceof String) {
                // skip one, cos it is now being used
                argumentIndex++;
                set((String) arg, getArgumentAt(args, argumentIndex));
            } else if (arg != null) {
                Method setter = setterCache.findSetter(getClass(), arg);
                if (setter != null) {
                    setArgumentWithSetter(setter, arg);
                } else {
                    set(arg);
                }
            }
        }
    }

    /**
     * Put <tt>value</tt> under <tt>lowercase(key)</tt>. Will throw a
     * {@link SmartParamUsageException} if there was value registered already.
     *
     * @param key
     * @param value
     * @return
     *
     * @see #set(java.lang.String, java.lang.Object, boolean)
     */
    public final DefaultContext set(String key, Object value) {
        return set(key, value, false);
    }

    /**
     * Put <tt>value</tt> under key <tt>lowercase(key)</tt>. allowOverwrite flag
     * determines what happens in case of key collision. If overwriting is allowed,
     * new value replaces old one, otherwise {@link SmartParamUsageException} is
     * thrown. Lowercase function uses default JVM locale, so be careful with
     * some fancy keys.
     *
     * @param key
     * @param value
     * @param allowOverwrite
     * @return
     *
     * @see Locale#getDefault()
     */
    public final DefaultContext set(String key, Object value, boolean allowOverwrite) {
        if (userContext == null) {
            userContext = new TreeMap<String, Object>();
        }

        String k = lowercase(key);
        if (userContext.containsKey(k) && !allowOverwrite) {
            throw new SmartParamUsageException(SmartParamErrorCode.ERROR_FILLING_CONTEXT,
                    "Trying to set duplicate key on userContext: key=" + key);
        }

        userContext.put(k, value);
        return this;
    }

    private String lowercase(final String str) {
        return str.toLowerCase(DEFAULT_LOCALE);
    }

    /**
     * Put value under <tt>lowercase(value.class.getSimpleName())</tt> in user
     * context map. Internally calls {@link #set(java.lang.String, java.lang.Object) }.
     *
     * @param value
     * @return
     */
    public final DefaultContext set(Object value) {
        return set(value.getClass().getSimpleName(), value);
    }

    /**
     * Return object stored under key. Key is always lowercased using default
     * locale.
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return userContext != null ? userContext.get(lowercase(key)) : null;
    }

    /**
     * Looks for object of class <tt>clazz</tt> (or object which class is
     * assignable from <tt>clazz</tt>. Algorithm:
     * <ol>
     * <li>look for object stored under <tt>clazz.getSimpleName()</tt>, return if not null and class match</li>
     * <li>iterate through all context values to look for first object that matches provided clazz</li>
     * </ol>
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {

        if (userContext != null) {

            Object obj = get(clazz.getSimpleName());
            if (obj != null && obj.getClass() == clazz) {
                return (T) obj;
            }

            for (Object contextValue : userContext.values()) {
                if (contextValue == null) {
                    continue;
                }

                if (clazz.isAssignableFrom(contextValue.getClass())) {
                    return (T) contextValue;
                }
            }
        }
        return null;
    }

    private Object getArgumentAt(Object[] args, int index) {
        if (index < args.length) {
            return args[index];
        }
        throw new SmartParamUsageException(
                SmartParamErrorCode.ERROR_FILLING_CONTEXT,
                String.format("Expected element at position %d in argument array, but passed only %d arguments to DefaultContext constructor. "
                + "Maybe you wanted to put value under key and forgot to pass in a value after last string argument?", index, args.length));
    }

    private void setArgumentWithSetter(Method setter, Object arg) {
        try {
            ReflectionsHelper.runSetter(setter, this, arg);
        } catch (SmartParamException exception) {
            throw new SmartParamUsageException(SmartParamErrorCode.ERROR_FILLING_CONTEXT, exception,
                    String.format("Unable to set argument %s on context using setter %s", arg, setter));
        }
    }

    @Override
    public String[] getLevelValues() {
        return levelValues;
    }

    @Override
    public final void setLevelValues(String... levelValues) {
        this.levelValues = levelValues;
    }

    /**
     * Set level values directly, without using user context. Objects are
     * transformed to level values by calling toString on each of them. Method
     * is null safe, puts null value into level values.
     *
     * @param levelValues
     */
    protected final void setLevelValues(Object... levelValues) {
        this.levelValues = new String[levelValues.length];
        for (int i = 0; i < levelValues.length; ++i) {
            Object value = levelValues[i];
            this.levelValues[i] = value != null ? value.toString() : null;
        }
    }

    public DefaultContext withLevelValues(String... levelValues) {
        setLevelValues(levelValues);
        return this;
    }

    public DefaultContext withLevelValues(Object... levelValues) {
        setLevelValues(levelValues);
        return this;
    }

    /**
     * Return map representing parameter evaluation context.
     *
     * @return
     */
    protected Map<String, Object> getUserContext() {
        return userContext;
    }

    @Override
    public String toString() {
        return "DefaultContext[levelValues=" + Arrays.toString(levelValues) + ", userContext=" + userContext + ']';
    }
}
