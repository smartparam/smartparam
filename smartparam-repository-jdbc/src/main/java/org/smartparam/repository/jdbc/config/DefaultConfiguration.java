package org.smartparam.repository.jdbc.config;

import org.smartparam.jdbc.dialect.Dialect;

/**
 * @author Przemek Hertel
 */
public class DefaultConfiguration implements Configuration {

    private Dialect dialect;

    private String parameterTable = "sp_parameter";

    private String parameterLevelTable = "sp_parameter_level";

    private String parameterEntryTable = "sp_parameter_entry";

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public String getParameterTable() {
        return parameterTable;
    }

    @Override
    public String getParameterLevelTable() {
        return parameterLevelTable;
    }

    @Override
    public String getParameterEntryTable() {
        return parameterEntryTable;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setParameterTable(String parameterTable) {
        this.parameterTable = parameterTable;
    }

    public void setParameterLevelTable(String parameterLevelTable) {
        this.parameterLevelTable = parameterLevelTable;
    }

    public void setParameterEntryTable(String parameterEntryTable) {
        this.parameterEntryTable = parameterEntryTable;
    }
}
