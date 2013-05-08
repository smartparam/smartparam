package org.smartparam.engine.core.provider;

import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.repository.SmartTypeRepository;
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class SmartTypeProviderTest {

    private IntegerType intType;

    private StringType strType;

    private StringType chrType;

    private Map<String, Type<?>> cases;

    @Before
    public void prepareUseCases() {

        // konfiguracja zaleznosci
        intType = new IntegerType();
        strType = new StringType();
        chrType = new StringType();

        // oczekiwane wyniki wyszukiwania
        cases = new HashMap<String, Type<?>>();
        cases.put("int", intType);
        cases.put("str", strType);
        cases.put("chr", chrType);
        cases.put("INT", null);
        cases.put("STR", null);
        cases.put("other", null);
    }

    /**
     * Sprawdza zgodnosc typow otrzymywanych z providera z typami oczekiwanymi,
     * skonfigurowanymi w mapie cases.
     */
    private void checkTypeProvider(TypeRepository tp) {
        for (Map.Entry<String, Type<?>> e : cases.entrySet()) {
            String code = e.getKey();
            Type<?> expectedType = e.getValue();
            Type<?> actualType = tp.getType(code);
            assertEquals(expectedType, actualType);
        }
    }

    @Test
    public void testRegisterType() {

        // utworzenie testowanego obiektu
        TypeRepository tp = new SmartTypeRepository();
        tp.register("int", intType);
        tp.register("str", strType);
        tp.register("chr", chrType);

        // sprawdzenie wynikow testu
        checkTypeProvider(tp);
    }

    @Test
    public void testSetTypeMap() {

        // przygotowanie zaleznosci
        Map<String, Type<?>> types = new HashMap<String, Type<?>>();
        types.put("int", intType);
        types.put("str", strType);
        types.put("chr", chrType);

        // utworzenie testowanego obiektu
        SmartTypeRepository tp = new SmartTypeRepository();
        tp.setItems(types);

        // sprawdzenie wynikow testu
        checkTypeProvider(tp);
    }

    @Test
    public void testRegisterType__nonUniqueCode() {

        // utworzenie testowanego obiektu
        TypeRepository tp = new SmartTypeRepository();
        tp.register("int", intType);
        tp.register("str", strType);

        // powtorna rejestracja typu int - skutkuje wyjatkiem
        try {
            tp.register("int", new IntegerType());
            fail();
        } catch (SmartParamException pe) {
            assertEquals(SmartParamErrorCode.NON_UNIQUE_TYPE_CODE, pe.getErrorCode());
        }
    }
}
