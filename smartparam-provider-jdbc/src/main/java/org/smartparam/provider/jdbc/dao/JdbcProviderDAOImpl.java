package org.smartparam.provider.jdbc.dao;

import org.smartparam.provider.jdbc.mapper.ParameterMapper;
import org.smartparam.provider.jdbc.mapper.StringMapper;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.provider.jdbc.dao.config.Configuration;
import org.smartparam.provider.jdbc.dao.config.DefaultConfiguration;
import org.smartparam.provider.jdbc.mapper.LevelMapper;
import org.smartparam.provider.jdbc.mapper.ParameterEntryMapper;
import org.smartparam.provider.jdbc.model.JdbcParameter;
import org.smartparam.provider.jdbc.query.JdbcQuery;
import org.smartparam.provider.jdbc.query.JdbcQueryRunner;
import org.smartparam.provider.jdbc.query.JdbcQueryRunnerImpl;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcProviderDAOImpl implements JdbcProviderDAO {

    private Configuration configuration = new DefaultConfiguration();

    private JdbcQueryRunner queryRunner;

    public JdbcProviderDAOImpl(DataSource dataSource) {
        this.queryRunner = new JdbcQueryRunnerImpl(dataSource);
    }

    @Override
    public JdbcParameter getParameter(String parameterName) {
        JdbcQuery query = JdbcQuery.query(" select id, input_levels, cacheable, nullable, array_separator"
                + " from " + configuration.getParameterTable()
                + " where name = :name");
        query.setString("name", parameterName);

        return queryRunner.queryForObject(query, new ParameterMapper());
    }

    @Override
    public Set<String> getParameterNames() {
        JdbcQuery query = JdbcQuery.query("select name from " + configuration.getParameterTable());

        return queryRunner.queryForSet(query, new StringMapper());
    }

    @Override
    public List<Level> getParameterLevels(int parameterId) {
        JdbcQuery query = JdbcQuery.query(" select id, order_no, label, type, matcher, level_creator, array_flag"
                + " from " + configuration.getParameterLevelTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForList(query, new LevelMapper(parameterId));
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(int parameterId) {
        JdbcQuery query = JdbcQuery.query("select id, level1, level2, level3, level4, level5, level6, level7, level8, value"
                + " from " + configuration.getParameterEntryTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForSet(query, new ParameterEntryMapper(parameterId));
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
