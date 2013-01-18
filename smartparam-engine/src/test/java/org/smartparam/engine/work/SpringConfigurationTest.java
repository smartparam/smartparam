package org.smartparam.engine.work;

import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.ParamProviderImpl;
import org.smartparam.engine.core.loader.ParamLoader;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.smartparam.engine.core.assembler.AssemblerMethod;
import org.smartparam.engine.core.config.AssemblerProvider;
import org.smartparam.engine.core.config.MatcherProvider;
import org.smartparam.engine.core.config.TypeProvider;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.function.SpringFunctionInvoker;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.functions.SpringFunction;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.string.StringHolder;

/**
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
        AssemblerProvider asmProvider = ctx.getBean(AssemblerProvider.class);

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
        ParamProviderImpl paramProvider = ctx.getBean(ParamProviderImpl.class);
        paramProvider.setLoader(loader);

        // pobranie testowanego obiektu
        ParamEngine engine = ctx.getBean(ParamEngine.class);

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

        SpringFunction f = new SpringFunction("stringType", "decode");
        Object result = invoker.invoke(f, "text");
        assertEquals(new StringHolder("text"), result);

        f = new SpringFunction("integerType", "newArray");
        result = invoker.invoke(f, 7);
        assertTrue(result instanceof IntegerHolder[]);
        assertEquals(7, ((IntegerHolder[]) result).length);
    }

    @Test
    public void testMatcherProvider() {
        MatcherProvider provider = ctx.getBean(MatcherProvider.class);

        Matcher betweenIE = provider.getMatcher("between/ie");
        System.out.println("betweenIE = " + betweenIE);

        Level level = new Level();
        level.setMatcherCode(MCode.BETWEEN_II);

        assertEquals("between/ii", level.getMatcherCode());
    }

    private Parameter buildParam() {
        Parameter par = new Parameter();
        par.setName("par");
        par.addLevel(new Level("string"));
        par.setType("string");
        par.addEntry(new ParameterEntry("A", "PESEL"));
        par.addEntry(new ParameterEntry("B", "REGON"));
        par.addEntry(new ParameterEntry("*", "PASSPORT"));
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
