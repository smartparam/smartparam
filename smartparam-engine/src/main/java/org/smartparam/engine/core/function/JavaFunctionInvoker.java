package org.smartparam.engine.core.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.ParamDefinitionException;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.model.functions.JavaFunction;
import org.smartparam.engine.util.Printer;

/**
 * Klasa odpowiedzialna za wykonywania funkcji (z repozytorium) typu {@link JavaFunction}.
 * Klasa zapewnia:
 * <ul>
 * <li> odnajdowanie metod pasujacych do wywolania (sprawdzanie kompatybilnosci parametrow),
 * <li> cache dla refleksji przyspieszajacy znajdowanie metody okolo 5 razy,
 * <li> cache dla obiektow, na ktorych sa wolane metody,
 * <li> cache dla uzywanych klas.
 * </ul>
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class JavaFunctionInvoker extends CoreJavaInvoker implements FunctionInvoker<JavaFunction> {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Cache dla obiektow klas znalezionych przez refleksje.
     */
    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<String, Class<?>>();

    /**
     * Cache dla obiektow instancjonowanych przez refleksje.
     */
    private final Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<Class<?>, Object>();

    /**
     * {@inheritDoc}
     *
     * @throws ParamException jesli wywolanie zostanie przerwane przez jakikolwiek wyjatek,
     *                        kod bledu: {@link org.smartparam.engine.core.exception.ParamException.ErrorCode#FUNCTION_INVOKE_ERROR}
     */
    @Override
    public Object invoke(JavaFunction function, ParamContext ctx) {
        return call(function, ctx);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ParamException jesli wywolanie zostanie przerwane przez jakikolwiek wyjatek,
     *                        kod bledu: {@link org.smartparam.engine.core.exception.ParamException.ErrorCode#FUNCTION_INVOKE_ERROR}
     */
    @Override
    public Object invoke(JavaFunction function, Object... args) {
        return call(function, args);
    }

    /**
     * Wywoluje funkcje <tt>f</tt> z argumentami <tt>args</tt>.
     * <p>
     * W tym celu:
     * <ol>
     * <li>znajduje klase o nazwie <tt>f.className</tt> korzystajac z cache'a,
     * <li>znajduje metode o nazwie <tt>f.methodName</tt> korzystajac z cache'a
     * <li>znajduje obiekt odpowiedniej klasy korzystajac z cache'a (jesli metoda nie jest statyczna)
     * </ol>
     *
     * @param f    funkcja typu java
     * @param args argumenty wywolania
     *
     * @return obiekt zwrocony przez funkcje
     *
     * @throws ParamException jesli wywolanie zostanie przerwane przez jakikolwiek wyjatek,
     *                        kod bledu: {@link org.smartparam.engine.core.exception.ParamException.ErrorCode#FUNCTION_INVOKE_ERROR}
     */
    Object call(JavaFunction f, Object... args) {
        Class<?> clazz = findClass(f.getClassName());
        Method m = findMethod(clazz, f.getMethodName(), args);

        Object instance = null;

        if (!Modifier.isStatic(m.getModifiers())) {
            instance = findInstance(clazz);
        }

        return invoke(instance, m, args);
    }

    /**
     * Pobiera obiekt klasy poprzez refleksje.
     * Korzysta przy tym z cache'a {@link #classMap}.
     *
     * @param className pelnopakietowa nazwa klasy
     *
     * @return obiekt klasy
     *
     * @throws ParamDefinitionException jesli nie mozna wczytac klasy <tt>className</tt>
     */
    public Class<?> findClass(String className) {
        Class<?> clazz = classMap.get(className);
        if (clazz == null) {
            clazz = loadClass(className);
            classMap.put(className, clazz);
        }
        return clazz;
    }

    /**
     * Pobiera obiekt klasy poprzez refleksje.
     *
     * @param className pelnopakietowa nazwa klasy
     *
     * @return obiekt klasy
     *
     * @throws ParamDefinitionException jesli nie mozna wczytac klasy <tt>className</tt>
     */
    Class<?> loadClass(String className) {
        logger.debug("loading class: {}", className);

        try {
            return Class.forName(className);

        } catch (ClassNotFoundException e) {
            throw new ParamDefinitionException(
                    ParamException.ErrorCode.FUNCTION_INVOKE_ERROR, e,
                    "Unable to load defined java class: " + className);
        }
    }

    /**
     * Zwraca instancje klasy <tt>clazz</tt>. Pobiera ja z cache'a instancji.
     *
     * @param clazz klasa
     *
     * @return instancja pobrana z cache'a lub utworzona (przy pierwszym uzyciu)
     */
    Object findInstance(Class<?> clazz) {
        Object obj = instanceMap.get(clazz);
        if (obj == null) {
            obj = createInstance(clazz);
            instanceMap.put(clazz, obj);
        }
        return obj;
    }

    /**
     * Tworzy instancje klasy <tt>clazz</tt> uzywajac refleksji.
     * Klasa musi miec publiczny konstruktor bezargumentowy.
     *
     * @param clazz klasa
     *
     * @return nowoutworzona instancja tej klasy
     *
     * @throws ParamDefinitionException jesli nie udalo sie utworzyc nowej instancji
     */
    Object createInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            logger.error("", e);
            throw new ParamDefinitionException(
                    ParamException.ErrorCode.FUNCTION_INVOKE_ERROR, e,
                    "Error instantiating class: " + clazz + ", msg=" + e.getMessage());
        }
    }

    /**
     * Zwraca zrzut zawartosci 3 cache'y:
     * <ul>
     * <li>classMap - cache'a klas
     * <li>methodMap - cache'a metod
     * <li>instanceMap - cache'a instancji
     * </ul>
     *
     * @return sformatowany zrzut zawartosci cache'y
     */
    public String dumpStatistics() {
        StringBuilder sb = new StringBuilder();

        sb.append("JavaFunctionEvaluator:");
        sb.append(Printer.print(getClassMap().values(), "cached classes"));
        sb.append(Printer.print(getMethodMap().values(), "cached methods"));
        sb.append(Printer.print(getInstanceMap().keySet(), "cached instances"));

        return sb.toString();
    }

    /**
     * Getter dla classMap.
     *
     * @return cache klas
     */
    Map<String, Class<?>> getClassMap() {
        return classMap;
    }

    /**
     * Getter dla instanceMap.
     *
     * @return cache instancji
     */
    Map<Class<?>, Object> getInstanceMap() {
        return instanceMap;
    }
}
