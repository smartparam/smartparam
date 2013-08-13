package org.smartparam.engine.core.index;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.LevelIndexTestBuilder.levelIndex;

/**
 * @author Przemek Hertel
 */
public class LevelNodeTest {

    @Test
    public void shouldAddNewNodeValueToLeavesWhenAddingToLastNodeInIndexTree() {
        // given
        LevelIndex<Integer> levelIndex = levelIndex().withLevelCount(0).build();
        LevelNode<Integer> node = new LevelNode<Integer>(levelIndex);

        // when
        int currentLevelNumber = 0;
        node.add(new String[]{}, 10, null, currentLevelNumber);

        // then
        assertThat(node).hasLeaves(1).leavesEqualTo(10);
    }

    @Test
    public void shouldAddNewNodeToChildrenListWhenCurrentNodeIsNotLastInIndexTree() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);

        // when
        int currentLevelNumber = 0;
        root.add(new String[]{"A"}, 10, null, currentLevelNumber);

        // then
        assertThat(root).hasNoLeaves().hasDirectChild("A");
    }

    @Test
    public void shouldFavourConcreteValuesOverDefaultWhenLookingForValue() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);
        root.add(new String[]{"*"}, 11, null, 0);
        root.add(new String[]{"A"}, 42, null, 0);

        // when
        LevelNode<Integer> node = root.findNode(new String[]{"A"}, 0);

        // then
        assertThat(node).leavesEqualTo(42);
    }

    @Test
    public void shouldFallBackToDefaultValueIfNoneOtherFound() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);
        root.add(new String[]{"*"}, 42, null, 0);
        root.add(new String[]{"A"}, 11, null, 0);

        // when
        LevelNode<Integer> node = root.findNode(new String[]{"B"}, 0);

        // then
        assertThat(node).leavesEqualTo(42);
    }

    @Test
    public void shouldReturnNullIfNothingFound() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);
        root.add(new String[]{"A"}, 10, null, 0);

        // when
        LevelNode<Integer> node = root.findNode(new String[]{"B"}, 0);

        // then
        assertThat(node).isNull();
    }
    
    @DataProvider(name = "findNodeSearchSet")
    public Object[][] provideFindNodeSearchSets() {
        return new Object[][]{
            {new String[]{"A", "B", "C"}, 1},
            {new String[]{"A", "B", "X"}, 9},
            {new String[]{"A", "E", "D"}, 11},
            {new String[]{"A", "X", "D"}, 12},
            {new String[]{"A", "X", "X"}, 13},
            {new String[]{"V", "Z", "Z"}, 21},
            {new String[]{"V", "Z", "A"}, 22},
            {new String[]{"V", "V", "V"}, 99}
        };
    }

    @Test(dataProvider = "findNodeSearchSet")
    public void shouldFindNodeFromTestSet(String[] levelValues, int expectedValue) {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(3).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);

        root.add(new String[]{"A", "B", "C"}, 1, null, 0);
        root.add(new String[]{"A", "B", "*"}, 9, null, 0);
        root.add(new String[]{"A", "E", "D"}, 11, null, 0);
        root.add(new String[]{"A", "*", "D"}, 12, null, 0);
        root.add(new String[]{"A", "*", "*"}, 13, null, 0);
        root.add(new String[]{"*", "Z", "Z"}, 21, null, 0);
        root.add(new String[]{"*", "Z", "*"}, 22, null, 0);
        root.add(new String[]{"*", "*", "*"}, 99, null, 0);

        // when
        LevelNode<Integer> node = root.findNode(levelValues, 0);

        // then
        assertThat(node).leavesEqualTo(expectedValue);
    }
}
