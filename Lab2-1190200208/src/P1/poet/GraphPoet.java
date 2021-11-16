/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 *
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 *
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 *
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 *
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 *
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {

    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    // AF(graph) = 语料中所有单词构成的图
    // Representation invariant:
    // 加入语料后，图不能为null
    // Safety from rep exposure:
    // 用private修饰graph，保证其不能被外界直接修改

    /**
     * Create a new poet with the graph from corpus (as described above).
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        //throw new RuntimeException("not implemented");
        BufferedReader br = new BufferedReader(new FileReader(corpus));
        String line;
        List<String> lineWords = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] OriLineWords = line.split(" ");
            for (String word : OriLineWords) { // 进行处理，删掉每个单词中字母以外的字符，且将大写转为小写
                lineWords.add(word.replaceAll("[^a-zA-Z]+", "").toLowerCase());
            }
        }
        br.close();
        for (int i = 0; i < lineWords.size() - 1; i++) {
            // 检查前后单词是否已经相连，若是，则边权++，若否，则增加边
            String source = lineWords.get(i);
            String target = lineWords.get(i + 1);
            if (graph.targets(source).containsKey(target)) { //前后单词已经相连
                int oldWeight = graph.targets(source).get(target);
                graph.set(source, target, oldWeight + 1);
            } else {
                graph.set(source, target, 1);
            }
        }
        checkRep();
    }

    // checkRep
    public void checkRep() {
        assert !graph.equals(Graph.empty());
    }

    /**
     * Generate a poem.
     *
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        //throw new RuntimeException("not implemented");
        StringBuilder s = new StringBuilder();
        String[] separateInput = input.split(" ");
        String oriFront, oriRear = "", front, rear;
        List<String> wordList = new ArrayList<>(Arrays.asList(separateInput)); // 保存语句中的单个单词，注意，这里保留了单词的本来形式，具体的删除符号等操作在检索中进行
        for (int i = 0; i < wordList.size() - 1; i++) { // 一次对前后两个单词在图中进行检索
            oriFront = wordList.get(i); // front保存前一个单词
            oriRear = wordList.get(i + 1); // rear保存后一个单词
            front = oriFront.replaceAll("[^a-zA-Z]+", "").toLowerCase(); // 对原始单词进行处理：删掉符号，大写全部转小写
            rear = oriRear.replaceAll("[^a-zA-Z]+", "").toLowerCase(); // 对原始单词进行处理：删掉符号，大写全部转小写
            s.append(oriFront).append(" ");
            if (graph.vertices().contains(front)) {
                Map<String, Integer> w1_b = graph.targets(front); // 保存w1->b的可能值
                Set<String> bSet = w1_b.keySet(); // 保存所有可能的b值
                Map<String, Integer> w1_b_w2 = new HashMap<>(); // 保存w1->b->w2的可能值，其中，键为b对应字符串，值为总权值
                for (String tempB : bSet) {
                    if (graph.targets(tempB).containsKey(rear)) { // 对于当前b值，存在b->w2的边
                        Map<String, Integer> b_w2 = graph.targets(tempB); // 保存b->w2的可能值
                        w1_b_w2.put(tempB, w1_b.get(tempB) + b_w2.get(rear));
                    }
                }
                if (!w1_b_w2.isEmpty()) { // 若存在这样的b
                    String b = "";
                    int maxWeight = 0;
                    // 寻找权重最高的b与对应的权重
                    for (Map.Entry<String, Integer> entry : w1_b_w2.entrySet()) {
                        if (entry.getValue() > maxWeight) {
                            b = entry.getKey();
                            maxWeight = entry.getValue();
                        }
                    }
                    s.append(b).append(" ");
                }
            }
        }
        s.append(oriRear);
        return s.toString();
    }

    // toString()
    @Override
    public String toString() {
        return graph.toString();
    }
}
