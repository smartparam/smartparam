package org.smartparam.engine.core.repository;

import org.smartparam.engine.core.Repository;
import org.smartparam.engine.core.index.Matcher;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface MatcherRepository extends Repository<Matcher> {

    Matcher getMatcher(String code);
}
