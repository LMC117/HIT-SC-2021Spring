package SubADT.ProcessSchedule;

import ADT.Interval.IntervalSet;
import ADT.MultiInterval.CommonMultiIntervalSet;
import ADT.MultiInterval.MultiIntervalSet;

import java.util.*;

public class ProcessIntervalSet {
    private final MultiIntervalSet<Process> schedule = new CommonMultiIntervalSet<>(); // 保存进程执行的时间段
    private final List<Process> processes = new ArrayList<>();  // 保存进程
    private final Map<Process, Long> executedTime = new HashMap<>(); // 保存每个进程的已执行时间,键为Process，值为已执行时间

    //Abstraction function:
    //  AF(schedule) = 代表进程执行的时间段
    //  AF(processes) = 进程
    //  AF(executedTime) = 每个进程的已执行时间
    //Representation invariant:
    //  进程执行的时间段不可重叠（但可相接）
    //  进程ID不可重复
    //  已执行时间不可小于0，不可超过进程的最大执行时间
    //Safety from rep exposure:
    //  成员变量使用private final修饰，防止其被外部修改
    //  在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    /**
     * 添加进程
     *
     * @param info 包含进程信息的字符串，格式：ID-名称-最短执行时间-最大执行时间
     * @return 若添加成功返回true，否则返回false
     */
    public boolean addProcess(String info) {
        if (!info.matches("^[0-9]+-[a-zA-Z0-9\\s*_]+-\\d+-\\d+")) {
            System.out.println("格式错误，请重新输入！");
            return false;
        }

        String[] splitInfo = info.split("-");

        long ID = Long.parseLong(splitInfo[0]);
        String name = splitInfo[1];
        long minTime = Long.parseLong(splitInfo[2]);
        long maxTime = Long.parseLong(splitInfo[3]);

        if (maxTime < minTime) {
            System.out.println("时间错误，请重新输入！");
            return false;
        }

        Process p = new Process(ID, name, minTime, maxTime);

        if (processes.contains(p)) {
            System.out.println("该进程已存在！");
            return false;
        } else {
            processes.add(p);
            executedTime.put(p, 0L);
            System.out.printf("添加成功！        ID:%d   名称：%s\n", ID, name);
            return true;
        }
    }

    /**
     * 随机选择进程进行调度
     *
     * @return 若调度成功返回true，否则返回false
     */
    public boolean RAschedule() {
        if (processes.isEmpty()) {
            System.out.println("没有进程可供调度！");
            return false;
        }

        if (!schedule.isEmpty()) {
            System.out.println("进程已被安排调度！");
            return false;
        }

        long timePoint = 0;
        Random rand = new Random();

        List<Process> temp_processes = new ArrayList<>(processes); // 保存尚未完全执行的进程
        while (!temp_processes.isEmpty()) {
            // 上次执行进程后的休眠时间
            long sleepTime = rand.nextInt(10);
            timePoint += sleepTime;

            // 选取下一个进程
            int size = temp_processes.size();
            int random;
            if (size > 1) // 随机选取下一个执行的进程
                random = rand.nextInt(size - 1);
            else
                random = 0;

            Process p = temp_processes.get(random); // p为下一个执行的进程

            long minTime = p.getMinTime();
            long maxTime = p.getMaxTime();

            // 计算下一个进程的执行情况
            long thisTime = (long) (rand.nextDouble() * maxTime); // p此次的随机执行时间
            long executed = executedTime.get(p); // p的已执行时间
            long totalTime = thisTime + executed; // 此次运行后p的总执行时间

            if (totalTime >= maxTime) { // 进程已执行完毕，且此次分配的时间时间超出
                thisTime = maxTime - executed; // p的实际已执行时间
                totalTime = maxTime;
                temp_processes.remove(p); // 将p从尚未完全执行的进程的集合中删去
            }

            if (totalTime >= minTime) { // 进程执行完毕，且分配时间未超出
                temp_processes.remove(p); // 将p从尚未完全执行的进程的集合中删去
            }

            schedule.insert(timePoint, timePoint + thisTime, p);
            executedTime.put(p, totalTime);
            timePoint += thisTime;
        }
        System.out.println("随机调度完毕！请用可视化功能查看！");
        return true;
    }

    /**
     * 按“最短进程优先”的原则进行调度
     *
     * @return 若调度成功返回true，否则返回false
     */
    public boolean SPschedule() {
        if (processes.isEmpty()) {
            System.out.println("没有进程可供调度！");
            return false;
        }

        if (!schedule.isEmpty()) {
            System.out.println("进程已被安排调度！");
            return false;
        }

        long timePoint = 0;
        Random rand = new Random();

        List<Process> temp_processes = new ArrayList<>(processes); // 保存尚未完全执行的进程
        while (!temp_processes.isEmpty()) {
            // 上次执行进程后的休眠时间
            long sleepTime = rand.nextInt(10);
            timePoint += sleepTime;

            // 选取下一个进程（根据最短进程优先原则）

            long remainTime = Long.MAX_VALUE;

            for (Process temp : temp_processes) { // 找到最短的剩余时间
                long temp_remainTime = temp.getMaxTime() - executedTime.get(temp);
                remainTime = Math.min(temp_remainTime, remainTime);
            }

            Process p = temp_processes.get(0); // p为下一个执行的进程

            for (Process temp : temp_processes) {
                long temp_remainTime = temp.getMaxTime() - executedTime.get(temp);
                if (temp_remainTime == remainTime) {
                    p = temp;
                }
            }

            long minTime = p.getMinTime();
            long maxTime = p.getMaxTime();

            // 计算下一个进程的执行情况
            long thisTime = (long) (rand.nextDouble() * maxTime); // p此次的随机执行时间
            long executed = executedTime.get(p); // p的已执行时间
            long totalTime = thisTime + executed; // 此次运行后p的总执行时间

            if (totalTime >= maxTime) { // 进程已执行完毕，且此次分配的时间时间超出
                thisTime = maxTime - executed; // p的实际已执行时间
                totalTime = maxTime;
                temp_processes.remove(p); // 将p从尚未完全执行的进程的集合中删去
            }

            if (totalTime >= minTime) { // 进程执行完毕，且分配时间未超出
                temp_processes.remove(p); // 将p从尚未完全执行的进程的集合中删去
            }

            schedule.insert(timePoint, timePoint + thisTime, p);
            executedTime.put(p, totalTime);
            timePoint += thisTime;
        }
        System.out.println("最短进程优先策略调度完毕！请用可视化功能查看！");
        return true;
    }

    /**
     * 可视化进程调度系统情况
     */
    public void visualization() {
        System.out.println("#######################################");
        System.out.println("进程情况：");
        System.out.println("#######################################");
        if (processes.isEmpty())
            System.out.println("尚无进程");
        else {
            System.out.println("现有进程：");
            for (Process p : processes) {
                if (executedTime.get(p) >= p.getMinTime())
                    System.out.printf("ID:%d    名称:%s   最小执行时间:%d   最大执行时间:%d   执行状态:已执行\n",
                            p.getID(), p.getName(), p.getMinTime(), p.getMaxTime());
                else
                    System.out.printf("ID:%d    名称:%s   最小执行时间:%d   最大执行时间:%d   执行状态:未调度\n",
                            p.getID(), p.getName(), p.getMinTime(), p.getMaxTime());
            }
        }

        System.out.println("#######################################");
        System.out.println("调度情况");
        System.out.println("#######################################");
        if (schedule.isEmpty()) {
            System.out.println("尚未进行调度");
        } else {
            System.out.println("当前调度情况：");
            // 对进程进行排序
            List<List<long[]>> info = toList();
            while (!info.isEmpty()) {
                long start = Long.MAX_VALUE, end = 0;
                long ID = 0;
                String name = null;
                for (List<long[]> entry : info) { // 找到最小进程对应的起始与终点
                    if (entry.get(1)[0] < start) {
                        start = entry.get(1)[0];
                        end = entry.get(1)[1];
                        ID = entry.get(0)[0];
                    }
                }
                for (Process p : processes) {
                    if (p.getID() == ID)
                        name = p.getName();
                }
                System.out.printf("[%d -> %d]  ID:%d  进程名：%s\n", start, end, ID, name);
                // 删除list中的该元素
                Iterator<List<long[]>> it = info.iterator();
                while (it.hasNext()) {
                    List<long[]> entry = it.next();
                    if (entry.get(1)[0] == start && entry.get(1)[1] == end)
                        it.remove();
                }
            }
        }
    }

    /**
     * 将所有的时间段保存在list中，list.get(0):进程的ID    list.get(1):[起始时间，终止时间]
     *
     * @return 保存所有时间段信息的Map
     */
    private List<List<long[]>> toList() {
        List<List<long[]>> info = new ArrayList<>();
        for (Process p : schedule.labels()) {
            IntervalSet<Integer> intervalSet = schedule.intervals(p);
            Set<Integer> set = intervalSet.labels();

            for (int i : set) {
                List<long[]> subInfo = new ArrayList<>();
                long start = intervalSet.start(i);
                long end = intervalSet.end(i);
                long[] ID = {p.getID()};
                long[] time = {start, end};
                subInfo.add(ID);
                subInfo.add(time);
                info.add(subInfo);
            }
        }
        return info;
    }
}
