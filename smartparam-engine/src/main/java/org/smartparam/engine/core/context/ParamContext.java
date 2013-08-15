package org.smartparam.engine.core.context;

/**
 * Context of parameter result evaluation. However complex the implementation,
 * it all boils down to vector (array) of values that will be matched to patterns
 * in parameter definition to find matching entries.
 *
 * @author Przemek Hertel
 */
public interface ParamContext {

    /**
     * @return array of values that form parameter query, has length equal to queried
     *         parameter input level count
     */
    String[] getLevelValues();

    /**
     * Can be used to inject query values directly to the context.
     *
     * @param levelValues array of values that form parameter query, should have
     *                    length equal to queried parameter input level count
     */
    void setLevelValues(String... levelValues);
}
