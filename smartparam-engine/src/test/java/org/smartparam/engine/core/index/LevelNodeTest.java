package org.smartparam.engine.core.index;

import java.util.Arrays;
import java.util.Map;
import org.junit.*;
import org.smartparam.engine.util.Formatter;
import static org.junit.Assert.*;
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
        node.add(new String[] {}, 10, null, currentLevelNumber);
        node.add(new String[] {}, 12, null, currentLevelNumber);

        // then
        assertThat(node).hasLeaves(2).leavesEqualTo(10, 12);
    }

    @Test
    public void shouldAddNodeToChildrenListWhenRootNodeIsNotLastInIndexTree() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);

        // when
        int currentLevelNumber = 0;
        root.add(new String[]{"A"}, 10, null, currentLevelNumber);
        root.add(new String[]{"*"}, 10, null, currentLevelNumber);

        // then
        assertThat(root).hasNoLeaves().hasDirectChild("A").hasDirectChild("*");
    }

    @Test
    public void testAdd() {

        // zaleznosci
        LevelIndex<Integer> index = levelIndex().withLevelCount(2).build();
        LevelNode<Integer> root = new LevelNode<Integer>(index);

        // test
        root.add(new String[]{"A", "B"}, 33, null, 0);
        root.add(new String[]{"X", "Y"}, 44, null, 0);

        // weryfikacja
        Map<String, LevelNode<Integer>> children = root.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.containsKey("A"));
        assertTrue(children.containsKey("X"));

        LevelNode<Integer> nodeA = children.get("A");
        assertSame(root, nodeA.getParent());
        verifyNode(nodeA, "A", false, true, null);

        LevelNode<Integer> nodeAB = nodeA.getChildren().get("B");
        assertSame(nodeA, nodeAB.getParent());
        verifyNode(nodeAB, "B", true, false, 33);
    }

    @Test
    public void testAdd__default() {

        // zaleznosci
        LevelIndex<Integer> index = levelIndex().withLevelCount(2).build();
        LevelNode<Integer> root = new LevelNode<Integer>(index);

        // test
        root.add(Arrays.asList("A", "B"), 33, null, 0);
        root.add(Arrays.asList("*", "Y"), 44, null, 0);

        // weryfikacja
        Map<String, LevelNode<Integer>> children = root.getChildren();
        assertEquals(1, children.size());
        assertTrue(children.containsKey("A"));
        assertNotNull(root.getDefaultNode());

        LevelNode<Integer> nodeDef = root.getDefaultNode();
        assertSame(root, nodeDef.getParent());
        verifyNode(nodeDef, "*", false, true, null);

        LevelNode<Integer> nodeDefY = nodeDef.getChildren().get("Y");
        assertSame(nodeDef, nodeDefY.getParent());
        verifyNode(nodeDefY, "Y", true, false, 44);
    }

    @Test
    public void testPrintNode() {
        // zaleznosci
        LevelIndex<Integer> index = levelIndex().withLevelCount(2).build();

        // testowany obiekt
        LevelNode<Integer> root = new LevelNode<Integer>(index);
        root.add(Arrays.asList("A", "B"), 33, null, 0);

        // wypelniany obiekt
        StringBuilder sb = new StringBuilder();

        // test
        root.printNode(sb, 0);

        // weryfikacja
        String expected = ""
                + "path : " + Formatter.NL
                + "    path : /A" + Formatter.NL
                + "        path : /A/B   (leaf=[33])" + Formatter.NL;

        assertEquals(expected, sb.toString());
    }

    @Test
    public void testFindNode() {

        // zaleznosci
        LevelIndex<Integer> index = levelIndex().withLevelCount(3).build();
        LevelNode<Integer> root = new LevelNode<Integer>(index);

        root.add(Arrays.asList("A", "B", "C"), 1, null, 0);
        root.add(Arrays.asList("A", "B", "*"), 9, null, 0);
        root.add(Arrays.asList("A", "E", "D"), 11, null, 0);
        root.add(Arrays.asList("A", "*", "D"), 12, null, 0);
        root.add(Arrays.asList("A", "*", "*"), 13, null, 0);
        root.add(Arrays.asList("*", "Z", "Z"), 21, null, 0);
        root.add(Arrays.asList("*", "Z", "*"), 22, null, 0);
        root.add(Arrays.asList("*", "*", "*"), 99, null, 0);

        // przypadki testowe
        String[][] testcases = {
            {"A", "B", "C"},
            {"A", "B", "X"},
            {"A", "E", "D"},
            {"A", "X", "D"},
            {"A", "X", "X"},
            {"V", "Z", "Z"},
            {"V", "Z", "A"},
            {"V", "V", "V"}
        };

        Integer[] expectations = {
            1,
            9,
            11,
            12,
            13,
            21,
            22,
            99
        };

        // testy
        for (int i = 0; i < testcases.length; i++) {
            String[] levelValues = testcases[i];
            Integer expectedResult = expectations[i];

            LevelNode<Integer> node = root.findNode(levelValues, 0);
            assertEquals(expectedResult, node.getLeafValue());
        }
    }

    private void verifyNode(LevelNode<?> node, String level, boolean isLeaf, boolean hasChildren, Object value) {
        assertEquals(level, node.getLevel());
        assertEquals(isLeaf, node.isLeaf());
        assertEquals(hasChildren, node.hasChildren());
        if (isLeaf) {
            assertEquals(value, node.getLeafValue());
        }
    }
}
