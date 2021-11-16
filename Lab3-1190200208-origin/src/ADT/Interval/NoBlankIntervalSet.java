package ADT.Interval;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class NoBlankIntervalSet<L> extends IntervalSetDecorator<L> implements IntervalSet<L> {

    // Constructor
    public NoBlankIntervalSet(IntervalSet<L> intervalSet) {
        super(intervalSet);
    }

    /*
     * 思路
     * 1.首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
     * 2.从min到max，步长为1遍历每个键值对
     * 3.若存在某个值不在一个键值对区间中，则返回true，否则遍历完成后返回false
     */
    public boolean checkBlank() {
        Iterator<L> it = intervalSet.labels().iterator(); // 方便遍历标签集合
        Map<Long, Long> map = new IdentityHashMap<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            L label = it.next();
            map.put(intervalSet.start(label), intervalSet.end(label));
            min = Math.min(intervalSet.start(label), min);
            max = Math.max(intervalSet.end(label), max);
        }

        //从min到max，步长为1遍历每个键值对
        for (long i = min; i < max; i++) {
            boolean flag = false;
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                if (i >= entry.getKey() && i <= entry.getValue()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) return true; // 此次循环，i不在任何一个键值对区间中
        }
        return false;
    }
}
