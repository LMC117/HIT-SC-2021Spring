package P3;

import P2.turtle.TurtleSoup;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class FriendshipGraphTest {
    /**
     * Tests that assertions are enabled.
     */
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    /**
     * Tests addVertex.
     */
    @Test
    public void addVertexTest() {
        FriendshipGraph graph = new FriendshipGraph();
        List<Person> correctPersons = new ArrayList<Person>();
        Vector<Vector<Integer>> relationships = new Vector<Vector<Integer>>();
        Vector<Integer> a = new Vector<Integer>();
        a.add(0);
        a.add(0);
        a.add(0);

        Person Alice = new Person("Alice");

        Person Bob = new Person("Bob");

        Person Bob_new = new Person("Bob");

        Person Clark = new Person("Clark");

        assertEquals(correctPersons, graph.persons);

        correctPersons.add(Alice);
        graph.addVertex(Alice);
        assertEquals(correctPersons, graph.persons);

        correctPersons.add(Bob);
        graph.addVertex(Bob);
        assertEquals(correctPersons, graph.persons);

        graph.addVertex(Bob_new);
        assertEquals(correctPersons, graph.persons);

        correctPersons.add(Clark);
        graph.addVertex(Clark);
        assertEquals(correctPersons, graph.persons);

        relationships.add(a);
        relationships.add(a);
        relationships.add(a);
        assertEquals(relationships, graph.relationships);
    }

    /**
     * Tests addEdge.
     */
    @Test
    public void addEdgeTest() {
        FriendshipGraph graph = new FriendshipGraph();
        Vector<Vector<Integer>> relationships = new Vector<Vector<Integer>>();
        Vector<Integer> a = new Vector<Integer>();
        Vector<Integer> b = new Vector<Integer>();
        Vector<Integer> c = new Vector<Integer>();
        Vector<Integer> d = new Vector<Integer>();
        Vector<Integer> temp = new Vector<Integer>();

        Person Alice = new Person("Alice");

        Person Bob = new Person("Bob");

        Person Clark = new Person("Clark");

        Person Drake = new Person("Drake");


        graph.addVertex(Alice);
        graph.addVertex(Bob);
        graph.addVertex(Clark);
        for (int i = 0; i < 3; i++) {
            a.add(0);
            b.add(0);
            c.add(0);
            d.add(0);
        }
        relationships.add(a);
        relationships.add(b);
        relationships.add(c);
        assertEquals(relationships, graph.relationships);


        graph.addEdge(Alice, Bob);
        graph.addEdge(Bob, Alice);
        temp = relationships.get(0);
        temp.set(1, 1);
        temp = relationships.get(1);
        temp.set(0, 1);
        assertEquals(relationships, graph.relationships);


        graph.addVertex(Drake);
        graph.addEdge(Drake, Bob);
        graph.addEdge(Bob, Drake);
        for (int i = 0; i < Drake.getIndex(); i++) {
            temp = relationships.get(i);
            temp.add(0);
        }
        d.add(0);
        relationships.add(d);
        temp = relationships.get(1);
        temp.set(3, 1);
        temp = relationships.get(3);
        temp.set(1, 1);
        assertEquals(relationships, graph.relationships);
    }

    /**
     * Tests getDistance.
     */
    @Test
    public void getDistanceTest() {
        FriendshipGraph graph = new FriendshipGraph();

        Person p1 = new Person("p1");

        Person p2 = new Person("p2");

        Person p3 = new Person("p3");

        Person p4 = new Person("p4");

        Person p5 = new Person("p5");

        Person p6 = new Person("p6");

        Person p7 = new Person("p7");

        Person p8 = new Person("p8");

        Person p9 = new Person("p9");

        Person p10 = new Person("p10");

        Person p11 = new Person("p11");

        graph.addVertex(p1);
        graph.addVertex(p2);
        graph.addVertex(p3);
        graph.addVertex(p4);
        graph.addVertex(p5);
        graph.addVertex(p6);
        graph.addVertex(p7);
        graph.addVertex(p8);
        graph.addVertex(p9);
        graph.addVertex(p10);

        graph.addEdge(p1, p2);
        graph.addEdge(p2, p1);

        graph.addEdge(p1, p3);
        graph.addEdge(p3, p1);

        graph.addEdge(p1, p4);
        graph.addEdge(p4, p1);

        graph.addEdge(p2, p5);
        graph.addEdge(p5, p2);

        graph.addEdge(p4, p6);
        graph.addEdge(p6, p4);

        graph.addEdge(p4, p7);
        graph.addEdge(p7, p4);

        graph.addEdge(p6, p7);
        graph.addEdge(p7, p6);

        graph.addEdge(p8, p9);
        graph.addEdge(p9, p8);

        // 正常情况
        assertEquals(1, graph.getDistance(p1, p4));
        assertEquals(2, graph.getDistance(p1, p5));
        assertEquals(3, graph.getDistance(p3, p6));
        assertEquals(4, graph.getDistance(p5, p7));
        assertEquals(1, graph.getDistance(p8, p9));

        assertEquals(-1, graph.getDistance(p1, p8));
        assertEquals(-1, graph.getDistance(p10, p1));
        // 输入两者为同一点
        assertEquals(0, graph.getDistance(p1, p1));
        assertEquals(0, graph.getDistance(p10, p10));
        // 有一点不在图中
        assertEquals(-1, graph.getDistance(p1, p11));
    }
}