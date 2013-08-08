package org.smartparam.provider.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.provider.jdbc.dao.config.Configuration;
import org.smartparam.provider.jdbc.dao.config.DefaultConfiguration;
import org.smartparam.provider.jdbc.model.JdbcParameter;
import org.smartparam.provider.jdbc.model.JdbcParameterEntry;
import org.smartparam.provider.jdbc.model.JdbcParameterLevel;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcProviderDAOImpl implements JdbcProviderDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    private Configuration configuration = new DefaultConfiguration();

    public JdbcProviderDAOImpl() {
    }

    public JdbcProviderDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public JdbcParameter getParameter(String parameterName) {

        Connection conn = getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    " select id, label, type, input_levels, multivalue, cacheable, nullable, array_flag, array_separator"
                    + " from " + configuration.getParameterTable()
                    + " where name = ?");

            ps.setString(1, parameterName);

            rs = ps.executeQuery();

            if (rs.next()) {
                return readJdbcParameter(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch JdbcParamter", e);
            //TODO #ph own hierarchy
            /*
             * jesli poleci wyjatek: loguje 1. configuration, 2. sql
             */

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.error("Failed to cleanup resources", ex);
            }
        }
    }

    @Override
    public Set<String> getParameterNames() {
        Connection conn = getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("select name from " + configuration.getParameterTable());
            rs = ps.executeQuery();

            Set<String> names = new HashSet<String>();
            while(rs.next()) {
                names.add(rs.getString("name"));
            }

            return names;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch JdbcParamter", e);
            //TODO #ph own hierarchy
            /*
             * jesli poleci wyjatek: loguje 1. configuration, 2. sql
             */

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.error("Failed to cleanup resources", ex);
            }
        }
    }

    private JdbcParameter readJdbcParameter(ResultSet rs) throws SQLException {
        JdbcParameter p = new JdbcParameter();
        p.setId(rs.getInt("id"));
        p.setLabel(rs.getString("label"));
        p.setType(rs.getString("type"));
        p.setInputLevels(rs.getInt("input_levels"));
        p.setMultivalue(rs.getBoolean("multivalue"));
        p.setCacheable(rs.getBoolean("cacheable"));
        p.setNullable(rs.getBoolean("nullable"));
        p.setArray(rs.getBoolean("array_flag"));
        p.setArraySeparator(toChar(rs.getString("array_separator")));
        return p;
    }

    private JdbcParameterLevel readJdbcParameterLevel(ResultSet rs, int parameterId) throws SQLException {
        JdbcParameterLevel level = new JdbcParameterLevel();
        level.setId(rs.getInt("id"));
        level.setParameterId(parameterId);
        level.setOrderNo(rs.getInt("order_no"));
        level.setName(rs.getString("label"));
        level.setType(rs.getString("type"));
        level.setMatcher(rs.getString("matcher"));
        level.setArray(rs.getBoolean("array_flag"));
        //level.setLevelCreatorId(rs.getInt("level_creator_id"));
        return level;
    }

    private JdbcParameterEntry readJdbcParameterEntry(ResultSet rs, int parameterId) throws SQLException {
        JdbcParameterEntry e = new JdbcParameterEntry();
        e.setId(rs.getInt("id"));
        e.setParameterId(parameterId);
        e.setLevels(
                new String[] {
                    rs.getString("level1"),
                    rs.getString("level2"),
                    rs.getString("level3"),
                    rs.getString("level4"),
                    rs.getString("level5"),
                    rs.getString("level6"),
                    rs.getString("level7"),
                    rs.getString("level8")
                });
        e.setValue(rs.getString("value"));
        return e;
    }

    private char toChar(String str) {
        return str != null && str.length() > 0 ? str.charAt(0) : ',';
    }

    @Override
    public List<JdbcParameterLevel> getParameterLevels(int parameterId) {

        List<JdbcParameterLevel> result = new ArrayList<JdbcParameterLevel>();

        Connection conn = getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    " select id, order_no, label, type, matcher, array_flag, level_creator_id"
                    + " from " + configuration.getParameterLevelTable()
                    + " where param_id = ?");

            ps.setInt(1, parameterId);

            rs = ps.executeQuery();

            while (rs.next()) {
                result.add(readJdbcParameterLevel(rs, parameterId));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch parameter levels", e);
            //TODO #ph own hierarchy

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.error("Failed to cleanup resources", ex);
            }
        }
    }

    @Override
    public Set<JdbcParameterEntry> getParameterEntries(int parameterId) {

        Set<JdbcParameterEntry> result = new HashSet<JdbcParameterEntry>();

        Connection conn = getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(""
                    + " select id, level1, level2, level3, level4, level5, level6, level7, level8, value"
                    + " from " + configuration.getParameterEntryTable()
                    + " where param_id = ?");

            ps.setInt(1, parameterId);

            rs = ps.executeQuery();

            while (rs.next()) {
                result.add(readJdbcParameterEntry(rs, parameterId));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch parameter levels", e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.error("Failed to cleanup resources", ex);
            }
        }
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to obtain connection from datasource", e);
            //TODO #ph [provider-jdbc] exception hierarchy
        }
    }

    //TODO #ph [provider-jdbc] implement jdbc spring-like abstraction layer
    /**
     * Close the given JDBC ResultSet and ignore any thrown exception.
     */
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable ex) {
                logger.trace("Failed to close JDBC ResultSet", ex);
            }
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
