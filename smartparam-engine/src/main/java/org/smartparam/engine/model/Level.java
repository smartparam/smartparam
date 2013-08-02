package org.smartparam.engine.model;

/**
 * Klasa reprezenuje definicje poziomu na poziomie parametru.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface Level {

    String getName();

    /**
     * Returns function for evaluating value of level using current context.
     *
     * @return function registered in function repository
     */
    String getLevelCreator();

    /**
     * Get type of values stored in level.
     *
     * @return level value type
     */
    String getType();

    /**
     * Is level an array of values.
     *
     * @return is array of values
     */
    boolean isArray();

    /**
     * Get code of matcher, that is used to match level value against the pattern.
     *
     * @return matcher code
     */
    String getMatcher();
}
