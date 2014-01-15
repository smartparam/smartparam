/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.editor.core.identity;

import org.smartparam.editor.core.identity.DescribedCollection;
import org.smartparam.editor.core.identity.RepositoryName;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class DescribedCollectionTest {

    @Test
    public void shouldReturnImmutableCollectionOfItems() {
        // given
        DescribedCollection<String> collection = new DescribedCollection<String>(RepositoryName.from("repository"), "1");
        Collection<String> collectionItems = collection.items();

        // when
        catchException(collectionItems).add("2");

        // then
        assertThat(caughtException()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void shouldInterpreteVarargsConstructorAsOrderedListOfItems() {
        // given
        DescribedCollection<String> collection = new DescribedCollection<String>(RepositoryName.from("repository"), "1", "2");

        // when
        Collection<String> collectionItems = collection.itemsList();

        // then
        assertThat(collectionItems).isInstanceOf(List.class);
    }

    @Test
    public void shouldReturnImmutableCollectionAsListIfCreatedFromList() {
        // given
        DescribedCollection<String> collection = new DescribedCollection<String>(RepositoryName.from("repository"), Arrays.asList("1"));

        // when
        List<String> collectionItems = collection.itemsList();
        catchException(collectionItems).add("2");

        // then
        assertThat(caughtException()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void shouldFailWhenTryingToGetIncompatibleCollectionAsList() {
        // given
        DescribedCollection<String> collection = new DescribedCollection<String>(RepositoryName.from("repository"), new HashSet<String>(Arrays.asList("1")));

        // when
        catchException(collection).itemsList();

        // then
        assertThat(caughtException()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void shouldReturnFristItemInIterationOrderWhenAskingForFirstItem() {
        // given
        DescribedCollection<String> collection = new DescribedCollection<String>(RepositoryName.from("repository"), "1", "2");

        // when
        String item = collection.firstItem();

        // then
        assertThat(item).isEqualTo("1");
    }
}
