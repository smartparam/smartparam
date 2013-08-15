package org.smartparam.engine.test.assertions;

import java.util.Map;
import org.fest.assertions.api.Assertions;
import static org.fest.assertions.api.Fail.*;
import org.fest.assertions.api.MapAssert;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel
 */
public class RepositoryItemMapAssert<T> extends MapAssert<RepositoryObjectKey, T> {

    private RepositoryItemMapAssert(Map<RepositoryObjectKey, T> map) {
        super(map);
    }

    public static <T> RepositoryItemMapAssert<T> assertThat(Map<RepositoryObjectKey, T> map) {
        return new RepositoryItemMapAssert<T>(map);
    }

    public RepositoryItemMapAssert<T> containsRepositoryKey(RepositoryObjectKey key) {
        if(!actual.containsKey(key)) {
            fail("expected entry with key: " + key.getKey() + " but none found");
        }
        return this;
    }

    public RepositoryItemMapAssert<T> containsRepositoryKey(String key) {
        return containsRepositoryKey(new RepositoryObjectKey(key));
    }

    public RepositoryItemMapAssert<T> containsRepositoryKeys(String... keys) {
        if(keys.length != actual.size()) {
            fail("expected " + keys.length + " items in map, instead found " + actual.size());
        }

        for(RepositoryObjectKey repositoryKey : actual.keySet()) {
            containsRepositoryKey(repositoryKey);
        }

        return this;
    }

    public RepositoryItemMapAssert<T> containsObjectsThatAreNotSame(String objectOneRepositoryKey, String objectTwoRepositoryKey) {
        containsRepositoryKey(objectOneRepositoryKey);
        containsRepositoryKey(objectTwoRepositoryKey);

        Assertions.assertThat(actual.get(new RepositoryObjectKey(objectOneRepositoryKey)))
                .isNotSameAs(actual.get(new RepositoryObjectKey(objectTwoRepositoryKey)));

        return this;
    }

    public RepositoryItemMapAssert<T> containsObjectsThatAreSame(String objectOneRepositoryKey, String objectTwoRepositoryKey) {
        containsRepositoryKey(objectOneRepositoryKey);
        containsRepositoryKey(objectTwoRepositoryKey);

        Assertions.assertThat(actual.get(new RepositoryObjectKey(objectOneRepositoryKey)))
                .isSameAs(actual.get(new RepositoryObjectKey(objectTwoRepositoryKey)));

        return this;
    }

}
