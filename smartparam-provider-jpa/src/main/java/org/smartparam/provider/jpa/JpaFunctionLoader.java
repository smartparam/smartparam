package org.smartparam.provider.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.smartparam.engine.core.loader.FunctionLoader;
import org.smartparam.engine.model.Function;
import org.smartparam.provider.jpa.model.JpaFunction;

/**
 * JPA implementation of function loader.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class JpaFunctionLoader implements FunctionLoader {

    private EntityManager entityManager;

    public JpaFunctionLoader(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public Function load(String functionName) {
        Query query = entityManager.createNamedQuery(JpaFunction.LOAD_FUNCTION_QUERY).setParameter("name", functionName);
        return (Function) query.getSingleResult();
    }
}
