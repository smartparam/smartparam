package org.smartparam.provider.jdbc.dao.config;

/**
 * @author Przemek Hertel
 */
public class DefaultConfiguration implements Configuration {

    private String parameterTable = "sp_parameter";

    private String parameterLevelTable = "sp_parameter_level";

    private String parameterEntryTable = "sp_parameter_entry";

    public String getParameterTable() {
        return parameterTable;
    }

    public String getParameterLevelTable() {
        return parameterLevelTable;
    }

    public String getParameterEntryTable() {
        return parameterEntryTable;
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
