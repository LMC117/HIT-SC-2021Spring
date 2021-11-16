/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * <p>
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * <p>
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<>();
    }

    /*
     * Testing ConcreteEdgesGraph...
     */

    // Testing strategy for ConcreteEdgesGraph.toString()
    // 对三个点的情况进行测试，观察toString方法是否出错

    // tests for ConcreteEdgesGraph.toString()
    @Test
    public void testToString(){
        Graph<String> graph =emptyInstance();
        graph.set("a", "b", 1);
        graph.set("b", "c", 2);
        graph.set("a", "c", 3);

        assertEquals("a->b (1)\n"+"b->c (2)\n"+"a->c (3)\n",graph.toString());
    }

    /*
     * Testing Edge...
     */

    // Testing strategy for Edge
    // 测试Edge中的三个不同方法：获取原点，获取终点，获取边权

    // tests for operations of Edge

    /* Testing strategy
     * 测试原点返回值
     */
    @Test
    public void testGetSource() {
        Edge<String> e = new Edge<>("a", "b", 1);
        assertEquals("a", e.getSource());
    }

    /* Testing strategy
     * 测试终点返回值
     */
    @Test
    public void testGetTarget() {
        Edge<String> e = new Edge<>("a", "b", 1);
        assertEquals("b", e.getTarget());
    }

    /* Testing strategy
     * 测试能否返回正确权重
     */
    @Test
    public void testGetWeight() {
        Edge<String> e = new Edge<>("a", "b", 1);
        assertEquals(1, e.getWeight());
    }
}
