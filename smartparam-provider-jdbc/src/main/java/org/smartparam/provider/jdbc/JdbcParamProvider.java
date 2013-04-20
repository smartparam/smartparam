package org.smartparam.provider.jdbc;

import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.smartparam.engine.core.loader.ParamProvider;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.provider.jdbc.dao.JdbcProviderDAO;
import org.smartparam.provider.jdbc.model.JdbcParameter;
import org.smartparam.provider.jdbc.model.JdbcParameterEntry;
import org.smartparam.provider.jdbc.model.JdbcParameterLevel;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamProvider implements ParamProvider {

    /**
     * JDBC DataSource to obtain connections from.
     */
    private DataSource dataSource;
    
    private JdbcProviderDAO dao;

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
        JdbcParameter p = dao.getParameter(parameterName);
        List<JdbcParameterLevel> levels = dao.getParameterLevels(p.getId());
        Set<JdbcParameterEntry> entries = dao.getParameterEntries(p.getId());
        
        p.setLevels(levels);
        p.setEntries(entries);
        
        return p;
    }

    //TODO #ph finish findEntries for non-cachable parameters
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

    public void setJdbcProviderDao(JdbcProviderDAO dao) {
        this.dao = dao;
    }
    
}
