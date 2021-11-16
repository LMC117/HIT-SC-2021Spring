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
public class ConcreteVerticesGraph<L> implements Graph<L> {

    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    // AF(vertices) = vertices中点，以及连接点与点的边所构成的图;
    // Representation invariant:
    // 全部的点都保存在vertices中，vertices为空代表图不包含任何顶点
    // vertices中无重复的顶点
    // Safety from rep exposure:
    // vertices的关键字为 private final，保证其不会被外部修改
    // 在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    // constructor
    public ConcreteVerticesGraph() {
    }

    // checkRep
    public void checkRep() {
        Set<Vertex<L>> testSet = new HashSet<>(vertices);
        assert testSet.size() == vertices.size();
        for (Vertex<L> v : testSet) {
            assert vertices.contains(v);
        }
    }

    /**
     * Add a vertex to this graph.
     *
     * @param vertex label for the new vertex
     * @return true if this graph did not already include a vertex with the
     * given label; otherwise false (and this graph is not modified)
     */
    @Override
    public boolean add(L vertex) {
        //throw new RuntimeException("not implemented");
        for (Vertex<L> v : vertices) {
            if (v.getName().equals(vertex))
                return false;
        }
        Vertex<L> newVertex = new Vertex<>(vertex);
        vertices.add(newVertex);
        checkRep();
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        //throw new RuntimeException("not implemented");
        int oldWeight = 0;
        Vertex<L> s = null, t = null;
        if (weight == 0) {
            for (Vertex<L> v : vertices) {
                if (v.getName().equals(source)) {
                    s = v;
                }
                if (v.getName().equals(target)) {
                    t = v;
                }
            }
            if (s == null || t == null) // 有点不存在于图中，则必不存在连接它们的边，直接返回
            {
                checkRep();
                return 0;
            }
            if (s.getTargets().containsKey(t.getName())) {
               oldWeight = s.removeTarget(t.getName());
            }
            if (t.getSources().containsKey(s.getName())) {
               oldWeight = t.removeSource(s.getName());
            }
        } else {
            this.add(source);
            this.add(target);
            for (Vertex<L> v : vertices) {
                if (v.getName().equals(source)) {
                    s = v;
                }
                if (v.getName().equals(target)) {
                    t = v;
                }
            }
            if (s != null) {
                oldWeight = s.addTarget(target, weight);
            }
            if (t != null) {
                oldWeight = t.addSource(source, weight);
            }
        }
        checkRep();
        return oldWeight;
    }


    @Override
    public boolean remove(L vertex) {
        //throw new RuntimeException("not implemented");
        boolean flag = false; // vertex是否存在于图中
        Iterator<Vertex<L>> it = vertices.iterator();
        while (it.hasNext()) {
            Vertex<L> temp = it.next();
            if (temp.getName().equals(vertex)) {
                it.remove();
                flag = true;
            }
            // 如果temp的父节点、子节点中有vertex，则删除
            if (temp.getTargets().containsKey(vertex)) {
                temp.removeTarget(vertex);
            }
            if (temp.getSources().containsKey(vertex)) {
                temp.removeSource(vertex);
            }
        }
        checkRep();
        return flag;
    }

    @Override
    public Set<L> vertices() {
        //throw new RuntimeException("not implemented");
        Set<L> verticesSet = new HashSet<>();
        for (Vertex<L> v : vertices) {
            verticesSet.add(v.getName());
        }
        return verticesSet;
    }

    @Override
    public Map<L, Integer> sources(L target) {
        //throw new RuntimeException("not implemented");
        Vertex<L> v = null;
        for (Vertex<L> temp : vertices) {
            if (temp.getName().equals(target)) {
                v = temp;
                break;
            }
        }
        Map<L, Integer> sources;
        if (v != null) {
            sources = new HashMap<>(v.getSources());
        } else
            sources = new HashMap<>();
        checkRep();
        return sources;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        //throw new RuntimeException("not implemented");
        Vertex<L> v = null;
        for (Vertex<L> temp : vertices) {
            if (temp.getName().equals(source)) {
                v = temp;
                break;
            }
        }
        Map<L, Integer> targets;
        if (v != null) {
            targets = new HashMap<>(v.getTargets());
        } else
            targets = new HashMap<>();
        checkRep();
        return targets;
    }

    // toString()
    @Override
    public String toString() {
        StringBuilder re = new StringBuilder();
        for (Vertex<L> v : vertices) {
            re.append(v.toString());
        }
        return re.toString();
    }

}

/**
 * specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {

    // fields
    private final L name; // 保存该点的名字
    private Map<L, Integer> sources = new HashMap<>(); // 保存该点的父节点
    private Map<L, Integer> targets = new HashMap<>(); // 保存该点的子节点
    // Abstraction function:
    // AF(name) = 顶点的名字
    // AF(sources) = 指向该点的所有点，以及连接这两点的边的权重
    // AF(targets) = 该点指向的所有点，以及连接这两点的边的权重
    // Representation invariant:
    // 每条边的权值都大于0
    // Vertex不能重名
    // Safety from rep exposure:
    // 成员变量均用private关键字修饰

    // constructor
    public Vertex(L name) {
        this.name = name;
        this.sources = new HashMap<>();
        this.targets = new HashMap<>();
    }

    // checkRep
    public void checkRep() {
        Set<L> testSources = sources.keySet();
        Set<L> testTargets = targets.keySet();
        if (testSources.size() != 0) {
            for (L source : testSources) {
                int value = sources.get(source);
                assert value > 0;
            }
        }
        if (testTargets.size() != 0) {
            for (L target : testTargets) {
                int value = targets.get(target);
                assert value > 0;
            }
        }
    }

    // methods
    public L getName() {
        return name;
    }

    public Map<L, Integer> getSources() {
        return sources;
    }

    public Map<L, Integer> getTargets() {
        return targets;
    }

    /**
     * 为当前点添加父节点
     * 若父节点已存在，则更新权值；若父节点不存在，为当前点加上一个父节点以及边
     * 若weight为0，则移除父节点
     *
     * @param source 待加入的父节点
     * @param weight 连接父节点与该点的边权值
     * @return 返回之前的权值。若之前不存在这样的边，则返回0。
     */
    public int addSource(L source, int weight) {
        int oldWeight = 0;
        boolean flag = false; // 标记之前是否存在父节点，若不存在，为false
        for (Map.Entry<L, Integer> entry : sources.entrySet()) {
            if (entry.getKey().equals(source)) {
                flag = true;
                oldWeight = entry.getValue();
                entry.setValue(weight);
                break;
            }
        }
        if (!flag) //之前不存在父节点
        {
            sources.put(source, weight);
        }
        checkRep();
        return oldWeight;
    }

    /**
     * 删除特定父节点，并返回之前的权值
     *
     * @param source 待删除的父节点
     * @return 若成功删除，返回之前的权值；否则若这样的边不存在，则返回0
     */
    public int removeSource(L source) {
        Integer oldWeight = sources.remove(source);
        checkRep();
        return Objects.requireNonNullElse(oldWeight, 0);
    }

    /**
     * 为当前点添加子节点
     * 若子节点已存在，则更新权值；若子节点不存在，为当前点加上该子节点以及边
     * 若weight为0，则移除子节点
     *
     * @param target 待加入的子节点
     * @param weight 连接当前点与子节点的边权值
     * @return 返回更新前的权值，若之前这样的边不存在，则返回0
     */
    public int addTarget(L target, int weight) {
        int oldWeight = 0;
        boolean flag = false; // 标记之前是否存在子节点，若不存在，为false
        for (Map.Entry<L, Integer> entry : targets.entrySet()) {
            if (entry.getKey().equals(target)) {
                flag = true;
                oldWeight = entry.getValue();
                entry.setValue(weight);
                break;
            }
        }
        if (!flag) //之前不存在父节点
        {
            targets.put(target, weight);
        }
        checkRep();
        return oldWeight;
    }

    /**
     * 删除特定子节点，并返回之前的权值
     *
     * @param target 待删除的子节点
     * @return 若成功删除，返回之前的权值；否则若这样的边不存在，则返回0
     */
    public int removeTarget(L target) {
        Integer oldWeight = targets.remove(target);
        checkRep();
        return Objects.requireNonNullElse(oldWeight, 0);
    }


    // toString()

    /**
     * 将该点的相关信息以字符串形式输出
     *
     * @return 以字符串形式表示的当前点的相关信息
     */
    @Override
    public String toString() {
        String l;
        l = "当前点: " + this.name.toString() + " 当前点的父节点及权值: " + this.sources.toString() +
                " 当前点的子节点及权值: " + this.targets.toString() + '\n';
        return l;
    }
}
