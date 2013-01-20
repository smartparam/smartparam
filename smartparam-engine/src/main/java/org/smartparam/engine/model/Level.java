package org.smartparam.engine.model;

/**
 * Klasa reprezenuje definicje poziomu na poziomie parametru.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface Level {

    /**
     * Returns parent parameter of this level.
     *
     * @return parameter owning this level
     */
    Parameter getParameter();

    /**
     * Returns short description of level.
     *
     * @return short description
     */
    String getLabel();

    /**
     * Returns label key, that can be used to fetch it from message bundle.
     *
     * @return label key
     */
    String getLabelKey();

    /**
     * Position of level in parameter level array.
     *
     * @return position number
     */
    int getOrderNo();

    /**
     * Returns function for evaluating value of level using current context.
     *
     * @return function registered in function repository
     */
    Function getLevelCreator();

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
    String getMatcherCode();

    /**
     * Returns function that validates if level value is correct (valid), can be null.
     *
     * @return function to validate contents of level
     */
    Function getValidator();
}
