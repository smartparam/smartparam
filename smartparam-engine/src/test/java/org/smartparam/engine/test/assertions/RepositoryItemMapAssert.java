package org.smartparam.engine.test.assertions;

import java.util.Map;
import org.fest.assertions.api.Assertions;
import static org.fest.assertions.api.Fail.*;
import org.fest.assertions.api.MapAssert;
import org.smartparam.engine.bean.RepositoryObjectKey;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RepositoryItemMapAssert extends MapAssert<RepositoryObjectKey, Object> {

    public RepositoryItemMapAssert(Map<RepositoryObjectKey, Object> map) {
        super(map);
    }

    public static RepositoryItemMapAssert assertThat(Map<RepositoryObjectKey, Object> map) {
        return new RepositoryItemMapAssert(map);
    }

    public RepositoryItemMapAssert containsRepositoryKey(RepositoryObjectKey key) {
        if(!actual.containsKey(key)) {
            fail("expected entry with key: " + key.getKey() + " but none found");
        }
        return this;
    }

    public RepositoryItemMapAssert containsRepositoryKey(String key) {
        return containsRepositoryKey(new RepositoryObjectKey(key));
    }

    public RepositoryItemMapAssert containsRepositoryKeys(String... keys) {
        if(keys.length != actual.size()) {
            fail("expected " + keys.length + " items in map, instead found " + actual.size());
        }
        
        for(RepositoryObjectKey repositoryKey : actual.keySet()) {
            containsRepositoryKey(repositoryKey);
        }

        return this;
    }

    public RepositoryItemMapAssert containsObjectsThatAreNotSame(String objectOneRepositoryKey, String objectTwoRepositoryKey) {
        containsRepositoryKey(objectOneRepositoryKey);
        containsRepositoryKey(objectTwoRepositoryKey);

        Assertions.assertThat(actual.get(new RepositoryObjectKey(objectOneRepositoryKey)))
                .isNotSameAs(actual.get(new RepositoryObjectKey(objectTwoRepositoryKey)));

        return this;
    }

    public RepositoryItemMapAssert containsObjectsThatAreSame(String objectOneRepositoryKey, String objectTwoRepositoryKey) {
        containsRepositoryKey(objectOneRepositoryKey);
        containsRepositoryKey(objectTwoRepositoryKey);

        Assertions.assertThat(actual.get(new RepositoryObjectKey(objectOneRepositoryKey)))
                .isSameAs(actual.get(new RepositoryObjectKey(objectTwoRepositoryKey)));

        return this;
    }

}
