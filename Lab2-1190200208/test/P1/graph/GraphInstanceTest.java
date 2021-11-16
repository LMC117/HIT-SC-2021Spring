/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 *
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    /* Testing strategy
     * testAdd():
     * 测试add()方法，分以下两种等价类:
     *      点尚未加入图
     *      点已经加入图
     *
     * testSet1~9():
     * 测试set()方法，等价类划分如下：
     *      按顶点划分：两个顶点均不存在，有一个顶点不存在，两个顶点均存在
     *      按边的存在与否划分：边已存在，边不存在
     *      按边权划分：正，0
     *      考虑总共可能的情况（如顶点不存在边却存在，显然不符合现实），共有8个等价类
     *
     * testRemove1~3():
     * 测试remove()方法，等价类划分如下：
     *      删除点是否在图中：是，否
     *      删除点是否有相连边：是，否
     *      考虑所有情况，共3个等价类
     *
     * testVertices():
     * 测试vertices()方法，等价类划分如下：
     *      点在图中
     *      点不在图中
     *
     * testSources():
     * 测试sources()方法，等价类划分如下：
     *      存在指向该点的节点
     *      不存在指向该点的节点
     *
     * testTargets():
     * 测试targets()方法，等价类划分如下：
     *      存在该点指向的节点
     *      不存在该点指向的节点
     */

    /**
     * Overridden by implementation-specific test classes.
     *
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        // you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    // other tests for instance methods of Graph

    /*  Testing strategy
     *  测试以下两种情况:
     *  点尚未加入图
     *  点已经加入图
     */
    @Test
    public void testAdd() {
        Graph<String> graph = emptyInstance();
        String a = "Mincoo";
        String b = "Lee";
        String c = "LMC";

        assertFalse(graph.vertices().contains(a));
        assertFalse(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        graph.add(a);
        assertTrue(graph.vertices().contains(a));
        assertFalse(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        graph.add(b);
        assertTrue(graph.vertices().contains(a));
        assertTrue(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        assertFalse(graph.add(a));
    }

    /* Testing Strategy
     * 按顶点划分：两个顶点均不存在，有一个顶点不存在，两个顶点均存在
     * 按边的存在与否划分：边已存在，边不存在
     * 按边权划分：正，0
     * 考虑总共可能的情况（如顶点不存在边却存在，显然不符合现实），共有8个等价类
     */
    @Test
    // 两顶点均不存在，边不存在，边权0
    public void testSet1() {
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSources = new HashMap<>();
        Map<String, Integer> testTargets = new HashMap<>();
        assertEquals(0, graph.set("a", "b", 0));
        assertEquals(testVertices, graph.vertices());
        assertEquals(testSources, graph.sources("a"));
        assertEquals(testSources, graph.targets("a"));
        assertEquals(testTargets, graph.sources("b"));
        assertEquals(testTargets, graph.targets("b"));
    }

    @Test
    // 两顶点均存在，边不存在，边权0
    public void testSet2() {
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSources = new HashMap<>();
        Map<String, Integer> testTargets = new HashMap<>();
        testVertices.add("a");
        testVertices.add("b");
        graph.add("a");
        graph.add("b");
        assertEquals(0, graph.set("a", "b", 0));
        assertEquals(0, graph.set("a", "b", 0));
        assertEquals(testVertices, graph.vertices());
        assertEquals(testSources, graph.sources("a"));
        assertEquals(testSources, graph.targets("a"));
        assertEquals(testTargets, graph.sources("b"));
        assertEquals(testTargets, graph.targets("b"));
    }

    @Test
    // 有一个顶点不存在，边不存在，边权0
    public void testSet3() {
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSources = new HashMap<>();
        Map<String, Integer> testTargets = new HashMap<>();
        graph.add("a");
        testVertices.add("a");
        assertEquals(0, graph.set("a", "b", 0));
        assertEquals(testVertices, graph.vertices());
        assertEquals(testSources, graph.sources("a"));
        assertEquals(testSources, graph.targets("a"));
        assertEquals(testTargets, graph.sources("b"));
        assertEquals(testTargets, graph.targets("b"));
    }

    @Test
    // 两顶点均不存在，边不存在，边权正
    public void testSet4() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();
        Map<String, Integer> testSourcesB = new HashMap<>();
        Map<String, Integer> testTargetsB = new HashMap<>();

        i = graph.set("a", "b", 2);
        assertEquals(0, i);

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());

        testTargetsA.put("b", 2);
        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));

        testSourcesB.put("a", 2);
        assertEquals(testSourcesB, graph.sources("b"));
        assertEquals(testTargetsB, graph.targets("b"));
    }

    @Test
    // 有一个顶点不存在，边不存在，边权正
    public void testSet5() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();
        Map<String, Integer> testSourcesB = new HashMap<>();
        Map<String, Integer> testTargetsB = new HashMap<>();

        graph.add("a");
        i = graph.set("a", "b", 2);
        assertEquals(0, i);

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());

        testTargetsA.put("b", 2);
        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));

        testSourcesB.put("a", 2);
        assertEquals(testSourcesB, graph.sources("b"));
        assertEquals(testTargetsB, graph.targets("b"));
    }

    @Test
    // 两顶点均存在，边不存在，边权正
    public void testSet6() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();
        Map<String, Integer> testSourcesB = new HashMap<>();
        Map<String, Integer> testTargetsB = new HashMap<>();

        graph.add("a");
        graph.add("b");
        i = graph.set("a", "b", 2);
        assertEquals(0, i);

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());

        testTargetsA.put("b", 2);
        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));

        testSourcesB.put("a", 2);
        assertEquals(testSourcesB, graph.sources("b"));
        assertEquals(testTargetsB, graph.targets("b"));
    }

    @Test
    // 两顶点均存在，边存在，边权正
    public void testSet7() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();
        Map<String, Integer> testSourcesB = new HashMap<>();
        Map<String, Integer> testTargetsB = new HashMap<>();

        graph.add("a");
        graph.add("b");
        graph.set("a", "b", 2);
        i = graph.set("a", "b", 4);
        assertEquals(2, i);

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());

        testTargetsA.put("b", 4);
        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));

        testSourcesB.put("a", 4);
        assertEquals(testSourcesB, graph.sources("b"));
        assertEquals(testTargetsB, graph.targets("b"));
    }

    @Test
    // 两顶点均存在，边存在，边权0
    public void testSet8() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();
        Map<String, Integer> testSourcesB = new HashMap<>();
        Map<String, Integer> testTargetsB = new HashMap<>();

        graph.add("a");
        graph.add("b");
        graph.set("a", "b", 2);
        i = graph.set("a", "b", 0);
        assertEquals(2, i);

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());

        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));

        assertEquals(testSourcesB, graph.sources("b"));
        assertEquals(testTargetsB, graph.targets("b"));
    }

    @Test
    // 两顶点均存在，边不存在，两顶点相同
    public void testSet9() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();

        graph.add("a");
        i = graph.set("a", "a", 2);
        assertEquals(0, i);

        testVertices.add("a");
        assertEquals(testVertices, graph.vertices());

        testTargetsA.put("a", 2);
        testSourcesA.put("a", 2);
        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));
    }

    @Test
    // 两顶点均存在，边存在，两顶点相同
    public void testSet10() {
        int i;
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesA = new HashMap<>();
        Map<String, Integer> testTargetsA = new HashMap<>();

        graph.add("a");
        graph.set("a", "a", 2);
        i = graph.set("a", "a", 4);
        assertEquals(2, i);

        testVertices.add("a");
        assertEquals(testVertices, graph.vertices());

        testTargetsA.put("a", 4);
        testSourcesA.put("a", 4);
        assertEquals(testSourcesA, graph.sources("a"));
        assertEquals(testTargetsA, graph.targets("a"));
    }

    /* Testing Strategy
     * 删除点是否在图中：是，否
     * 删除点是否有相连边：是，否
     * 考虑所有情况，共三个等价类
     */
    @Test
    // 删除点不在图中
    public void testRemove1() {
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();

        graph.set("a", "b", 2);

        assertFalse(graph.remove("d")); // 删除点不在图中

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());
    }

    @Test
    // 删除点在图中，且无相连边
    public void testRemove2() {
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();

        graph.set("a", "b", 2);
        graph.add("c");

        assertTrue(graph.remove("c")); // 删除点在图中，且无相连边

        testVertices.add("a");
        testVertices.add("b");
        assertEquals(testVertices, graph.vertices());
    }

    @Test
    // 删除点在图中，且存在相连边
    public void testRemove3() {
        Graph<String> graph = emptyInstance();
        Set<String> testVertices = new HashSet<>();
        Map<String, Integer> testSourcesB = new HashMap<>();
        Map<String, Integer> testTargetsB = new HashMap<>();
        Map<String, Integer> testSourcesC = new HashMap<>();
        Map<String, Integer> testTargetsC = new HashMap<>();

        graph.set("a", "b", 2);
        graph.set("a", "c", 4);

        assertTrue(graph.remove("a")); // 删除点在图中，且存在相连边

        testVertices.add("b");
        testVertices.add("c");
        assertEquals(testVertices, graph.vertices());

        assertEquals(testSourcesB, graph.sources("b"));
        assertEquals(testTargetsB, graph.targets("b"));

        assertEquals(testSourcesC, graph.sources("c"));
        assertEquals(testTargetsC, graph.targets("c"));
    }

    /* Testing Strategy
     * 点在图中
     * 点不在图中
     */
    @Test
    public void testVertices() {
        Graph<String> graph = emptyInstance();
        Set<String> testSet = new HashSet<>();

        assertEquals(testSet, graph.vertices());

        graph.add("a");
        testSet.add("a");
        assertEquals(testSet, graph.vertices());

        graph.add("b");
        testSet.add("b");
        assertEquals(testSet, graph.vertices());

        assertTrue(graph.vertices().contains("a"));
        assertTrue(graph.vertices().contains("b"));
        assertFalse(graph.vertices().contains("c"));
    }

    /* Testing Strategy
     * 存在指向该点的节点
     * 不存在指向该点的节点
     */
    @Test
    public void testSources() {
        Graph<String> graph = emptyInstance();
        Map<String, Integer> map = new HashMap<>();

        String a = "a";
        String b = "b";
        String c = "c";
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.set(a, b, 2);
        graph.set(b, a, 4);
        graph.set(c, b, 6);

        map.put(b, 4);
        assertEquals(map, graph.sources(a));
        map.clear();

        map.put(a, 2);
        map.put(c, 6);
        assertEquals(map, graph.sources(b));
        map.clear();

        assertEquals(map, graph.sources(c));
    }

    /* Testing Strategy
     * 存在该点指向的节点
     * 不存在该点指向的节点
     */
    @Test
    public void testTargets() {
        Graph<String> graph = emptyInstance();
        Map<String, Integer> map = new HashMap<>();

        String a = "a";
        String b = "b";
        String c = "c";
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.set(a, b, 2);
        graph.set(b, a, 4);
        graph.set(c, b, 6);
        graph.set(a, c, 8);

        map.put(b, 2);
        map.put(c, 8);
        assertEquals(map, graph.targets(a));
        map.clear();

        map.put(a, 4);
        assertEquals(map, graph.targets(b));
        map.clear();

        map.put(b, 6);
        assertEquals(map, graph.targets(c));
    }
}
