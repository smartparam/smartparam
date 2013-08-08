package org.smartparam.provider.jdbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.provider.jdbc.dao.JdbcProviderDAO;
import org.smartparam.provider.jdbc.model.JdbcParameter;
import org.smartparam.provider.jdbc.model.JdbcParameterLevel;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamProvider implements ParamRepository {

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
    //TODO #ph rethink default and comment
    private int fetchSize = 100;

    @Override
    public Set<String> listParameters() {
        return dao.getParameterNames();
    }

    @Override
    public Parameter load(String parameterName) {
        JdbcParameter parameter = loadMetadata(parameterName);

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>(dao.getParameterEntries(parameter.getId()));
        parameter.setEntries(entries);

        return parameter;
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private JdbcParameter loadMetadata(String parameterName) {
        JdbcParameter parameter = dao.getParameter(parameterName);

        List<JdbcParameterLevel> levels = dao.getParameterLevels(parameter.getId());
        parameter.setLevels(new ArrayList<Level>(levels));
        return parameter;
    }

    //TODO #ph finish findEntries for non-cachable parameters
    @Override
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
