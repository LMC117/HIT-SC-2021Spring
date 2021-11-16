/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for ConcreteVerticesGraph.
 * <p>
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * <p>
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */

    // Testing strategy for ConcreteVerticesGraph.toString()
    // 对三个点的情况进行测试，观察toString方法是否出错

    // tests for ConcreteVerticesGraph.toString()

    @Test
    public void testToString() {
        Graph<String> graph = emptyInstance();
        graph.add("a");
        graph.add("b");
        graph.add("c");
        graph.set("a", "b", 1);
        graph.set("b", "c", 2);
        graph.set("c", "a", 3);

        assertEquals("当前点: a 当前点的父节点及权值: {c=3} 当前点的子节点及权值: {b=1}\n" +
                "当前点: b 当前点的父节点及权值: {a=1} 当前点的子节点及权值: {c=2}\n" +
                "当前点: c 当前点的父节点及权值: {b=2} 当前点的子节点及权值: {a=3}\n", graph.toString());
    }

    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex

    /*
     * Testing strategy for Vertex
     * testGetName():
     * 测试能否正确返回顶点名称
     * testGetSources():
     * 分两个等价类：
     * 父节点为空，父节点不为空
     * testGetTargets():
     * 分两个等价类：
     * 子节点为空，子节点不为空
     * testAddSources():
     * 按父节点是否存在划分：父节点存在，父节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     * testAddTarget():
     * 按子节点是否存在划分：子节点存在，子节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     * testRemoveSources():
     * 按父节点是否存在划分：父节点存在，父节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     * testRemoveTarget():
     * 按子节点是否存在划分：子节点存在，子节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     */

    // tests for operations of Vertex


    /*
     * Testing strategy
     * testGetName():
     * 测试能否正确返回顶点名称
     */
    @Test
    public void testGetName() {
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        assertEquals("a", a.getName());
        assertEquals("b", b.getName());
    }

    /*
     * testGetSources():
     * 分两个等价类：
     * 父节点为空，父节点不为空
     */
    @Test
    // 父节点为空
    public void testGetSources1() {
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();
        assertEquals(testMap, a.getSources());
    }

    @Test
    // 父节点不为空
    public void testGetSources2() {
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();

        a.addSource("b", 2);
        testMap.put("b", 2);

        assertEquals(testMap, a.getSources());

        a.addSource("c", 3);
        testMap.put("c", 3);

        assertEquals(testMap, a.getSources());
    }

    /*
     * testGetTargets():
     * 分两个等价类：
     * 子节点为空，子节点不为空
     */
    @Test
    // 子节点为空
    public void testGetTargets1() {
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();
        assertEquals(testMap, a.getTargets());
    }

    @Test
    // 子节点不为空
    public void testGetTargets2() {
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();

        a.addTarget("b", 2);
        testMap.put("b", 2);

        assertEquals(testMap, a.getTargets());

        a.addTarget("c", 3);
        testMap.put("c", 3);

        assertEquals(testMap, a.getTargets());
    }

    /*
     * testAddSources():
     * 按父节点是否存在划分：父节点存在，父节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     */
    @Test
    // 父节点不存在，边不存在
    public void testAddSources1() {
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();

        int i = a.addSource("b", 2);
        testMap.put("b", 2);

        assertEquals(0, i);
        assertEquals(testMap, a.getSources());

        int j = a.addSource("c", 3);
        testMap.put("c", 3);

        assertEquals(0, j);
        assertEquals(testMap, a.getSources());
    }

    @Test
    // 父节点存在，边不存在
    public void testAddSources2() {
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMap = new HashMap<>();

        int i = a.addSource(b.getName(), 2);
        testMap.put(b.getName(), 2);

        assertEquals(0, i);
        assertEquals(testMap, a.getSources());

        int j = a.addSource(c.getName(), 3);
        testMap.put(c.getName(), 3);

        assertEquals(0, j);
        assertEquals(testMap, a.getSources());
    }

    @Test
    // 父节点存在，边存在
    public void testAddSources3() {
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMapA = new HashMap<>();
        Map<String, Integer> testMapB = new HashMap<>();

        a.addSource(b.getName(), 1);
        b.addSource(c.getName(), 2);

        int i = a.addSource(b.getName(), 3);
        testMapA.put(b.getName(), 3);

        assertEquals(1, i);
        assertEquals(testMapA, a.getSources());

        int j = b.addSource(c.getName(), 4);
        testMapB.put(c.getName(), 4);

        assertEquals(2, j);
        assertEquals(testMapB, b.getSources());
    }

    /*
     * testAddTarget():
     * 按子节点是否存在划分：子节点存在，子节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     */
    @Test
    // 子节点不存在，边不存在
    public void testAddTarget1() {
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();

        int i = a.addTarget("b", 2);
        testMap.put("b", 2);

        assertEquals(0, i);
        assertEquals(testMap, a.getTargets());

        int j = a.addTarget("c", 3);
        testMap.put("c", 3);

        assertEquals(0, j);
        assertEquals(testMap, a.getTargets());
    }

    @Test
    // 子节点存在，边不存在
    public void testAddTarget2() {
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMap = new HashMap<>();

        int i = a.addTarget(b.getName(), 2);
        testMap.put(b.getName(), 2);

        assertEquals(0, i);
        assertEquals(testMap, a.getTargets());

        int j = a.addTarget(c.getName(), 3);
        testMap.put(c.getName(), 3);

        assertEquals(0, j);
        assertEquals(testMap, a.getTargets());
    }

    @Test
    // 子节点存在，边存在
    public void testAddTarget3() {
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMapA = new HashMap<>();
        Map<String, Integer> testMapB = new HashMap<>();

        a.addTarget(b.getName(), 1);
        b.addTarget(c.getName(), 2);

        int i = a.addTarget(b.getName(), 3);
        testMapA.put(b.getName(), 3);

        assertEquals(1, i);
        assertEquals(testMapA, a.getTargets());

        int j = b.addTarget(c.getName(), 4);
        testMapB.put(c.getName(), 4);

        assertEquals(2, j);
        assertEquals(testMapB, b.getTargets());
    }

    /*
     * testRemoveSources():
     * 按父节点是否存在划分：父节点存在，父节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     */
    @Test
    // 父节点不存在，边不存在
    public void testRemoveSources1(){
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();

        a.addSource("b",2);
        int i =a.removeSource("c");

        testMap.put("b",2);

        assertEquals(0,i);
        assertEquals(testMap, a.getSources());
    }

    @Test
    // 父节点存在，边不存在
    public void testRemoveSources2(){
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMap = new HashMap<>();

        a.addSource("b",2);
        int i =a.removeSource(c.getName());

        testMap.put("b",2);

        assertEquals(0,i);
        assertEquals(testMap, a.getSources());
    }

    @Test
    // 父节点存在，边存在
    public void testRemoveSources3(){
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMap = new HashMap<>();

        a.addSource("b",2);
        a.addSource("c",4);
        int i =a.removeSource(c.getName());

        testMap.put("b",2);

        assertEquals(4,i);
        assertEquals(testMap, a.getSources());
    }

    /*
     * testRemoveTargets():
     * 按子节点是否存在划分：子节点存在，子节点不存在
     * 按之前边是否存在划分：边存在，边不存在
     * 结合实际情况，共可分3个等价类
     */
    @Test
    // 子节点不存在，边不存在
    public void testRemoveTargets1(){
        Vertex<String> a = new Vertex<>("a");
        Map<String, Integer> testMap = new HashMap<>();

        a.addTarget("b",2);
        int i =a.removeTarget("c");

        testMap.put("b",2);

        assertEquals(0,i);
        assertEquals(testMap, a.getTargets());
    }

    @Test
    // 子节点存在，边不存在
    public void testRemoveTargets2(){
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMap = new HashMap<>();

        a.addTarget("b",2);
        int i =a.removeTarget(c.getName());

        testMap.put("b",2);

        assertEquals(0,i);
        assertEquals(testMap, a.getTargets());
    }

    @Test
    // 子节点存在，边存在
    public void testRemoveTargets3(){
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Map<String, Integer> testMap = new HashMap<>();

        a.addTarget("b",2);
        a.addTarget("c",4);
        int i =a.removeTarget(c.getName());

        testMap.put("b",2);

        assertEquals(4,i);
        assertEquals(testMap, a.getTargets());
    }
}
