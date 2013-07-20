package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.index.LevelNode;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class LevelNodeAssert extends AbstractAssert<LevelNodeAssert, LevelNode<?>> {

    private LevelNodeAssert(LevelNode<?> actual) {
        super(actual, LevelNodeAssert.class);
    }

    public static LevelNodeAssert assertThat(LevelNode<?> actual) {
        return new LevelNodeAssert(actual);
    }

    public LevelNodeAssert hasLeaves(int count) {
        Assertions.assertThat(actual.getLeafList()).hasSize(count);
        return this;
    }

    public LevelNodeAssert hasNoLeaves() {
        Assertions.assertThat(actual.getLeafList()).isNullOrEmpty();
        return this;
    }

    public LevelNodeAssert leavesEqualTo(Object... leafValues) {
        Assertions.assertThat(actual.getLeafList()).hasSize(leafValues.length);
        int index = 0;
        for (Object object : actual.getLeafList()) {
            Assertions.assertThat(object).isEqualTo(leafValues[index]);
            index++;
        }
        return this;
    }

    public LevelNodeAssert hasDirectChild(String childLevelValue) {
        actual.findNode(new String[]{childLevelValue}, 1);
        return this;
    }
}
