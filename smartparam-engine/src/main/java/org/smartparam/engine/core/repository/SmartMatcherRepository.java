package org.smartparam.engine.core.repository;

import java.util.Map;
import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.annotations.scanner.TypeScanner;
import org.smartparam.engine.core.MapRepository;
import org.smartparam.engine.core.index.Matcher;

/**
 * @see Matcher
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class SmartMatcherRepository implements MatcherRepository, TypeScanningRepository {

    private MapRepository<Matcher> innerRepository = new MapRepository<Matcher>(Matcher.class);

    @Override
    public void scanAnnotations(TypeScanner scanner) {
        Map<RepositoryObjectKey, Matcher> matchers = scanner.scanTypes(SmartParamMatcher.class);
        innerRepository.registerAll(matchers);
    }

    @Override
    public Matcher getMatcher(String code) {
        return innerRepository.getItem(code);
    }

    @Override
    public void register(String code, Matcher matcher) {
        innerRepository.register(code, matcher);
    }

    @Override
    public Map<String, Matcher> registeredItems() {
        return innerRepository.getItemsUnordered();
    }

    @Override
    public void registerAll(Map<String, Matcher> objects) {
        innerRepository.registerAllUnordered(objects);
    }
}
