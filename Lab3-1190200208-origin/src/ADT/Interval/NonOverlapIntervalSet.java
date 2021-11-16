package ADT.Interval;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class NonOverlapIntervalSet<L> extends IntervalSetDecorator<L> implements IntervalSet<L> {

    // Constructor
    public NonOverlapIntervalSet(IntervalSet<L> intervalSet) {
        super(intervalSet);
    }

    /*
     * 思路
     * 1.首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
     * 2.从min到max，步长为1遍历每个键值对
     * 3.若某个点i同时存在于两个键值对中，则return true，即允许重叠
     * 4.若遍历到最后也没有点同时存在于两个键值对中，则return false，即不允许重叠
     */
    public boolean checkOverlap() {
        Iterator<L> it = intervalSet.labels().iterator(); // 方便遍历标签集合
        Map<Long, Long> map = new IdentityHashMap<>(); // 保存每个时间轴的起点与终点

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            L label = it.next();
            map.put(intervalSet.start(label), intervalSet.end(label));
            min = Math.min(intervalSet.start(label), min);
            max = Math.max(intervalSet.end(label), max);
        }

        // 从min到max，步长为1遍历每个键值对
        for (long i = min; i <= max; i++) {
            boolean flag1 = false, flag2 = false;
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                if (i >= entry.getKey() && i <= entry.getValue()) {
                    if (!flag1)
                        flag1 = true;
                    else {
                        flag2 = true;
                        break;
                    }
                }
            }
            if (flag1 && flag2) return true; // 若某个点i同时存在于两个键值对中，则return true，即允许重叠
        }

        return false; // 若遍历到最后也没有点同时存在于两个键值对中，则return false，即不允许重叠
    }
}
