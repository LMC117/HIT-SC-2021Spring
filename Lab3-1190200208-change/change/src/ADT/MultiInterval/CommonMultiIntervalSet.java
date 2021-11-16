package ADT.MultiInterval;

import ADT.Interval.IntervalSet;

import java.util.*;

public class CommonMultiIntervalSet<L> implements MultiIntervalSet<L> {
    private final List<IntervalSet<L>> multi = new ArrayList<>();
    private final Set<L> labels = new HashSet<>();

    //Abstraction function:
    //  AF(multi) = 各标签对应的时间段
    //  AF(labels) = MultiIntervalSet中的所有标签
    //Representation invariant:
    //  时间段有且仅有一个起点，一个终点
    //  时间段起点对应时刻必须小于等于终点
    //  时间段起点，终点必须不小于0
    //Safety from rep exposure:
    //  在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    // constructor
    public CommonMultiIntervalSet() {
    }

    public CommonMultiIntervalSet(IntervalSet<L> initial) {
        this.multi.add(initial);
    }

    // checkRep
    private void checkRep() {
        for (IntervalSet<L> intervalSet : multi) {
            for (L label : labels) {
                long start = intervalSet.start(label);
                long end = intervalSet.end(label);
                assert start <= end;
            }
        }
    }

    /**
     * 判断该MultiIntervalSet是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    @Override
    public boolean isEmpty() {
        return multi.isEmpty() && labels.isEmpty();
    }

    /**
     * 在当前的multiIntervalSet中插入新的时间段和标签
     *
     * @param start 时间段的起点
     * @param end   时间段的终点
     * @param label 时间段的标签
     */
    @Override
    public void insert(long start, long end, L label) {
        boolean flag = false; // 标志是否加入了现有的某个intervalSet

        for (IntervalSet<L> intervalSet : multi) {
            if (!intervalSet.labels().contains(label)) { // 如果当前intervalSet不包含label
                intervalSet.insert(start, end, label);
                this.labels.add(label);
                flag = true;
                break;
            }
        }

        if (!flag) { // 没有加入现有的intervalSet，说明现有set已经含有label对应时间段，故在multi中增加一个新的intervalSet
            IntervalSet<L> temp = IntervalSet.empty();
            temp.insert(start, end, label);
            this.labels.add(label);
            multi.add(temp);
        }

        checkRep();
    }

    /**
     * 获得当前对象中的标签集合
     *
     * @return 当前对象中的标签集合
     */
    @Override
    public Set<L> labels() {
        return new HashSet<>(labels); // 返回一个新的Hashset，防止暴露自身变量
    }

    /**
     * 从当前MultiIntervalSet中移除某个标签所关联的所有时间段
     *
     * @param label 所要移除时间段的对应标签
     * @return 移除成功返回true，移除失败返回false
     */
    @Override
    public boolean remove(L label) {
        boolean flag = false; // 表示是否进行了移除

        Iterator<IntervalSet<L>> it = multi.iterator();
        while (it.hasNext()) {
            IntervalSet<L> intervalSet = it.next();
            if (intervalSet.labels().contains(label)) {
                flag = true;
                intervalSet.remove(label);
                if (intervalSet.isEmpty()) { // 如果一个intervalSet已空，将其移除，以维护较少的空间
                    it.remove();
                }
            }
        }
        if (flag) labels.remove(label);
        checkRep();

        return flag;
    }

    /**
     * 从当前对象中获取与某个标签所关联的所有时间段
     *
     * @param label 所要获取具体信息的时间段的对应标签
     * @return 返回IntervalSet<Integer>，其中的时间段按开始时间从小到大的次序排列
     */
    @Override
    public IntervalSet<Integer> intervals(L label) {
        if (this.isEmpty())
            return null;

        List<List<Long>> map = new ArrayList<>();
        IntervalSet<Integer> temp = IntervalSet.empty();

        for (IntervalSet<L> intervalSet : multi) { // 将对应label的intervalSet加入map
            if (intervalSet.labels().contains(label)) {
                List<Long> subList = new ArrayList<>();
                subList.add((intervalSet.start(label)));
                subList.add((intervalSet.end(label)));
                map.add(subList);
            }
        }

        // 将该label对应的intervalSet按从小到大的次序加入list
        int i = 0;
        long min = Long.MAX_VALUE;
        while (!map.isEmpty()) {
            for (List<Long> entry : map) { // 找到当前map中start的最小值
                if (entry.get(0) < min)
                    min = entry.get(0);
            }

            Iterator<List<Long>> it = map.iterator();
            while (it.hasNext()) {
                List<Long> entry = it.next();
                if (entry.get(0) == min) {
                    temp.insert(entry.get(0), entry.get(1), i);
                    i++;
                    it.remove();
                    break;
                }
            }
            min = Long.MAX_VALUE;
        }

        checkRep();
        return temp;
    }

    @Override
    public boolean checkBlank() {
        return new NoBlankMultiIntervalSet<>(this).checkBlank();
    }

    @Override
    public boolean checkOverlap() {
        return new NonOverlapMultiIntervalSet<>(this).checkOverlap();
    }

    @Override
    public boolean checkPeriodic() {
        return new NonPeriodicMultiIntervalSet<>(this).checkPeriodic();
    }

    /**
     * 将MultiIntervalSet的信息以字符串形式输出
     *
     * @return 展示MultiIntervalSet的具体信息
     */
    @Override
    public String toString() {
        int size = multi.size();
        if (size == 0) return "empty multiIntervalSet";
        else {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < size; i++) {
                s.append("IntervalSet ").append(i).append(":\n");
                IntervalSet<L> temp = multi.get(i);
                Set<L> labelSet = temp.labels();
                for (L label : labelSet) {
                    long start = temp.start(label);
                    long end = temp.end(label);
                    s.append("标签：").append(label).append("; ").append("时间段：[")
                            .append(start).append(",").append(end).append("]\n");
                }
            }
            return s.toString();
        }
    }

    /**
     * 从当前MultiIntervalSet中移除某个标签所关联的特定时间段
     *
     * @param label 所要移除时间段的对应标签
     * @param start 所要移除时间段的起点
     * @return 移除成功返回true，移除失败返回false
     */
    @Override
    public boolean removeSpecific(L label, long start) {
        boolean flag = false; // 表示是否进行了移除

        Iterator<IntervalSet<L>> it = multi.iterator();
        while (it.hasNext()) {
            IntervalSet<L> intervalSet = it.next();
            if (intervalSet.labels().contains(label)) {
                if (intervalSet.start(label) == start) {
                    flag = true;
                    intervalSet.remove(label);
                    if (intervalSet.isEmpty()) { // 如果一个intervalSet已空，将其移除，以维护较少的空间
                        it.remove();
                    }
                }
            }
        }

        // 检查移除该特定时间段后，是否还存在其他该标签对应的时间段，若不存在，则将其从labels中移去
        boolean checkLabelExist = false;
        for (IntervalSet<L> intervalSet : multi) {
            if (intervalSet.labels().contains(label))
                checkLabelExist = true;
        }
        if (!checkLabelExist) labels.remove(label);

        checkRep();

        return flag;
    }
}
