package pl.generali.merkury.param.core.assembler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Skaner, ktory przeszukuje metody podanego obiektu w poszukiwaniu
 * metod bedacych tzw. assemblerami ({@link Assembler}).
 *
 * @see Assembler
 * @see AssemblerMethod
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class AssemblerAnnotationScanner {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(AssemblerAnnotationScanner.class);

    /**
     * Przeszukuje metody obiektu <tt>owner</tt> - zdefiniowane
     * bezposrednio w klasie ownera lub w klasach nadrzednych -
     * i zwraca liste tych metod, ktore sa oznaczone przy
     * pomocy adnotacji {@link Assembler}.
     *
     * @param owner obiekt ownera
     *
     * @return lista metod assemblera
     */
    public List<AssemblerMethod> scan(Object owner) {
        logger.debug("scanning assembler object: " + owner.getClass());

        List<AssemblerMethod> foundMethods = new ArrayList<AssemblerMethod>();

        Class<?> clazz = owner.getClass();
        while (clazz != null) {
            for (Method m : clazz.getDeclaredMethods()) {
                Assembler annotation = m.getAnnotation(Assembler.class);
                if (annotation != null) {
                    foundMethods.add(new AssemblerMethod(owner, m));
                }
            }
            clazz = clazz.getSuperclass();
        }

        logger.debug("found {} method(s)", foundMethods.size());
        return foundMethods;
    }
}
