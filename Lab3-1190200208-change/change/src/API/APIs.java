package API;

import ADT.Interval.IntervalSet;
import ADT.MultiInterval.MultiIntervalSet;

import java.util.*;

public class APIs<L> {

    /**
     * 计算两个 MultiIntervalSet 对象的相似度
     * <p>
     * 思路：
     * 1.找到s1，s2的共有标签
     * 2.找到时间轴的起始值min和结束值max
     * 3.对每个共有标签，寻找其在s1中对应的时间段，遍历s2，寻找其与s2中重合的长度，并保存该长度
     * 4.循环完所有的共有标签，得到最终的相似度
     *
     * @param s1 传入的第一个MultiIntervalSet
     * @param s2 传入的第二个MultiIntervalSet
     * @return 计算得到的相似度
     */
    public double Similarity(MultiIntervalSet<L> s1, MultiIntervalSet<L> s2) {
        Set<L> s1FullLabel = s1.labels();
        Set<L> s2FullLabel = s2.labels();
        Set<L> labels = new HashSet<>();

        // 找到s1，s2中的共有标签
        for (L label : s1FullLabel)
            if (s2FullLabel.contains(label))
                labels.add(label);

        // 找到时间轴的起始值min和结束值max
        List<MultiIntervalSet<L>> list = new ArrayList<>(); // 保存s1与s2，方便遍历
        list.add(s1);
        list.add(s2);
        Iterator<L> it1 = s1.labels().iterator(); // 方便遍历标签集合
        Iterator<L> it2 = s2.labels().iterator();

        long min1 = Long.MAX_VALUE, min2 = Long.MAX_VALUE;
        long max1 = Long.MIN_VALUE, max2 = Long.MIN_VALUE;

        while (it1.hasNext()) { // 寻找s1的min,max
            L label = it1.next();
            IntervalSet<Integer> temp = s1.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                min1 = Math.min(temp.start(tempLabel), min1);
                max1 = Math.max(temp.end(tempLabel), max1);
            }
        }

        while (it2.hasNext()) { // 寻找s2的min,max
            L label = it2.next();
            IntervalSet<Integer> temp = s2.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                min2 = Math.min(temp.start(tempLabel), min2);
                max2 = Math.max(temp.end(tempLabel), max2);
            }
        }

        long min = Math.min(min1, min2);
        long max = Math.max(max1, max2);
        long length = max - min;

        // 对每个共有标签，寻找其在s1中对应的时间段，遍历s2中同一标签的时间段，寻找其与s2中重合的长度，并将该长度累加至返回值
        double similarity = 0;

        for (L label : labels) {
            IntervalSet<Integer> s1Interval = s1.intervals(label); // 保存当前共有标签在s1中的时间段

            for (int i : s1Interval.labels()) { // 获取当前共有标签在s1中的某一个时间段[baseStart,baseEnd]

                long baseStart = s1Interval.start(i);
                long baseEnd = s1Interval.end(i);
                // 在s2中，选取同一标签时间段进行比较
                IntervalSet<Integer> s2Interval = s2.intervals(label); // 保存当前共有标签在s2中的时间段

                for (int j : s1Interval.labels()) { // 获取当前共有标签在s2中的某一个时间段[baseStart,baseEnd]
                    long compareStart = s2Interval.start(j);
                    long compareEnd = s2Interval.end(j);
                    if (compareStart > baseEnd || compareEnd < baseStart)
                        continue;
                    long accStart = Math.max(baseStart, compareStart);
                    long accEnd = Math.min(baseEnd, compareEnd);
                    similarity += ((float) accEnd - accStart) / length;
                }
            }
        }
        return similarity;
    }

    /**
     * 发现一个 IntervalSet<L>对象中的时间冲突比例
     *
     * @param set 传入的IntervalSet<L>
     * @return 计算得到的冲突比例
     */
    public double calcConflictRatio(IntervalSet<L> set) {
        Iterator<L> it = set.labels().iterator(); // 方便遍历标签集合
        List<List<Long>> list = new ArrayList<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long conflict = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            L label = it.next();
            List<Long> subList = new ArrayList<>();
            subList.add(set.start(label));
            subList.add(set.end(label));
            list.add(subList);
            min = Math.min(set.start(label), min);
            max = Math.max(set.end(label), max);
        }

        for (long i = min; i <= max; i++) {
            boolean flag1 = false, flag2 = false;
            for (List<Long> entry : list) {
                if (i >= entry.get(0) && i <= entry.get(1)) {
                    if (!flag1)
                        flag1 = true;
                    else {
                        flag2 = true;
                        break;
                    }
                }
            }
            if (flag1 && flag2) conflict++;
        }
        return (double) conflict / (max - min + 1);
    }

    /**
     * 发现一个MultiIntervalSet<L>对象中的时间冲突比例
     *
     * @param set 传入的MultiIntervalSet<L>
     * @return 计算得到的冲突比例
     */
    public double calcConflictRatio(MultiIntervalSet<L> set) {
        Iterator<L> it = set.labels().iterator(); // 方便遍历标签集合
        List<List<Long>> list = new ArrayList<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            L label = it.next();
            IntervalSet<Integer> temp = set.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                List<Long> subList = new ArrayList<>();
                subList.add(temp.start(tempLabel));
                subList.add(temp.end(tempLabel));
                list.add(subList);
                min = Math.min(temp.start(tempLabel), min);
                max = Math.max(temp.end(tempLabel), max);
            }
        }

        // 从min到max，步长为1遍历每个键值对
        long conflict = 0;

        for (long i = min; i <= max; i++) {
            boolean flag1 = false, flag2 = false;
            for (List<Long> entry : list) {
                if (i >= entry.get(0) && i <= entry.get(1)) {
                    if (!flag1)
                        flag1 = true;
                    else {
                        flag2 = true;
                        break;
                    }
                }
            }
            if (flag1 && flag2) conflict++;
        }
        return (double) conflict / (max - min + 1);
    }

    /**
     * 计算一个 IntervalSet<L> 对象中的空闲时间比例
     *
     * @param set 传入的 IntervalSet<L>
     * @return 计算得到的空闲时间
     */
    public double calcFreeTimeRatio(IntervalSet<L> set) {
        long blank = 0;
        Iterator<L> it = set.labels().iterator(); // 方便遍历标签集合
        List<List<Long>> list = new ArrayList<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            L label = it.next();
            List<Long> subList = new ArrayList<>();
            subList.add(set.start(label));
            subList.add(set.end(label));
            list.add(subList);
            min = Math.min(set.start(label), min);
            max = Math.max(set.end(label), max);
        }

        for (long i = min; i <= max; i++) {
            boolean flag = false;
            for (List<Long> entry : list) {
                if (i >= entry.get(0) && i <= entry.get(1)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) blank++;
        }
        return (double) blank / (max - min + 1);
    }

    /**
     * 计算一个 MultiIntervalSet<L> 对象中的空闲时间比例
     *
     * @param set 传入的 MultiIntervalSet<L>
     * @return 计算得到的空闲时间
     */
    public double calcFreeTimeRatio(MultiIntervalSet<L> set) {
        long blank = 0;
        Iterator<L> it = set.labels().iterator(); // 方便遍历标签集合
        List<List<Long>> list = new ArrayList<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            L label = it.next();
            IntervalSet<Integer> temp = set.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                List<Long> subList = new ArrayList<>();
                subList.add(temp.start(tempLabel));
                subList.add(temp.end(tempLabel));
                list.add(subList);
                min = Math.min(temp.start(tempLabel), min);
                max = Math.max(temp.end(tempLabel), max);
            }
        }

        //从min到max，步长为1遍历每个键值对
        for (long i = min; i <= max; i++) {
            boolean flag = false;
            for (List<Long> entry : list) {
                if (i >= entry.get(0) && i <= entry.get(1)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) blank++;
        }
        return (double) blank / (max - min + 1);
    }
}
