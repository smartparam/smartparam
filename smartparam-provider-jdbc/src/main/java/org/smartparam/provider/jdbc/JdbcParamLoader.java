/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartparam.provider.jdbc;

import java.util.List;
import javax.sql.DataSource;
import org.smartparam.engine.core.loader.ParamLoader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 * @since 0.1.0
 */
public class JdbcParamLoader implements ParamLoader {

    /**
     * JDBC DataSource to obtain connections from.
     */
    private DataSource dataSource;

    /**
     * This variable will be used to set the fetchSize property on jdbc statements.
     * ...
     * If this variable is set to a non-zero value, it will be used for setting the
     * fetchSize property on statements used for query processing.
     */
    private int fetchSize = 100;
    //TODO #ph rethink default and comment

    @Override
    public Parameter load(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }
}
