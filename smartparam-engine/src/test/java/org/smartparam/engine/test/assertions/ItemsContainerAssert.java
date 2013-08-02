package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.ItemsContainer;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ItemsContainerAssert extends AbstractAssert<ItemsContainerAssert, ItemsContainer<?, ?>> {

    private ItemsContainerAssert(ItemsContainer<?, ?> actual) {
        super(actual, ItemsContainerAssert.class);
    }

    public static ItemsContainerAssert assertThat(ItemsContainer<?, ?> actual) {
        return new ItemsContainerAssert(actual);
    }

    public ItemsContainerAssert hasItems() {
        Assertions.assertThat(actual.registeredItems()).isNotEmpty();
        return this;
    }

}
