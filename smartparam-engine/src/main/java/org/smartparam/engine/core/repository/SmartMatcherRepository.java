package org.smartparam.engine.core.repository;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.index.Matcher;

/**
 * @see Matcher
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartMatcherRepository extends AbstractAnnotationScanningRepository<Matcher> implements MatcherRepository {

    private MapRepository<Matcher> innerRepository = new MapRepository<Matcher>(Matcher.class);

    @Override
    public void register(String code, Matcher matcher) {
        innerRepository.register(code, matcher);
    }

    @Override
    public Map<String, Matcher> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void setItems(Map<String, Matcher> objects) {
        innerRepository.setItemsUnordered(objects);
    }

    @Override
    public Matcher getMatcher(String code) {
        return innerRepository.getItem(code);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SmartParamMatcher.class;
}

    @Override
    protected void handleRegistration(RepositoryObjectKey key, Matcher objectToRegister) {
        register(key.getKey(), objectToRegister);
    }
}
