package org.smartparam.provider.jdbc.dao;

import java.util.List;
import java.util.Set;
import org.smartparam.provider.jdbc.model.JdbcParameter;
import org.smartparam.provider.jdbc.model.JdbcParameterEntry;
import org.smartparam.provider.jdbc.model.JdbcParameterLevel;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public interface JdbcProviderDAO {

    JdbcParameter getParameter(String parameterName);

    List<JdbcParameterLevel> getParameterLevels(int parameterId);

    Set<JdbcParameterEntry> getParameterEntries(int parameterId);
}
