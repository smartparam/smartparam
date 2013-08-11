package org.smartparam.repository.jdbc.config;

import org.smartparam.repository.jdbc.dialect.Dialect;

/**
 * @author Przemek Hertel
 */
public interface Configuration {

    Dialect getDialect();

    String getParameterTable();

    String getParameterLevelTable();

    String getParameterEntryTable();
}
