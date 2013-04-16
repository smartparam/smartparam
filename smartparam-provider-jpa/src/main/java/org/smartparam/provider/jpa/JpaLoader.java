package org.smartparam.provider.jpa;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.smartparam.engine.core.loader.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.provider.jpa.model.JpaParameter;
import org.smartparam.provider.jpa.model.JpaParameterEntry;

/**
 * Loads parameters using provided connection.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class JpaLoader implements ParamRepository {

    private static final int FIND_ENTRIES_QUERY_LENGTH = 100;

    private EntityManager entityManager;

    public JpaLoader(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Parameter load(String parameterName) {
        Query query = entityManager.createNamedQuery(JpaParameter.LOAD_PARAMETER_QUERY).setParameter("name", parameterName);
        return (Parameter) query.getSingleResult();
    }

    @Override
    public List<ParameterEntry> findEntries(String parameterName, String[] levelValues) {

        StringBuilder sb = new StringBuilder(FIND_ENTRIES_QUERY_LENGTH);
        sb.append(" select pe from JpaParameterEntry pe");
        sb.append(" where pe.parameter.name = ?");
        for (int i = 0; i < levelValues.length; i++) {
            sb.append(" and pe.level").append(i + 1).append(" = ?");
        }

        TypedQuery<JpaParameterEntry> query = entityManager.createQuery(sb.toString(), JpaParameterEntry.class);
        query.setParameter(1, parameterName);

        String value;
        for (int i = 0; i < levelValues.length; i++) {
            value = levelValues[i];
            query.setParameter(2 + i, value);
        }

        return new LinkedList<ParameterEntry>(query.getResultList());
    }
}
