package org.smartparam.provider.hibernate;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.smartparam.engine.core.loader.ParamLoader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.provider.hibernate.model.HibernateParameter;
import org.smartparam.provider.hibernate.model.HibernateParameterEntry;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class HibernateLoader implements ParamLoader {

    private EntityManager entityManager;

    public HibernateLoader(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Parameter load(String parameterName) {
        Query query = entityManager.createNamedQuery(HibernateParameter.LOAD_PARAMETER_QUERY).setParameter("name", parameterName);
        return (Parameter) query.getSingleResult();
    }

    @Override
    public List<ParameterEntry> findEntries(String parameterName, String[] levelValues) {

        StringBuilder sb = new StringBuilder();
        sb.append(" select pe from HibernateParameterEntry pe");
        sb.append(" where pe.parameter.name = ?");
        for (int i = 0; i < levelValues.length; i++) {
            sb.append(" and pe.level").append(i + 1).append(" = ?");
        }

        TypedQuery<HibernateParameterEntry> query = entityManager.createQuery(sb.toString(), HibernateParameterEntry.class);
        query.setParameter(1, parameterName);

        String value;
        for (int i = 0; i < levelValues.length; i++) {
            value = levelValues[i];
            query.setParameter(2 + i, value);
        }

        return new LinkedList<ParameterEntry>(query.getResultList());
    }

}
