package org.smartparam.provider.jpa.model;

import org.junit.Test;

/**
 * FIXME #ad move to implementation project
 *
 * @author Przemek Hertel
 */
public class RhinoFunctionTest {

    @Test
    public void noop() {
    }
//    RhinoFunction rf;
//
//    @Before
//    public void init() {
//        rf = new RhinoFunction();
//    }
//
//    @Test
//    public void testConstructor() {
//
//        // stworzenie obiektu
//        RhinoFunction f = new RhinoFunction();
//
//        // weryfikacja
//        assertNull(f.getArgs());
//        assertNull(f.getBody());
//    }
//
//    @Test
//    public void testConstructor2() {
//
//        // stworzenie obiektu
//        RhinoFunction f = new RhinoFunction("a,b", "return 1;");
//
//        // weryfikacja
//        assertEquals("a,b", f.getArgs());
//        assertEquals("return 1;", f.getBody());
//    }
//
//    @Test
//    public void testGetImplCode() {
//        assertEquals("rhino", rf.getImplCode());
//    }
//
//    @Test
//    public void testGetId() {
//
//        // inicjalizacja zerem
//        assertEquals(0, rf.getId());
//
//        // ustawienie id
//        rf.setId(9);
//
//        // weryfikacja
//        assertEquals(9, rf.getId());
//    }
//
//    @Test
//    public void testGetArgs() {
//        // test
//        rf.setArgs("a");
//
//        // weryfikacja
//        assertEquals("a", rf.getArgs());
//    }
//
//    @Test
//    public void testGetBody() {
//
//        // przykladowe body
//        String body = "return 1;";
//
//        // test
//        rf.setBody(body);
//
//        // weryfikacja
//        assertEquals(body, rf.getBody());
//    }
//
//    @Test
//    public void testSetFullCode() {
//
//        // dane testowe
//        String[] fullcodes = {
//            "function f1() {return 7;}",
//            " function f2(a,b) { return a+b; } ",
//            "\nfunction \n f3( a, b,c ) { \nreturn a*b*c; \n}"
//        };
//
//        // oczekiwane sparsowane argumenty
//        String[] args = {
//            "",
//            "a,b",
//            "a, b,c"
//        };
//
//        // oczekiwane sparsowane ciala
//        String[] bodies = {
//            "return 7;",
//            " return a+b; ",
//            " \nreturn a*b*c; \n"
//        };
//
//        // weryfikacja
//        for (int i = 0; i < fullcodes.length; i++) {
//            String fullcode = fullcodes[i];
//            String expectedArgs = args[i];
//            String expectedBody = bodies[i];
//
//            rf.setFullCode(fullcode);
//
//            assertEquals(expectedArgs, rf.getArgs());
//            assertEquals(expectedBody, rf.getBody());
//        }
//    }
//
//    @Test
//    public void testSetFullCode__badSyntax() {
//
//        // dane testowe - niepoprawna skladnia
//        String[] fullcodes = {
//            "fun f1() {return 7;}", // brak function
//            "function (a,b) { return a+b; } ", // brak nazwy funkcji
//            "function f3(a,b) return 7;", // brak nawiasu otwierajacego
//            "function f3(a,b) { return 7;", // brak nawiasu zamykajacego
//            "function f3(a,b) }{ return 7;", // nawias zamykajacy przed otwierajacym
//            null
//        };
//
//        // wykonanie testow
//        for (String fullcode : fullcodes) {
//            try {
//                rf.setFullCode(fullcode);
//                fail();
//            } catch (IllegalArgumentException e) {
//                System.out.println("ok: " + e.getMessage());
//            }
//        }
//    }
//
//    @Test
//    public void testToString() {
//
//        // inicjalizacja obiektu
//        rf.setId(17);
//        rf.setArgs("a,b");
//        rf.setBody("return 1;");
//
//        // oczekiwany wynik
//        String expected = "RhinoFunction#17[body=return 1;, args=a,b]";
//
//        // weryfikacja
//        assertEquals(expected, rf.toString());
//    }
}
