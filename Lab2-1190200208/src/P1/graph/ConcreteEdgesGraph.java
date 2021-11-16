/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

//     Abstraction function:
//     AF(vertices) = Graph中的点
//     AF(edges) = Graph中的边
//     Representation invariant:
//     点的名字不能重复
//     所有点都在vertices中
//     edge的权值为正
//     两点间的单向边最多只能有一条
//     Safety from rep exposure:
//     成员变量vertices与edges均用private final修饰，防止其被外部修改
//     在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    // constructor
    public ConcreteEdgesGraph() {
    }

    // checkRep
    private void checkRep() {
        for (Edge<L> edge : edges) {
            assert edge.getWeight() > 0;
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
        }
    }

    @Override
    public boolean add(L vertex) {
        //throw new RuntimeException("not implemented");
        if (vertices.contains(vertex))
            return false;
        else {
            vertices.add(vertex);
            checkRep();
            return true;
        }
    }

    @Override
    public int set(L source, L target, int weight) {
        //throw new RuntimeException("not implemented");
        int oldWeight = 0;
        if (weight != 0) {
            vertices.add(source);
            vertices.add(target);
        }
        boolean flag = false; // 标志是否进行了边的删除操作，若否，说明不存在这样的边，需要进行后续操作
        // 在遍历时进行删除，需要显式使用迭代器，不然会产生严重错误！！！
        // 对现有边集进行遍历：考虑source与target原本就相连的情况
        Iterator<Edge<L>> it = edges.iterator();
        while (it.hasNext()) {
            Edge<L> temp = it.next();
            if (temp.getSource().equals(source) && temp.getTarget().equals(target)) {
                flag = true;
                oldWeight = temp.getWeight();
                it.remove();
                if (weight != 0) {
                    Edge<L> newEdge = new Edge<>(source, target, weight); // 因为Edge是不可变的，所以用这种方式修改边权
                    edges.add(newEdge);
                }
                break;
            }
        }
        if (!flag) { // 说明不存在source与target之间的边
            if (weight == 0) {
                return 0;
            } else {
                Edge<L> newEdge = new Edge<>(source, target, weight); // 因为Edge是不可变的，所以用这种方式修改边权
                edges.add(newEdge);
            }
        }
        checkRep();
        return oldWeight;
    }

    @Override
    public boolean remove(L vertex) {
        //throw new RuntimeException("not implemented");
        if (vertices.contains(vertex)) {
            vertices.remove(vertex);
            edges.removeIf(temp -> temp.getSource().equals(vertex) || temp.getTarget().equals(vertex));
            checkRep();
            return true;
        } else
            return false;
    }

    @Override
    public Set<L> vertices() {
        ///throw new RuntimeException("not implemented");
        return new HashSet<>(vertices); // 返回一个新的Hashset，防止暴露自身变量
    }

    @Override
    public Map<L, Integer> sources(L target) {
        //throw new RuntimeException("not implemented");
        Map<L, Integer> sources = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getTarget().equals(target)) {
                sources.put(edge.getSource(), edge.getWeight());
            }
        }
        checkRep();
        return sources;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        //throw new RuntimeException("not implemented");
        Map<L, Integer> targets = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source)) {
                targets.put(edge.getTarget(), edge.getWeight());
            }
        }
        checkRep();
        return targets;
    }

    // toString()
    @Override
    public String toString() {
        StringBuilder re = new StringBuilder();
        for (Edge<L> edge : edges) {
            re.append(edge.toString());
        }
        checkRep();
        return re.toString();
    }

}

/**
 * specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {

    // fields
    private final L source;
    private final L target;
    private final int weight;
    // Abstraction function:
    // AF(source) = 边的起点
    // AF(target) = 边的终点
    // AF(target) = 边的权值
    // Representation invariant:
    // source与target为图中的顶点
    // 边的权值大于0
    // Safety from rep exposure:
    // 成员变量均用private final修饰，防止其被外部变量修改

    // constructor
    Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    // checkRep
    public void checkRep() {
        assert this.weight > 0;
        assert this.source != null;
        assert this.target != null;
    }

    // methods
    public L getSource() {
        checkRep();
        return source;
    }

    public L getTarget() {
        checkRep();
        return target;
    }

    public int getWeight() {
        checkRep();
        return weight;
    }

    // toString()
    @Override
    public String toString() {
        String re = this.source + "->" + this.target + " (" + this.weight + ")" + '\n';
        checkRep();
        return re;
    }
}
