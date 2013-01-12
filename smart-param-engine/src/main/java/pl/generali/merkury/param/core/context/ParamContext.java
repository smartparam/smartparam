package pl.generali.merkury.param.core.context;

/**
 * @author Przemek Hertel
 */
public interface ParamContext {

    String[] getLevelValues();

    void setLevelValues(String... levelValues);

    Class<?> getResultClass();

    void setResultClass(Class<?> resultClass);
}
