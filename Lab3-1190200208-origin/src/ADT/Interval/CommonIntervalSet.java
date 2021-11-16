package ADT.Interval;

import java.util.*;

public class CommonIntervalSet<L> implements IntervalSet<L> {
    private final Map<L, List<Long>> timeSchedule = new HashMap<>();
    private final Set<L> labels = new HashSet<>();

    //Abstraction function:
    //  AF(timeSchedule) = IntervalSet中不同标签对应的时间段
    //  AF(labels) = IntervalSet中的所有标签
    //Representation invariant:
    //  时间段的标签不能重复
    //  时间段有且仅有一个起点，一个终点
    //  时间段起点对应时刻必须小于等于终点
    //  时间段起点，终点必须不小于0
    //Safety from rep exposure:
    //  成员变量timeSchedule用private final修饰，防止其被外部修改
    //  在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    // constructor
    public CommonIntervalSet() {
    }

    // checkRep
    private void checkRep() {
        assert labels.size() == timeSchedule.size(); // 确保标签不重复

        for (List<Long> temp : timeSchedule.values()) {
            assert temp.size() == 2;    // 确保时间段有且仅有一个起点，一个终点

            long front = temp.get(0);
            long rear = temp.get(1);

            assert front >= 0;
            assert rear >= 0;
            assert front <= rear;
        }
    }

    @Override
    public void insert(long start, long end, L label) {
        if (start > end || end < 0 || labels.contains(label))
            return;
        List<Long> temp = new ArrayList<>();
        temp.add(start);
        temp.add(end);
        timeSchedule.put(label, temp);

        labels.add(label);

        checkRep();
    }

    @Override
    public Set<L> labels() {
        return new HashSet<>(labels); // 返回一个新的HashSet，防止暴露自身变量
    }

    @Override
    public boolean remove(L label) {
        Iterator<Map.Entry<L, List<Long>>> it = timeSchedule.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<L, List<Long>> entry = it.next();
            if (entry.getKey().equals(label)) {
                timeSchedule.remove(entry.getKey(), entry.getValue());
                labels.remove(label);
                return true;
            }
        }
        return false;
//        for (Map.Entry<L, List<Long>> entry : timeSchedule.entrySet()) {
//            if (entry.getKey().equals(label)) {
//                timeSchedule.remove(entry.getKey(), entry.getValue());
//                labels.remove(label);
//                return true;
//            }
//        }
//        return false;
    }

    @Override
    public long start(L label) {
        for (Map.Entry<L, List<Long>> entry : timeSchedule.entrySet()) {
            if (entry.getKey().equals(label)) {
                return entry.getValue().get(0);
            }
        }
        return -1;
    }

    @Override
        public long end(L label) {
        for (Map.Entry<L, List<Long>> entry : timeSchedule.entrySet()) {
            if (entry.getKey().equals(label)) {
                return entry.getValue().get(1);
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return timeSchedule.isEmpty() && labels.isEmpty();
    }

    @Override
    public boolean checkBlank() {
        return new NoBlankIntervalSet<>(this).checkBlank();
    }

    @Override
    public boolean checkOverlap() {
        return new NonOverlapIntervalSet<>(this).checkOverlap();
    }

    /**
     * 将IntervalSet的信息以字符串形式输出
     *
     * @return 展示IntervalSet的具体信息
     */
    @Override
    public String toString() {
        StringBuilder re = new StringBuilder();
        for (Map.Entry<L, List<Long>> entry : timeSchedule.entrySet()) {
            String temp = "标签：" + entry.getKey() + "; " + "时间段：" + entry.getValue() + "\n";
            re.append(temp);
        }
        checkRep();
        return re.toString();
    }
}
