package org.smartparam.engine.core.index;

import org.smartparam.engine.core.index.LevelNode;
import org.smartparam.engine.core.index.LevelIndex;
import java.util.Arrays;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import org.smartparam.engine.util.Formatter;

/**
 * @author Przemek Hertel
 */
public class LevelNodeTest {

    @Test
    public void testConstructor() {

        // zaleznosci
        LevelIndex<Integer> index = new LevelIndex<Integer>(1);

        // test
        LevelNode<Integer> node = new LevelNode<Integer>(index);

        // weryfikacja
        assertSame(index, node.getIndex());
    }

    @Test
    public void testConstructor2() {

        // zaleznosci
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);
        LevelNode<Integer> root = new LevelNode<Integer>(index);

        // test
        LevelNode<Integer> node = new LevelNode<Integer>("A", root, index);

        // weryfikacja
        assertSame(index, node.getIndex());
        assertSame(root, node.getParent());
        assertEquals("A", node.getLevel());
    }

    @Test
    public void testAdd() {

        // zaleznosci
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);
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
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);
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
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);

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
    public void testToString() {
        // zaleznosci
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);
        LevelNode<Integer> root = new LevelNode<Integer>(index);
        root.add(Arrays.asList("A", "B"), 33, null, 0);     // A ma children=1
        root.add(Arrays.asList("E", "*"), 99, null, 0);     // E ma tylko defaultNode

        LevelNode<Integer> nodeA = root.getChildren().get("A");     // /A
        LevelNode<Integer> nodeB = nodeA.getChildren().get("B");    // /A/B
        LevelNode<Integer> nodeE = root.getChildren().get("E");     // /E
        LevelNode<Integer> nodeX = nodeE.getDefaultNode();          // /E/*

        // testowane obiekty
        LevelNode<?>[] nodes = new LevelNode<?>[]{
            root,
            nodeA,
            nodeB,
            nodeE,
            nodeX
        };

        // oczekiwane wyniki w fromie regexp (ze wzgledu na zmienna kolejnosc w [0])
        String[] expected = {
            "LevelNode\\[level=null, path=, children=\\[(A, E|E, A)\\]\\]",
            "LevelNode\\[level=A, path=\\/A, children=\\[B\\]\\]",
            "LevelNode\\[level=B, path=\\/A\\/B, leaf=\\[33\\]\\]",
            "LevelNode\\[level=E, path=\\/E, children=null\\]",
            "LevelNode\\[level=\\*, path=\\/E\\/\\*, leaf=\\[99\\]\\]",
        };

        // test
        for (int i = 0; i < nodes.length; i++) {
            LevelNode<?> node = nodes[i];
            String expectedToString = expected[i];

            String result = node.toString();
            System.out.println("node = " + result);
            assertTrue(result.matches(expectedToString));
        }
    }

    @Test
    public void testFindNode() {

        // zaleznosci
        LevelIndex<Integer> index = new LevelIndex<Integer>(3);
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
