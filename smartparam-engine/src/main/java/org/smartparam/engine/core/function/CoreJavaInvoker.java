package org.smartparam.engine.core.function;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * Klasa bazowa dla FunctionInvokerow, ktore wykonuja funkcje bazujace na implementacji poprzez metode javy.
 * Klasa zapewnia:
 * <ul>
 * <li>odnajdowanie metod pasujacych do wywolania (sprawdzanie kompatybilnosci parametrow),
 * <li>cache dla refleksji przyspieszajacy znajdowanie metody okolo 5 razy
 * </ul>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class CoreJavaInvoker {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Cache metod uzywany przez {@link #findMethod(java.lang.Class, java.lang.String, java.lang.Object[])}.
     * <ul>
     * <li>Klucz: typy parametrow zlozone w string przy pomocy metody
     * {@link #createMethodKey(java.lang.Class, java.lang.String, java.lang.Object[])},
     * <li>Wartosc: metoda, ktorej parametry formalne najlepiej pasuja do przekazanych parametrow aktualnych.
     * </ul>
     */
    private final Map<String, Method> methodMap = new ConcurrentHashMap<String, Method>();

    /**
     * Wykonuje metode <tt>m</tt> na obiekcie <tt>instance</tt>.
     * Przekazuje argumenty <tt>args</tt>, zwraca obiekt zwrocony przez metode.
     *
     * @param instance obiekt, na ktorym zostanie zawolana metoda, moze byc <tt>null</tt>, jesli metoda jest statyczna
     * @param m        metoda
     * @param args     argumenty przykazywane do metody
     *
     * @return obiekt zwrocony przez metode <tt>m</tt>
     *
     * @throws ParamException jesli wywolanie metody zostanie przerwane przez wyjatek,
     * wyjatek bedzie mial kod: {@link ErrorCode#FUNCTION_INVOKE_ERROR}
     */
    Object invoke(Object instance, Method m, Object... args) {
        try {
            return m.invoke(instance, args);
        } catch (Exception e) {
            logger.error("", e);
            throw new SmartParamException(SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e, "Error invoking method: " + m);
        }
    }

    /**
     * Znajduje metode o nazwie <tt>methodName</tt> w klasie <tt>clazz</tt>,
     * przy czym argumenty znalezionej metody beda pasowac do argumentow <tt>args</tt>.
     * <p>
     * Metoda korzysta z cache'a, wiec kazde kolejne wyszukanie dla tych samych <b>typow</b>
     * argumentow zwraca wartosc z cache'a.
     * <p>
     * Metoda jest srednio 4-6 razy szybsza od metody niekorzystajacej z cache'a.
     *
     * @see #loadMethod(java.lang.Class, java.lang.String, java.lang.Object[])
     *
     * @param clazz      klasa, w ktorej bedzie szukana metoda
     * @param methodName nazwa szukanej metody
     * @param args       argumenty, ktore musza pasowac do znalezionej metody
     *
     * @return obiekt metody
     *
     * @throws ParamDefinitionException jesli w klasie <tt>clazz</tt> nie ma metody o podanej nazwie
     * lub pasujacej do przekazanych argumentow, kod wyjatku to {@link ErrorCode#FUNCTION_INVOKE_ERROR}
     */
    protected Method findMethod(Class<?> clazz, String methodName, Object... args) {

        String key = createMethodKey(clazz, methodName, args);
        Method m = methodMap.get(key);

        if (m == null) {
            logger.debug("loading method for key: {}", key);
            m = loadMethod(clazz, methodName, args);
            methodMap.put(key, m);
        }

        return m;
    }

    /**
     * Znajduje (poprzez mechanizmy refleksji) metode o nazwie <tt>methodName</tt>
     * w klasie <tt>clazz</tt>, przy czym argumenty znalezionej metody beda pasowac
     * do argumentow <tt>args</tt>.
     * <p>
     * Dopasowanie argumentow oznacza, ze kazdy argument <tt>args[i]</tt>
     * moze byc zrzutowany na <tt>i</tt>-ty parametr formalny metody.
     * Dodatkowo dopasowywane sa typy proste i ich wrappery.
     *
     * @param clazz      klasa, w ktorej bedzie szukana metoda
     * @param methodName nazwa szukanej metody
     * @param args       argumenty, ktore musza pasowac do znalezionej metody
     *
     * @return obiekt metody
     *
     * @throws ParamDefinitionException jesli w klasie <tt>clazz</tt> nie ma metody o podanej nazwie
     * lub pasujacej do przekazanych argumentow, kod wyjatku to {@link ErrorCode#FUNCTION_INVOKE_ERROR}
     */
    protected Method loadMethod(Class<?> clazz, String methodName, Object... args) {

        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            argTypes[i] = args[i] != null ? args[i].getClass() : null;
        }

        Method m = MethodUtils.getMatchingAccessibleMethod(clazz, methodName, argTypes);

        if (m == null) {
            throw new SmartParamDefinitionException(
                    SmartParamErrorCode.FUNCTION_INVOKE_ERROR,
                    "Cannot find method: " + clazz.getName() + "." + methodName + " for args: " + Arrays.toString(argTypes));
        }

        return m;
    }

    /**
     * Tworzy string reprezentujacy sygnature metody. String ten jest wykorzystywany
     * jako klucz dla cache'a {@link #methodMap}.
     *
     * @param clazz      klasa
     * @param methodName nazwa metody
     * @param args       argumenty
     *
     * @return string reprezentujacy jednoznacznie sygnature pasujacej metody
     */
    private String createMethodKey(Class<?> clazz, String methodName, Object... args) {
        StringBuilder sb = new StringBuilder(INITIAL_KEY_CAPACITY);
        sb.append(clazz.getName()).append('#');
        sb.append(methodName).append('#');
        for (Object arg : args) {
            sb.append(arg != null ? arg.getClass().getName() : null).append(';');
        }
        return sb.toString();
    }
    /**
     * Wstepny rozmiar bufora przeznaczonego na klucz budowany
     * w metodzie {@link #createMethodKey(java.lang.Class, java.lang.String, java.lang.Object[])}.
     */
    static final int INITIAL_KEY_CAPACITY = 150;

    /**
     * Getter dla methodMap.
     *
     * @return cache metod
     */
    protected Map<String, Method> getMethodMap() {
        return methodMap;
    }
}
