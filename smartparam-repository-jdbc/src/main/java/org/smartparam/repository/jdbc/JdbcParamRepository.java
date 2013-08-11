package org.smartparam.repository.jdbc;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.dao.JdbcProviderDAO;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamRepository implements ParamRepository {

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
        parameter.setEntries(dao.getParameterEntries(parameter.getId()));

        return parameter;
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private JdbcParameter loadMetadata(String parameterName) {
        JdbcParameter parameter = dao.getParameter(parameterName);
        parameter.setLevels(dao.getParameterLevels(parameter.getId()));

        return parameter;
    }

    //TODO #ph finish findEntries for non-cachable parameters
    @Override
    public List<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException("Not supported yet.");
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
