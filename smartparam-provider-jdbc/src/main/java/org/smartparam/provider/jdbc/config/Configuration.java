package org.smartparam.provider.jdbc.config;

import org.smartparam.jdbc.dialect.Dialect;

/**
 * @author Przemek Hertel
 */
public interface Configuration {

    Dialect getDialect();

    String getParameterTable();

    String getParameterLevelTable();

    String getParameterEntryTable();
}
