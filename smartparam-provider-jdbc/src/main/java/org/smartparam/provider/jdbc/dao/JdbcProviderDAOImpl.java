package org.smartparam.provider.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public JdbcProviderDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcParameter getParameter(String parameterName) {

        Connection conn = getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    " select id, label, type, input_levels, multivalue, cacheable, nullable, array_flag, array_separator"
                    + " from ecs_param"
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
        if (p.isArray()) {
            p.setArraySeparator(toChar(rs.getString("array_separator")));
        }
        return p;
    }

    private char toChar(String str) {
        return str != null && str.length() > 0 ? str.charAt(0) : ',';
    }

    public List<JdbcParameterLevel> getParameterLevels(int parameterId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<JdbcParameterEntry> getParameterEntries(int parameterId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to obtain connection from datasource", e);
            //TODO #ph [provider-jdbc] exception hierarchy
        }
    }
}
