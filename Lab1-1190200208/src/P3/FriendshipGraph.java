package P3;

import java.util.*;

/**
 * An undirected graph, in which nodes represent Persons
 * and edges represent relationships between Persons
 */
public class FriendshipGraph {
    public List<Person> persons = new ArrayList<Person>(); // Graph中的节点列表
    private final Set<String> names = new HashSet<String>(); // 保存名字，用于判断是否重复
    public Vector<Vector<Integer>> relationships = new Vector<Vector<Integer>>(); // 嵌套的Vector，保存关系矩阵


    /**
     * Add a new Vertex to the graph.
     *
     * @param p Person to be added to the graph
     * @return If the vertex is successfully added,return true; else return false
     */
    public boolean addVertex(Person p) {
        if (names.contains(p.getName())) { // 通过HashSet来判断人名是否重复
            System.out.println("此人已存在！");
            return false;
        } else {
            Vector<Integer> singlePerson = new Vector<Integer>();
            Vector<Integer> temp = new Vector<Integer>();
            p.setIndex(persons.size());
            persons.add(p);
            names.add(p.getName());
            relationships.add(singlePerson);
            for (int i = 0; i < p.getIndex(); i++) {
                temp = relationships.elementAt(i);
                temp.add(0);
            }
            temp = relationships.elementAt(p.getIndex());
            for (int i = 0; i <= p.getIndex(); i++) {
                temp.add(0);
            }
            return true;
        }
    }

    /**
     * Add a new edge from p1 to p2.
     *
     * @param p1 The starting vertex of the new edge
     * @param p2 The ending vertex of the new edge
     * @return If p1 or p2 isn't in the graph, return false; else, return true;
     */
    public boolean addEdge(Person p1, Person p2) {
        if (p1.getIndex() == -1) {
            System.out.println(p1.getName() + "不在关系图中");
            return false;
        }
        if (p2.getIndex() == -1) {
            System.out.println(p2.getName() + "不在关系图中");
            return false;
        }
        Vector<Integer> temp = relationships.elementAt(p1.getIndex());
        temp.set(p2.getIndex(), 1);
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
        // 异常情况处理
        // p1与p2有其一不在关系图中
        if (p1.getIndex() == -1) {
            System.out.print(p1.getName() + "不在关系图中");
            return -1;
        }
        if (p2.getIndex() == -1) {
            System.out.print(p2.getName() + "不在关系图中");
            return -1;
        }
        // p1与p2相等
        if (p1.getIndex() == p2.getIndex()) {
            return 0;
        }
        Queue<Person> queue = new LinkedList<Person>(); // 队列，用于BFS搜素
        int distance = 0;
        Person temp = new Person("");
        Person queueEnd = new Person("");
        Vector<Integer> tempCol = new Vector<Integer>();
        // visit数组（visit为标志是否访问过的数组,访问过为1，否则为0）
        int[] visit = new int[persons.size()];
        // isQueueEnd标志节点i是否是某轮bfs深搜的终点，若是，其为true，,需要使distance++
        boolean[] isQueueEnd = new boolean[persons.size()];


        // 初始化，对p1进行设定
        queue.add(p1);
        visit[p1.getIndex()] = 1;
        isQueueEnd[p1.getIndex()]=true;

        while (queue.peek() != p2) {
            temp = queue.poll(); // 弹出并保存queue的头元素
            // 将与queue头元素直接相连，且未访问过的元素入队
            tempCol = relationships.get(temp.getIndex()); // tempCol保存头元素对应的关系矩阵行
            for (int i = 0; i < tempCol.size(); i++) { // 头元素对应的关系矩阵行，遍历此行中的所有元素，找到与头元素直接相邻的元素
                if (tempCol.get(i) == 1) {
                    // 查找index为i的person，并将其加入队列,同时把其标记为访问过
                    for (Person t : persons) {
                        if (t.getIndex() == i && visit[i] == 0) {
                            queue.add(t);
                            visit[i] = 1;
                            queueEnd = t; // 记录当前队尾
                            break;
                        }
                    }
                }
            }

            // 最后队列空，说明没有p1到p2的直接通路
            if (queue.isEmpty())
                return -1;

            // 记录当前队尾，并使distance++
            if (isQueueEnd[temp.getIndex()]) {
                isQueueEnd[queueEnd.getIndex()]=true;
                distance++;
            }
        }
        return distance;
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
