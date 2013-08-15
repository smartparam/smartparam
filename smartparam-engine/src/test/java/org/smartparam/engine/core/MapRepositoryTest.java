package org.smartparam.engine.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.bean.RepositoryObjectKey;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static com.googlecode.catchexception.CatchException.*;

/**
 *
 * @author Adam Dubiel
 */
public class MapRepositoryTest {

    private MapRepository<Object> mapRepository;

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void shouldRegisterObjectUnderKey() {
        // given
        mapRepository = new MapRepository<Object>(Object.class);

        // when
        mapRepository.register(RepositoryObjectKey.withKey("TEST_KEY"), this);

        // then
        assertThat(mapRepository).contains("TEST_KEY");
    }

    @Test
    public void shouldRegisterAllItemsFromCollection() {
        // given
        mapRepository = new MapRepository<Object>(Object.class);
        Map<RepositoryObjectKey, Object> items = new HashMap<RepositoryObjectKey, Object>();
        items.put(new RepositoryObjectKey("TEST_1", 0), new Object());
        items.put(new RepositoryObjectKey("TEST_2", 1), new Object());

        // when
        mapRepository.registerAll(items);

        // then
        assertThat(mapRepository).contains("TEST_1").contains("TEST_2");
    }

    @Test
    public void shouldReturnStoredItemsInOrder() {
        // given
        mapRepository = new MapRepository<Object>(Object.class, new LinkedHashMap<RepositoryObjectKey, Object>());
        Map<RepositoryObjectKey, Object> items = new HashMap<RepositoryObjectKey, Object>();
        items.put(new RepositoryObjectKey("TEST_1", 0), new Object());
        items.put(new RepositoryObjectKey("TEST_2", 1), new Object());
        mapRepository.registerAll(items);

        // when
        Map<String, Object> orderedItems = mapRepository.getItemsOrdered();

        // then
        assertThat(orderedItems).isInstanceOf(LinkedHashMap.class).hasSize(2);
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringDuplicateEntryUsingRegisterUnique() {
        // given
        mapRepository = new MapRepository<Object>(Object.class, new LinkedHashMap<RepositoryObjectKey, Object>());
        mapRepository.registerUnique("TEST", new Object());

        // when
        catchException(mapRepository).registerUnique("TEST", new Object());
        SmartParamException exception = (SmartParamException) caughtException();

        /// then
        assertThat(exception).hasErrorCode(SmartParamErrorCode.NON_UNIQUE_ITEM_CODE);
    }
}