package org.smartparam.spring;

import org.junit.*;
import org.smartparam.engine.core.assembler.AssemblerMethod;
import org.smartparam.engine.core.provider.SmartAssemblerProvider;
import org.smartparam.engine.core.provider.SmartMatcherProvider;
import org.smartparam.engine.core.provider.TypeProvider;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.engine.SmartParamEngine;
import org.smartparam.engine.core.engine.SmartParamPreparer;
import org.smartparam.engine.core.loader.ParamLoader;
import org.smartparam.engine.mockBuilders.LevelMockBuilder;
import org.smartparam.engine.mockBuilders.ParameterEntryMockBuilder;
import org.smartparam.engine.mockBuilders.ParameterMockBuilder;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.spring.function.SpringFunction;
import org.smartparam.spring.function.SpringFunctionInvoker;
import org.smartparam.spring.mockBuilders.SpringFunctionMockBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * FIXME #ad move to smartparam-spring
 *
 * @author Przemek Hertel
 */
public class SpringConfigurationTest {

    private static ApplicationContext ctx;

    /**
     * Buduje kontekst z testowego pliku.
     */
    @BeforeClass
    public static void init() {
        ctx = new ClassPathXmlApplicationContext("test-app-config.xml");
    }

    @Test
    public void testTypeProvider() {
        TypeProvider tp = ctx.getBean(TypeProvider.class);

        assertNotNull(tp.getType("string"));
        assertNotNull(tp.getType("integer"));
        assertNotNull(tp.getType("number"));
    }

    @Test
    public void testAssemblerProvider() {

        // weryfikacja konfiguracji assemblerProvidera
        SmartAssemblerProvider asmProvider = ctx.getBean(SmartAssemblerProvider.class);

        // odszukanie assemblera
        AssemblerMethod asm = asmProvider.findAssembler(StringHolder.class, NumberType.class);

        // weryfikacja budowania wyniku
        Object obj = asm.assemble(new StringHolder("PESEL"), new DefaultContext(NumberType.class));
        assertEquals(NumberType.PESEL, obj);

        obj = asm.assemble(new StringHolder("REGON"), new DefaultContext().withResultClass(NumberType.class));
        assertEquals(NumberType.REGON, obj);
    }

    @Test
    public void testGetResult() {

        // zaleznosci
        Parameter par = buildParam();

        // konfiguracja zaleznosci
        ParamLoader loader = mock(ParamLoader.class);
        when(loader.load("par")).thenReturn(par);
        SmartParamPreparer paramProvider = ctx.getBean(SmartParamPreparer.class);
        paramProvider.setLoader(loader);

        // pobranie testowanego obiektu
        SmartParamEngine engine = ctx.getBean(SmartParamEngine.class);

        // sprawdzenie wynikow testu
        assertSame(NumberType.PESEL, engine.getResult("par", NumberType.class, new LevelValues("A")));
        assertSame(NumberType.REGON, engine.getResult("par", NumberType.class, new LevelValues("B")));
        assertSame(NumberType.PASSPORT, engine.getResult("par", NumberType.class, new LevelValues("F")));

        assertSame(NumberType.PESEL, engine.getResult("par", new DefaultContext(NumberType.class, new String[]{"A"})));
        assertSame(NumberType.REGON, engine.getResult("par", new DefaultContext(new String[]{"B"}, NumberType.class)));
    }

    @Test
    public void testSpringFunctionInvoker() {
        SpringFunctionInvoker invoker = ctx.getBean(SpringFunctionInvoker.class);

        SpringFunction f = SpringFunctionMockBuilder.function("stringType", "decode");
        Object result = invoker.invoke(f, "text");
        assertEquals(new StringHolder("text"), result);

        f = SpringFunctionMockBuilder.function("integerType", "newArray");
        result = invoker.invoke(f, 7);
        assertTrue(result instanceof IntegerHolder[]);
        assertEquals(7, ((IntegerHolder[]) result).length);
    }

    @Test
    public void testMatcherProvider() {
        SmartMatcherProvider provider = ctx.getBean(SmartMatcherProvider.class);

        Level level = LevelMockBuilder.level().withMatcherCode(MCode.BETWEEN_II.toString()).get();

        assertEquals("between/ii", level.getMatcherCode());
    }

    private Parameter buildParam() {
        Parameter par = ParameterMockBuilder.parameter().withName("par").withType("string")
                .withLevels(
                LevelMockBuilder.level("string")).withEntries(
                ParameterEntryMockBuilder.parameterEntry("A", "PESEL"),
                ParameterEntryMockBuilder.parameterEntry("B", "REGON"),
                ParameterEntryMockBuilder.parameterEntry("*", "PASSPORT")).get();

        return par;
    }

    private enum NumberType {

        PESEL,
        REGON,
        PASSPORT

    }

    private enum MCode {

        BETWEEN_IE("between/ie"),
        BETWEEN_II("between/ii");

        private String code;

        private MCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
