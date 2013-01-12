package pl.generali.merkury.param.core.config;

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.type.AbstractType;
import pl.generali.merkury.param.types.integer.IntegerType;
import pl.generali.merkury.param.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class TypeProviderTest {

    private IntegerType intType;
    private StringType strType;
    private StringType chrType;
    private Map<String, AbstractType<?>> cases;

    @Before
    public void prepareUseCases() {

        // konfiguracja zaleznosci
        intType = new IntegerType();
        strType = new StringType();
        chrType = new StringType();

        // oczekiwane wyniki wyszukiwania
        cases = new HashMap<String, AbstractType<?>>();
        cases.put("int", intType);
        cases.put("str", strType);
        cases.put("chr", chrType);
        cases.put("INT", null);
        cases.put("STR", null);
        cases.put("other", null);
    }

    /**
     * Sprawdza zgodnosc typow otrzymywanych z providera
     * z typami oczekiwanymi, skonfigurowanymi w mapie cases.
     */
    private void checkTypeProvider(TypeProvider tp) {
        for (Map.Entry<String, AbstractType<?>> e : cases.entrySet()) {
            String code = e.getKey();
            AbstractType<?> expectedType = e.getValue();
            AbstractType<?> actualType = tp.getType(code);
            assertEquals(expectedType, actualType);
        }
    }

    @Test
    public void testRegisterType() {

        // utworzenie testowanego obiektu
        TypeProvider tp = new TypeProvider();
        tp.registerType("int", intType);
        tp.registerType("str", strType);
        tp.registerType("chr", chrType);

        // sprawdzenie wynikow testu
        checkTypeProvider(tp);
    }

    @Test
    public void testSetTypeMap() {

        // przygotowanie zaleznosci
        Map<String, AbstractType<?>> types = new HashMap<String, AbstractType<?>>();
        types.put("int", intType);
        types.put("str", strType);
        types.put("chr", chrType);

        // utworzenie testowanego obiektu
        TypeProvider tp = new TypeProvider();
        tp.setTypeMap(types);

        // sprawdzenie wynikow testu
        checkTypeProvider(tp);
    }

    @Test
    public void testRegisterType__nonUniqueCode() {

        // utworzenie testowanego obiektu
        TypeProvider tp = new TypeProvider();
        tp.registerType("int", intType);
        tp.registerType("str", strType);

        // powtorna rejestracja typu int - skutkuje wyjatkiem
        try {
            tp.registerType("int", new IntegerType());
            fail();
        }
        catch(ParamException pe) {
            assertEquals(ParamException.ErrorCode.NON_UNIQUE_TYPE_CODE, pe.getErrorCode());
        }
    }
}
