package SubADT.CourseSchedule;

import ADT.Interval.IntervalSet;
import ADT.MultiInterval.CommonMultiIntervalSet;
import ADT.MultiInterval.MultiIntervalSet;

import java.time.LocalDate;
import java.util.*;

public class CourseIntervalSet {
    private LocalDate start = null, end = null; // 保存学期开始与结束日期
    private final MultiIntervalSet<Course> schedule = new CommonMultiIntervalSet<>(); // 课程上课的时间段
    private final List<Course> courses = new ArrayList<>(); // 保存课程
    private final Map<Course, Integer> arrangedTime = new HashMap<>(); // 保存每门课的每周已安排学时

    //Abstraction function:
    //  AF(start) = 学期开始时间
    //  AF(end) = 学期结束时间
    //  AF(schedule) = 课程上课的时间段
    //  AF(courses) = 已添加的课程
    //  AF(arrangedTime) = 课程的每周已安排学时
    //Representation invariant:
    //  start对应时间不能晚于end对应时间
    //  同一个时间段最多安排一门课程
    //  课程的ID不能重复
    //  每门课的每周上课时间不能超过每周已安排学时
    //Safety from rep exposure:
    //  成员变量使用private / private final修饰，防止其被外部修改
    //  在涉及返回内部变量时，采用防御性拷贝的方式，创造一份新的变量return

    /**
     * 设定学期开始日期（年月日）和总周数（例如 18）
     *
     * @param date    表示学期开始信息的字符串
     * @param rawWeek 表示学期总周数的字符串
     * @return 设定成功返回true;否则返回false，并弹出提示信息
     */
    public boolean setTerm(String date, String rawWeek) {
        if (!schedule.isEmpty()) {
            System.out.println("存在排课，无法设定！");
            return false;
        }

        if (!date.matches("^(\\d{4})(-)(\\d{2})(-)(\\d{2})")) {
            System.out.println("日期格式错误，请重新输入！");
            return false;
        }

        if (!rawWeek.matches("^[1-9]\\d")) {
            System.out.println("周数格式错误，请重新输入！");
            return false;
        }

        String[] dateSplit = date.split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);

        int week = Integer.parseInt(rawWeek);

        start = LocalDate.of(year, month, day);
        end = start.plusWeeks(week);

        System.out.printf("设定成功！   学期开始日期:%s   学期结束日期:%d-%d-%d   总周数:%d\n",
                date, end.getYear(), end.getMonthValue(), end.getDayOfMonth(), week);

        return true;
    }

    /**
     * 增加一组课程，每门课程的信息包括：课程 ID、课程名称、教师名字、地点、周学时数（偶数）
     *
     * @param info 包含课程信息的字符串
     * @return 添加成功返回true;否则返回false，并弹出提示信息
     */
    public boolean addCourse(String info) {
        if (!info.matches("^[a-zA-Z0-9]+-(.*)-(.*)-(\\d{1,2})")) {
            System.out.println("格式错误，请重新输入！");
            return false;
        }

        String[] infoSplit = info.split("-");
        String ID = infoSplit[0];
        String course = infoSplit[1];
        String teacher = infoSplit[2];
        String location = infoSplit[3];
        int weekHour = Integer.parseInt(infoSplit[4]);

        if (weekHour > (5 * 2 * 7) || weekHour % 2 != 0) {
            System.out.println("周学时数格式错误，请重新输入！");
            return false;
        }


        Course c = new Course(ID, course, teacher, location, weekHour);
        if (courses.contains(c)) {
            System.out.println("该课程已存在，请重新输入！");
            return false;
        }
        courses.add(c);
        arrangedTime.put(c, 0);

        System.out.printf("成功添加课程!     ID:%s       课程名:%s      老师:%s       地点:%s       周学时:%d\n",
                ID, course, teacher, location, weekHour);

        return true;
    }

    /**
     * 手工选择某个课程、上课时间（只能是 8-10 时、10-12 时、13-15 时、15-17 时、19-21 时），为其安排一次课，
     * 每次课的时间长度为 2 小时，可重复安排，直到达到周学时数目时该课程不能再安排；
     *
     * @param courseID 输入的代表课程ID的字符串
     * @param info     输入的包含课程信息的字符串
     * @return 安排成功返回true;否则返回false，并弹出提示信息
     */
    public boolean arrangeCourse(String courseID, String info) {
        if (start == null || end == null) {
            System.out.println("尚未设定学期信息，请先进行设定！");
            return false;
        }

        if (!courseID.matches("^[a-zA-Z0-9]+") || !info.matches("^[1-7]-(8-10|10-12|13-15|15-17|19-21)")) {
            System.out.println("课程信息格式错误，请重新输入！");
            return false;
        }

        Course course = null;
        for (Course temp : courses) {
            if (temp.getID().equals(courseID)) {
                course = temp;
            }
        }

        if (course == null) {
            System.out.println("课程不存在，请先添加课程！");
            return false;
        }

        // 处理输入字符串
        /*
         * 插入schedule的思路：
         * 设定星期一的第一次课（8点-10点）为起始位置0，星期日的最后一次课（19点-21点）为终结位置34
         * 每天的5个时段对应5个偏移量，这样最后便可以得到每次课相较于星期一的第一次课的偏移量，若记为n，则向其中插入[n,n]的课程
         * 这样便能方便地抽象化表示每周的课程安排
         */
        String[] infoSplit = info.split("-");
        int weekOffset = Integer.parseInt(infoSplit[0]) - 1; // 标志星期几的偏移量
        int flag = Integer.parseInt(infoSplit[1]);
        int dayOffset = 0; // 标志每日内的偏移量
        switch (flag) {
            case 8 -> {
            }
            case 10 -> dayOffset = 1;
            case 13 -> dayOffset = 2;
            case 15 -> dayOffset = 3;
            case 19 -> dayOffset = 4;
        }
        long offset = weekOffset * 5L + dayOffset; // 总偏移量

        // 检查同一时间是否该课程已被安排
        for(Course c:courses) {
            IntervalSet<Integer> intervalSet = schedule.intervals(c);
            if (intervalSet != null) {
                Set<Integer> set = intervalSet.labels();
                for (int label : set) {
                    if (intervalSet.start(label) == (int) offset) {
                        System.out.println("同一时间已安排有课程，无法再次安排！");
                        return false;
                    }
                }
            }
        }

        // 进行学时数的计算、判断与更新
        int weekHour = course.getWeekHour();
        int doneHour = arrangedTime.get(course); // 已完成的学时数
        if (doneHour == weekHour) {
            System.out.println("课程学时数已满，无法继续安排！");
            return false;
        } else
            arrangedTime.put(course, doneHour + 2);

        // 插入schedule
        schedule.insert(offset, offset, course);

        System.out.printf("课程安排成功！   ID:%s   课程名:%s      安排时间:每周%s,%s时-%s时\n",
                courseID, course.getName(), numberToWeek(weekOffset + 1), infoSplit[1], infoSplit[2]);

        return true;
    }

    /**
     * 查看哪些课程没安排、当前每周的空闲时间比例、重复时间比例
     */
    public void checkSchedule() {
        // 查看哪些课程尚未被安排
        System.out.println("#######################################");
        System.out.println("检查是否有未安排课程");
        System.out.println("#######################################");
        boolean flag = false;
        Set<Course> label = schedule.labels();

        if (label.isEmpty()) {
            System.out.println("没有课程！");
            return;
        }

        for (Course c : courses) {
            if (!label.contains(c)) { // 该课程未被安排
                System.out.printf("课程未被安排:     ID:%s       课程名:%s      老师:%s       地点:%s       周学时:%d\n",
                        c.getID(), c.getName(), c.getTeacher(), c.getLocation(), c.getWeekHour());
                flag = true;
            }
        }

        if (!flag)
            System.out.println("所有课程均已安排！");

        // 查看当前每周的空闲时间比例
        System.out.println("#######################################");
        System.out.printf("当前每周空闲时间比例:%f\n", calcFreeTimeRatio());
        System.out.println("#######################################");

        // 查看当前每周的重复时间比例
        System.out.println("#######################################");
        System.out.printf("当前每周冲突时间比例:%f\n", calcConflictRatio());
        System.out.println("#######################################");
    }

    /**
     * 用户查看本学期内任意一天的课表结果
     *
     * @param date 带有日期信息的字符串
     * @return 成功查看返回true，否则返回false
     */
    public boolean showSchedule(String date) {
        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("格式错误！");
            return false;
        }

        String[] splitString = date.split("-"); // 分割合规字符串

        int year = Integer.parseInt(splitString[0]);
        int month = Integer.parseInt(splitString[1]);
        int day = Integer.parseInt(splitString[2]);

        if (splitString.length != 3 || year < 0 || month < 1 || day < 1 || month > 12 || day > 31) {
            System.out.println("起始日期时间表示错误，请重新输入！");
            return false;
        }

        LocalDate inputDate = LocalDate.of(year, month, day);

        if (start == null || end == null) {
            System.out.println("尚未排期，请重新输入！");
            return false;
        }

        if (inputDate.isAfter(end) || inputDate.isBefore(start)) {
            System.out.println("输入日期不在学期范围内，请重新输入！");
            return false;
        }

        int dayOfWeek = inputDate.getDayOfWeek().getValue();
        int weekOffset = dayOfWeek - 1;
        long rangeMin = weekOffset * 5L; // 检索起点
        long rangeMax = rangeMin + 4; // 检索终点

        // 检索当日课程
        List<List<String>> list = new ArrayList<>();

        for (Course c : courses) {
            IntervalSet<Integer> intervalSet = schedule.intervals(c);
            if (intervalSet != null) {
                Set<Integer> set = intervalSet.labels();
                for (int i : set) {
                    if (intervalSet.start(i) >= rangeMin && intervalSet.start(i) <= rangeMax) { // 该时间段的该课程在课表上

                        int intDayOffset = (int) (intervalSet.start(i) % 5);
                        String dayOffset = String.valueOf(intDayOffset);
                        List<String> subList = new ArrayList<>();

                        String s = "上课时间:" + dayOffsetToString(intDayOffset) + "     课程名:" + c.getName() +
                                "   授课老师:" + c.getTeacher() + "     地点:" + c.getLocation();

                        subList.add(dayOffset);
                        subList.add(s);
                        list.add(subList);
                    }
                }
            }
        }

        System.out.println("#######################################");
        System.out.printf("输入日期:%s, 为%s, ", date, numberToWeek(dayOfWeek));
        if (!list.isEmpty())
            System.out.println("当日课表如下:");
        else {
            System.out.println("当日无课");
            System.out.println("#######################################");
            return true;
        }

        while (!list.isEmpty()) {
            List<String> temp = list.get(0);
            for (List<String> entry : list) {
                if (temp.get(0).compareTo(entry.get(0)) > 0) { // temp的字典序在entry后，返回正数
                    temp = entry;
                }
            }
            System.out.println(temp.get(1));
            list.remove(temp);
        }

        System.out.println("当前其余时间无课");
        System.out.println("#######################################");

        return true;
    }

    /**
     * 根据数字返回对应的星期几字符串
     *
     * @param num 表示星期几的数字，1~7
     * @return 字符串"星期X"
     */
    private String numberToWeek(int num) {
        String s;
        switch (num) {
            case 1 -> s = "星期一";
            case 2 -> s = "星期二";
            case 3 -> s = "星期三";
            case 4 -> s = "星期四";
            case 5 -> s = "星期五";
            case 6 -> s = "星期六";
            case 7 -> s = "星期日";
            default -> s = "星期转换函数错误";
        }
        return s;
    }

    /**
     * 根据日偏移量返回对应的上课时间
     *
     * @param dayOffset 日偏移量
     * @return 对应的上课时间
     */
    private String dayOffsetToString(int dayOffset) {
        String s;
        switch (dayOffset) {
            case 0 -> s = "8时-10时";
            case 1 -> s = "10时-12时";
            case 2 -> s = "13时-15时";
            case 3 -> s = "15时-17时";
            case 4 -> s = "19时-21时";
            default -> s = "转换错误";
        }
        return s;
    }

    /**
     * 计算当前schedule中的空闲时间比例
     *
     * @return schedule中的空闲时间比例
     */
    private double calcFreeTimeRatio() {
        long blank = 0;
        Iterator<Course> it = schedule.labels().iterator(); // 方便遍历标签集合
        List<List<Long>> list = new ArrayList<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = 0;
        long max = 34;
        while (it.hasNext()) {
            Course label = it.next();
            IntervalSet<Integer> temp = schedule.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                List<Long> subList = new ArrayList<>();
                subList.add(temp.start(tempLabel));
                subList.add(temp.end(tempLabel));
                list.add(subList);
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

    /**
     * 计算当前schedule中的重复时间比例
     *
     * @return schedule中的重复时间比例
     */
    private double calcConflictRatio() {
        Iterator<Course> it = schedule.labels().iterator(); // 方便遍历标签集合
        List<List<Long>> list = new ArrayList<>();

        // 首先找到时间轴的起点与终点min,max；并将每组标签对应的start-end保存在键值对中
        long min = 0;
        long max = 34;
        while (it.hasNext()) {
            Course label = it.next();
            IntervalSet<Integer> temp = schedule.intervals(label);
            for (Integer tempLabel : temp.labels()) {
                List<Long> subList = new ArrayList<>();
                subList.add(temp.start(tempLabel));
                subList.add(temp.end(tempLabel));
                list.add(subList);
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
}
