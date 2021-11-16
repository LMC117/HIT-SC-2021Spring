package SubADT.DutyRoster;

import ADT.Interval.IntervalSet;
import ADT.MultiInterval.CommonMultiIntervalSet;
import ADT.MultiInterval.MultiIntervalSet;
import ADT.MultiInterval.NoBlankMultiIntervalSet;
import ADT.MultiInterval.NonOverlapMultiIntervalSet;

import java.time.LocalDate;
import java.util.*;

public class DutyIntervalSet {
    private LocalDate start = null, end = null; // 排班的起始与结束时间
    private final List<Employee> employees = new ArrayList<>(); // 保存上班的员工
    private MultiIntervalSet<Employee> schedule = new CommonMultiIntervalSet<>(); // 保存时间段

    //Abstraction function:
    //  AF(start) = 排班的起始时间
    //  AF(end) = 排班的结束时间
    //  AF(employees) = 上班的员工
    //  AF(schedule) = 排班的时间段
    //Representation invariant:
    //  start对应日期必须小于等于end对应日期
    //  员工的名字是唯一标识符，同一员工可以安排多个时间段
    //  时间段不可重叠
    //Safety from rep exposure:
    //  成员变量使用private修饰，防止其被外部修改
    //  在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    /**
     * 设定排班开始日期、结束日期，具体到年月日
     *
     * @param s1 排班开始日期的字符串
     * @param s2 排班结束日期的字符串
     * @return 若成功设定，返回true;否则返回false
     */
    public boolean setDate(String s1, String s2) {

        if (!schedule.isEmpty()) {
            System.out.println("存在排班，无法设定！");
            return false;
        }

        // 处理起始日期
        String[] startPiece;
        if (!(s1.matches("^(\\d{4})(-)(\\d{2})(-)(\\d{2})"))) {
            System.out.println("起始日期格式错误，请重新输入！");
            return false;
        }
        startPiece = s1.split("-");

        int startYear = Integer.parseInt(startPiece[0]);
        int startMonth = Integer.parseInt(startPiece[1]);
        int startDay = Integer.parseInt(startPiece[2]);

        if (!checkDate(startYear, startMonth, startDay)) {
            System.out.println("起始日期时间表示错误，请重新输入！");
            return false;
        }

        // 读入结束日期
        String[] endPiece;
        if (!(s2.matches("^(\\d{4})(-)(\\d{2})(-)(\\d{2})"))) {
            System.out.println("结束日期格式错误，请重新输入！");
            return false;
        }
        endPiece = s2.split("-");

        int endYear = Integer.parseInt(endPiece[0]);
        int endMonth = Integer.parseInt(endPiece[1]);
        int endDay = Integer.parseInt(endPiece[2]);

        if (!checkDate(endYear, endMonth, endDay)) {
            System.out.println("结束日期格式错误，请重新输入！\n");
            return false;
        }


        // 创建LocalDate对象
        start = LocalDate.of(startYear, startMonth, startDay);
        end = LocalDate.of(endYear, endMonth, endDay);

        if (start.isAfter(end)) {
            System.out.println("起始时间在终止时间之后，请重新输入！");
            return false;
        } else {
            System.out.print("排班时间范围设定完成！\t");
            System.out.printf("起始日期：%d-%d-%d\t", startYear, startMonth, startDay);
            System.out.printf("结束日期：%d-%d-%d\n", endYear, endMonth, endDay);
            return true;
        }
    }

    /**
     * 增加一组员工，包括他们各自的名字、职务、手机号码
     *
     * @param info 输入的员工信息字符串
     * @return 若成功增加，返回true;否则返回false
     */
    public boolean addEmployee(String info) {

        if (!info.matches("^[a-zA-Z]+[{][a-zA-Z\\s*]+,(\\d{3})-(\\d{4})-(\\d{4})}")) {
            System.out.println("格式错误，请重新输入！");
            return false;
        }

        String[] splitString = info.split("[{,}]"); // 分割合规字符串

        String name = splitString[0];
        String duty = splitString[1];
        String rawPhone = splitString[2];

        String[] splitPhone = rawPhone.split("-");
        String phone = splitPhone[0] + splitPhone[1] + splitPhone[2];

        for (Employee e : employees) {
            if (e.getName().equals(name)) {
                System.out.printf("员工%s已存在，请重新输入!\n", name);
                return false;
            }
        }

        employees.add(new Employee(name, duty, phone));
        System.out.printf("成功添加员工[%s,%s,%s]\n", name, duty, phone);
        return true;
    }

    /**
     * 删除员工。如果某个员工已经被编排进排班表，那么他不能被删除，必须将其排班信息删掉之后才能删除该员工。
     *
     * @return 删除成功返回true, 失败返回false
     */
    public boolean deleteEmployee(String name) {
        Employee temp = null;
        for (Employee e : employees) {
            if (e.getName().equals(name)) {
                temp = e;
            }
        }
        if (temp == null) {
            System.out.printf("不存在名为%s的员工!\n", name);
            return false;
        }

        if (checkInRoster(temp)) {
            System.out.printf("员工%s尚在排班表中，无法删除！\n", name);
            return false;
        } else {
            System.out.printf("成功删除员工%s\n", temp.getName());
            employees.remove(temp);
            return true;
        }
    }

    /**
     * 手工选择某个员工、某个时间段（以“日”为单位，最小 1 天，可以是多天），向排班表增加一条排班记录
     *
     * @param name   选定的员工名字
     * @param sStart 代表起始时间的字符串
     * @param sEnd   代表结束时间的字符串
     * @return 若添加成功返回true, 否则返回false
     */
    public boolean manualRosters(String name, String sStart, String sEnd) {
        if (!(sStart.matches("^\\d{4}-\\d{1,2}-\\d{1,2}") && sEnd.matches("^\\d{4}-\\d{1,2}-\\d{1,2}"))) { // 检查输入格式
            System.out.println("格式错误！");
            return false;
        }

        if (start == null || end == null) {
            System.out.println("尚未安排排班时间！");
            return false;
        }

        Employee e = null;
        for (Employee temp : employees) {
            if (temp.getName().equals(name)) {
                e = temp;
            }
        }
        if (e == null) {
            System.out.printf("不存在名为%s的员工!\n", name);
            return false;
        }

        String[] startSplit = sStart.split("-");
        String[] endSplit = sEnd.split("-");

        if (!checkDate(Integer.parseInt(startSplit[0]), Integer.parseInt(startSplit[1]),
                Integer.parseInt(startSplit[2])) || !checkDate(Integer.parseInt(endSplit[0]), Integer.parseInt(endSplit[1]),
                Integer.parseInt(endSplit[2]))) { // 检查日期
            System.out.println("日期有误！");
            return false;
        }

        LocalDate eStart = LocalDate.of(Integer.parseInt(startSplit[0]), Integer.parseInt(startSplit[1]),
                Integer.parseInt(startSplit[2]));
        LocalDate eEnd = LocalDate.of(Integer.parseInt(endSplit[0]), Integer.parseInt(endSplit[1]),
                Integer.parseInt(endSplit[2]));

        if (eStart.isBefore(start) || eStart.isAfter(end) || eEnd.isBefore(start) || eEnd.isAfter(end)) {
            System.out.println("设定时间超出排班范围！");
            return false;
        }

        schedule.insert(getBetweenDays(start, eStart), getBetweenDays(start, eEnd), e);

        // 检查是否存在日期重叠
        MultiIntervalSet<Employee> multiIntervalSet = new NonOverlapMultiIntervalSet<>(schedule);
        if (multiIntervalSet.checkOverlap()) { // 存在重叠
            schedule.removeSpecific(e, getBetweenDays(start, eStart));
            System.out.println("存在日期重叠，添加失败！");
            return false;
        }
        System.out.printf("排班记录添加成功！添加的排班记录为：[%s] -> [%s]  %s\n", sStart, sEnd, name);
        return true;
    }

    /**
     * 自动根据现有员工进行排班
     * <p>
     * 思路
     * 如果days不能被n整除，则最后一个人排 days%n 天班，其他人排 days/n 天班
     */
    public boolean autoRosters() {
        if (!schedule.isEmpty()) {
            System.out.println("已有排班！");
            return false;
        }

        if (start == null || end == null) {
            System.out.println("尚未设定排班时间段！");
            return false;
        }

        if (employees.isEmpty()) {
            System.out.println("尚无员工！");
            return false;
        }

        int n = employees.size(); // 员工人数n
        long days = getBetweenDays(start, end); // 总时间之差
        long i = 0;
        long interval = days / n;

        for (int j = 0; j < n - 1; j++, i += interval)
            schedule.insert(i, i + interval - 1, employees.get(j));
        schedule.insert(i, days, employees.get(n - 1));

        System.out.println("自动排班成功！\n");
        return true;
    }

    /**
     * 删除特定员工的排班
     *
     * @param name  待删除员工的姓名
     * @param start 待删除日期的起始
     * @return 删除成功返回true, 否则返回false
     */
    public boolean deleteRoster(String name, String start) {
        if (!name.matches("^[a-zA-Z]+") || !start.matches("^\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("格式错误，请重新输入！");
            return false;
        }

        if (schedule.isEmpty()) {
            System.out.println("尚未排班！");
            return false;
        }

        Employee e = null;
        boolean flag = false;
        for (Employee temp : employees) {
            if (temp.getName().equals(name)) {
                e = temp;
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("查无此人！");
            return false;
        }

        IntervalSet<Integer> list = schedule.intervals(e);
        if (list == null) {
            System.out.println("此人无排班！");
            return false;
        }

        Set<Integer> set = list.labels();
        boolean checkDate = false;
        String[] split = start.split("-");
        List<Integer> date = new ArrayList<>();
        LocalDate thisBegin = null;
        for (int i : set) {
            long begin = list.start(i), end = list.end(i);
            List<Integer> temp = dateConversion(begin, end);
            if ((temp.get(0) == Integer.parseInt(split[0])) && (temp.get(1) == Integer.parseInt(split[1]))
                    && (temp.get(2) == Integer.parseInt(split[2]))) {
                date = temp;
                thisBegin = LocalDate.of(date.get(0), date.get(1), date.get(2));
                checkDate = true;
            }
        }
        if (!checkDate) {
            System.out.println("未检索到输入日期！");
            return false;
        }

        schedule.removeSpecific(e, getBetweenDays(this.start, thisBegin));


        System.out.printf("成功删除%s的排班信息:[%s] -> [%d-%02d-%02d]\n",
                name, start, date.get(3), date.get(4), date.get(5));
        return true;
    }

    /**
     * 检查排班是否排满，并返回相应的信息
     */
    public void checkFullRoster() {
        MultiIntervalSet<Employee> multiIntervalSet = new NoBlankMultiIntervalSet<>(schedule);

        Iterator<Employee> it = multiIntervalSet.labels().iterator(); // 方便遍历标签集合

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        while (it.hasNext()) {
            Employee label = it.next();
            IntervalSet<Integer> temp = multiIntervalSet.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                min = Math.min(temp.start(tempLabel), min);
                max = Math.max(temp.end(tempLabel), max);
            }
        }

        if (!multiIntervalSet.checkBlank()) {
            if ((min == 0) && (max == getBetweenDays(start, end))) {
                System.out.println("排班已排满!\n");
                return;
            }
        }

        /*
         * 检测空缺的思路：
         * 从开头到结尾依次遍历，获得每个空缺位置的起始和终止，从而得到空缺的intervalSet
         */
        List<Long> interval = new ArrayList<>(); // 保存空闲的时间
        Map<Long, Long> map = saveMap();

        for (long i = 0; i <= getBetweenDays(start, end); i++) { // 获得每个空缺集的元素，保存在interval中
            boolean flag = false;
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                if (i >= entry.getKey() && i <= entry.getValue()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) interval.add(i);
        }

        // 对interval进行处理，归并得到最终的时间段（此部分后续不需修改）
        long endElement = interval.get(interval.size() - 1); // interval的末尾元素
        long iStart = 0, iStartOld = 0, iEnd = endElement;
        Map<Long, Long> intervalMap = new HashMap<>();

        for (long i = 0; i <= endElement + 1; i++) {
            if ((!interval.contains(i - 1)) && interval.contains(i)) { //前一个元素不在其中且当前元素在其中，则当前元素为又一个空闲段的开始
                iStartOld = iStart; // 保存iStart的前值
                iStart = i;
            }
            if (iStart > iEnd) {
                intervalMap.put(iStartOld, iEnd); // 利用了HashMap的性质：键是唯一的
            }
            if (interval.contains(i) && (!interval.contains(i + 1))) {
                iEnd = i;
            }
            if (i == (endElement + 1)) { // 加入最后一次的结果
                intervalMap.put(iStart, iEnd);
            }
        }

        // 利用intervalMap打印空闲段相关信息
        System.out.println("排班未排满！目前尚有的空闲时间如下：");
        for (Map.Entry<Long, Long> entry : intervalMap.entrySet()) {
            List<Integer> list = dateConversion(entry.getKey(), entry.getValue());
            System.out.printf("[%d.%d.%d]-[%d.%d.%d]\n",
                    list.get(0), list.get(1), list.get(2),
                    list.get(3), list.get(4), list.get(5));
        }
        System.out.println("空闲时间比例为：" + calcFreeTimeRatio(schedule));
    }


    /**
     * 可视化当前排班表
     */
    public void rosterVisualization() {
        System.out.println("#######################################");
        System.out.println("员工情况：");
        System.out.println("#######################################");
        if (employees.isEmpty())
            System.out.println("尚无员工");
        else {
            System.out.println("现有员工：");
            for (Employee e : employees) {
                System.out.printf("%s{%s,%s}\n", e.getName(), e.getDuty(), e.getPhone());
            }
        }


        System.out.println("#######################################");
        System.out.println("排班表情况");
        System.out.println("#######################################");
        if (schedule.isEmpty()) {
            System.out.println("尚无排班表");
        } else {
            System.out.println("当前排班表为：");
            for (Employee e : schedule.labels()) {
                IntervalSet<Integer> list = schedule.intervals(e);
                Set<Integer> set = list.labels();
                for (int i : set) {
                    long eStart = list.start(i);
                    long eEnd = list.end(i);
                    List<Integer> dateList = dateConversion(eStart, eEnd);
                    System.out.printf("[%d-%d-%d] -> [%d-%d-%d]  姓名：%s  职务：%s  电话：%s\n",
                            dateList.get(0), dateList.get(1), dateList.get(2),
                            dateList.get(3), dateList.get(4), dateList.get(5),
                            e.getName(), e.getDuty(), e.getPhone()
                    );
                }
            }
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        start = null;
        end = null;
        employees.clear();
        schedule = null;
    }

    /**
     * 检查日期是否合法
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 合法返回true, 否则返回false
     */
    private boolean checkDate(int year, int month, int day) {
        if (month > 12 || day > 31) return false;
        else if ((month == 2 || month == 4 || month == 6 || month == 9 || month == 11) && day == 31) return false;
        else if (month == 2 && day == 30) return false;
        else if (month == 2 && day == 29) {
            return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        } else
            return true;
    }


    /**
     * 检查某员工是否在排班表中
     *
     * @param e 待检测的员工
     * @return 如果其在排班表中，返回true；否则返回false
     */
    private boolean checkInRoster(Employee e) {
        return schedule.labels().contains(e);
    }

    /**
     * 计算两个时间点之间间隔的天数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 两个时间点之间间隔的天数
     */
    private long getBetweenDays(LocalDate start, LocalDate end) {
        return end.toEpochDay() - start.toEpochDay();
    }

    /**
     * 根据时间轴上的起始与终止时间，返回对应的实际日期
     *
     * @param startPoint 时间轴上的起始时间
     * @param endPoint   时间轴上的终止时间
     * @return 包含实际日期的List，其长度为6，从头到尾依次是起始年份、起始月份、起始日期、终止年份、终止月份、终止日期
     */
    private List<Integer> dateConversion(long startPoint, long endPoint) {
        List<Integer> list = new ArrayList<>();

        LocalDate intervalStart = start.plusDays(startPoint);
        LocalDate intervalEnd = start.plusDays(endPoint);

        list.add(intervalStart.getYear());
        list.add(intervalStart.getMonthValue());
        list.add(intervalStart.getDayOfMonth());
        list.add(intervalEnd.getYear());
        list.add(intervalEnd.getMonthValue());
        list.add(intervalEnd.getDayOfMonth());

        return list;
    }

    /**
     * 将用户时间段保存在map中
     *
     * @return 保存有时间段的map
     */
    private Map<Long, Long> saveMap() {
        Iterator<Employee> it = schedule.labels().iterator(); // 方便遍历标签集合
        Map<Long, Long> map = new IdentityHashMap<>();

        while (it.hasNext()) {
            Employee label = it.next();
            IntervalSet<Integer> temp = schedule.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                map.put(temp.start(tempLabel), temp.end(tempLabel));
            }
        }

        return map;
    }

    /**
     * 计算一个 IntervalSet<Employee> 对象中的空闲时间比例
     *
     * @param set 传入的 IntervalSet<Employee>
     * @return 计算得到的空闲时间
     */
    private double calcFreeTimeRatio(MultiIntervalSet<Employee> set) {
        long blank = 0;
        Iterator<Employee> it = set.labels().iterator(); // 方便遍历标签集合
        Map<Long, Long> map = new IdentityHashMap<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = 0;
        long max = getBetweenDays(start, end);
        while (it.hasNext()) {
            Employee label = it.next();
            IntervalSet<Integer> temp = schedule.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                map.put(temp.start(tempLabel), temp.end(tempLabel));
            }
        }

        for (long i = min; i <= max; i++) {
            boolean flag = false;
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                if (i >= entry.getKey() && i <= entry.getValue()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) blank++;
        }
        return (double) blank / (max - min + 1);
    }
}
