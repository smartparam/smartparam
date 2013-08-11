package org.smartparam.provider.jdbc.dao;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.provider.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public interface JdbcProviderDAO {

    void createSchema();

    JdbcParameter getParameter(String parameterName);

    Set<String> getParameterNames();

    List<Level> getParameterLevels(int parameterId);

    Set<ParameterEntry> getParameterEntries(int parameterId);
}
