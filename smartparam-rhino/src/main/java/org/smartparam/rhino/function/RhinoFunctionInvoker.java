package org.smartparam.rhino.function;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.function.FunctionInvoker;

/**
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class RhinoFunctionInvoker implements FunctionInvoker<RhinoFunction> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<Integer, Function> compileCache = new ConcurrentHashMap<Integer, Function>();

    private Map<String, Object> globalJavaObjects;

    @Override
    public Object invoke(RhinoFunction function, ParamContext ctx) {
        return call(function, ctx);
    }

    @Override
    public Object invoke(RhinoFunction function, Object... args) {
        return call(function, args);
    }

    public void invalidate(int functionId) {
        logger.debug("invalidating compiled function for id: {}", functionId);
        compileCache.remove(functionId);
    }

    public void invalidate() {
        logger.debug("invalidating all compiled functions");
        compileCache.clear();
    }

    public void setGlobalJavaObjects(Map<String, Object> globalJavaObjects) {
        this.globalJavaObjects = globalJavaObjects;
    }

    private Object call(RhinoFunction f, Object... args) {
        try {
            Function js = findCompiledFunction(f);
            return internalCall(js, args);
        } catch (Exception e) {
            throw new SmartParamException(
                    SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e,
                    "Failed to invoke rhino function: \n" + f + "\nwith args: " + Arrays.toString(args));
        }
    }

    private Object internalCall(Function js, Object... args) {
        Object result = null;
        Context cx = Context.enter();

        try {
            Scriptable scope = js.getParentScope();
            result = js.call(cx, scope, scope, args);
        } finally {
            Context.exit();
        }

        return unwrap(result);
    }

    private Function findCompiledFunction(RhinoFunction f) {
        Function js = compileCache.get(f.getId());
        if (js == null) {
            js = compile(createFullBody(f));
            compileCache.put(f.getId(), js);
        }
        return js;
    }

    private Function compile(String fullBody) {
        logger.debug("compiling rhino function: \n{}", fullBody);

        Context cx = Context.enter();
        cx.setOptimizationLevel(OPTIMIZATION_LEVEL);

        try {
            Scriptable scope = initCompileScope(cx);
            return cx.compileFunction(scope, fullBody, "", 0, null);
        } finally {
            Context.exit();
        }
    }

    private Scriptable initCompileScope(Context cx) {
        Scriptable scope = cx.initStandardObjects();

        if (globalJavaObjects != null) {
            for (Map.Entry<String, Object> e : globalJavaObjects.entrySet()) {
                String variableName = e.getKey();
                Object variableObj = e.getValue();
                scope.put(variableName, scope, Context.javaToJS(variableObj, scope));
            }
        }

        return scope;
    }

    /**
     * Tworzy pelny tekst funkcji javascript wg schematu:
     * <pre>
     * function $internalName($args) {$body}
     * </pre> przy czym:
     * <ol>
     * <li>jesli funkcja ma podane argumenty (oddzielone przecinkiem) lub string
     * pusty, to $args oznacza te argumenty
     * <li>jesli funkcja ma argumenty rowne null, to $args jest renderowane jako
     * 'ctx'
     * </ol>
     * na przyklad:
     * <pre>
     * args : "a,b,c"   - function F_11(a,b,c) {$body}
     * args : ""        - function F_11() {$body}
     * args : null      - function F_11(ctx) {$body}
     * </pre>
     *
     * @param f funkcja rhino
     *
     * @return pelny tekst funkcji javascript
     */
    String createFullBody(RhinoFunction f) {
        StringBuilder sb = new StringBuilder(f.getBody().length() + INITIAL_EXTENT);
        sb.append("function ").append(internalName(f));
        sb.append('(');
        sb.append(f.getArgs() != null ? f.getArgs() : "ctx");
        sb.append(") {");
        sb.append(f.getBody());
        sb.append('}');
        return sb.toString();
    }
    /**
     * Liczba znakow, o ktore zostanie powiekszony rozmiar StringBuildera
     * podczas konstruowania pelnego ciala funkcji.
     */
    private static final int INITIAL_EXTENT = 256;

    /**
     * Parametr ustawiajacy sile optymalizacji podczas kompilacji funkcji rhino
     * do bytecode'u.
     */
    private static final int OPTIMIZATION_LEVEL = 9;

    /**
     * Tworzy wewnetrzna nazwe funkcji, ktora zostanie uzyta podczas kompilacji
     * funkcji javascriptowej reprezentowanej przez podana RhinoFunction. Nazwa
     * funkcji bedzie unikalna pod warunkiem, ze identyfikatory funkcji
     * rhinowskich (f.getId()) sa unikalne.
     *
     * @param f podana funkcja
     *
     * @return nazwa wewnetrzna
     */
    String internalName(RhinoFunction f) {
        return "F_" + f.getId();
    }

    /**
     * Zamienia rezultat zwrocony przez funkcje rhino na obiekt javowy, jesli
     * rezultat jest wrapperem.
     *
     * @param result obiekt zwrocony przez funkcje rhino
     *
     * @return obiekt skonwertowany na typ javowy
     */
    Object unwrap(Object result) {

        if (result instanceof NativeJavaObject) {
            return ((NativeJavaObject) result).unwrap();
        }

        if (result instanceof NativeArray) {
            NativeArray nativeArray = ((NativeArray) result);
            Object[] array = new Object[(int) nativeArray.getLength()];
            for (Object ix : nativeArray.getIds()) {
                int index = (Integer) ix;
                array[index] = nativeArray.get(index, null);
            }
            return array;
        }

        if (result instanceof Undefined) {
            return null;
        }

        return result;
    }
}
