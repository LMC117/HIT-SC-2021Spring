package P2;

import P1.graph.ConcreteEdgesGraph;

import java.util.*;

public class FriendshipGraph extends ConcreteEdgesGraph<Person> {
    private final List<Person> visited = new ArrayList<>(); // 标志是否访问过的列表

    // Abstraction function:
    // AF(socialNetwork) = 人员组成的社交网络图
    // Representation invariant:
    // Graph中的Person不能重复
    // Safety from rep exposure:
    // 用private final修饰socialNetwork

    // constructor
    public FriendshipGraph() {
    }

    @Override
    public Set<Person> vertices() {
        return super.vertices();
    }

    @Override
    public Map<Person, Integer> sources(Person target) {
        return super.sources(target);
    }

    @Override
    public Map<Person, Integer> targets(Person source) {
        return super.targets(source);
    }

    /**
     * Add a new Vertex to the graph.
     *
     * @param p Person to be added to the graph
     * @return If the vertex is successfully added,return true; else return false
     */
    public boolean addVertex(Person p) {
        return super.add(p);
    }

    /**
     * Add a new edge from p1 to p2.
     *
     * @param p1 The starting vertex of the new edge
     * @param p2 The ending vertex of the new edge
     * @return If p1 or p2 isn't in the graph, return false; else, return true;
     */
    public boolean addEdge(Person p1, Person p2) {
        if (p1.equals(p2))
            return false;
        if (!super.vertices().contains(p1) || !super.vertices().contains(p2))
            return false;
        super.set(p1, p2, 1);
        return true;
    }

    /**
     * Add a new edge from p1 to p2.
     *
     * @param p1 The starting vertex of the new edge
     * @param p2 The ending vertex of the new edge
     * @return The shortest distance between p1 and p2. If there are no any paths between them, return -1.
     */
    public int getDistance(Person p1, Person p2) {
        // 检查特殊情况
        if (!super.vertices().contains(p1) || !super.vertices().contains(p2))
            return -1;
        if (p1.equals(p2))
            return 0;

        int min = super.vertices().size() + 1;
        visited.add(p1);
        Set<Person> son = super.targets(p1).keySet();
        for (Person t : son) {
            if (visited.contains(t)) continue;
            int temp = getDistance(t, p2);
            if (temp != -1 && temp < min) {
                min = temp;
            }
        }
        visited.remove(p1);
        return min > super.vertices().size() ? -1 : min + 1;
    }

    public static void main(String[] args) {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        System.out.println(graph.getDistance(rachel, ross));
        //should print 1
        System.out.println(graph.getDistance(rachel, ben));
        //should print 2
        System.out.println(graph.getDistance(rachel, rachel));
        //should print 0
        System.out.println(graph.getDistance(rachel, kramer));
        //should print -1
    }
}
