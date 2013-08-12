package org.smartparam.engine.assemblers;

import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.types.string.StringHolder;


import static org.fest.assertions.api.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;
import org.testng.annotations.Test;

/**
 * @author Przemek Hertel
 */
public class GenericEnumAssemblerTest {

    private GenericEnumAssembler asm = new GenericEnumAssembler();

    @Test
    public void testFindEnum() {

        // przygotowanie danych testowych i oczekiwanych wynikow
        Object[][] tests = {
            {"A3", LetterType.class, LetterType.A3},
            {"A5", LetterType.class, LetterType.A5},
            {"PESEL", IdType.class, IdType.PESEL},
            {"PASSPORT", IdType.class, IdType.PASSPORT},
            {null, IdType.class, null}
        };

        // wykonanie testow
        for (Object[] test : tests) {
            String code = (String) test[0];
            Class<?> resultClass = (Class<?>) test[1];
            Object expectedEnumObject = test[2];

            // przygotowanie argumentow
            AbstractHolder value = new StringHolder(code);
            ParamContext ctx = new DefaultContext().withResultClass(resultClass);

            // test
            Object resultObject = asm.findEnum(value, ctx);
            assertThat(resultObject).isSameAs(expectedEnumObject);
        }
    }

    @Test
    public void testFindEnum__illegalCode() {

        // przygotowanie argumentow
        StringHolder value = new StringHolder("pesel");
        ParamContext ctx = new DefaultContext().withResultClass(IdType.class);

        // niepoprawny kod, assembler powinien rzucic wyjatek
        catchException(asm).findEnum(value, ctx);

        assertThat(caughtException()).isNotNull().isInstanceOf(IllegalArgumentException.class);
    }

    private enum LetterType {

        A3,
        A4,
        A5

    }

    private enum IdType {

        PESEL,
        PASSPORT

    }
}
