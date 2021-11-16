package SubADT.CourseSchedule;

import org.junit.Test;

public class CourseIntervalSetTest {

    /**
     * Tests that assertions are enabled.
     */
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    /*
     * Testing Strategy
     *
     * testSetTerm():
     * 测试如下几种情况：
     * 日期格式错误
     * 周数格式错误
     * 正常设定
     * 存在排课
     *
     * testAddCourse():
     * 测试如下几种情况：
     * 不合法输入
     * 正常添加
     * 已添加
     *
     * testArrangeCourse():
     * 测试如下几种情况：
     * 未设定学期信息
     * 输入信息格式错误
     * 课程不存在（尚未添加）
     * 正常添加
     * 课程学时数已满
     * 重复添加到同一时段
     *
     * testCheckSchedule():
     * 测试如下几种情况：
     * 未排课
     * 有排课
     *
     * testShowSchedule():
     * 测试如下几种情况：
     * 输入格式错误
     * 未安排学期
     * 查询日期在学期外
     * 正常查询，无课
     */

    @Test
    public void testSetTerm() {
        CourseIntervalSet c = new CourseIntervalSet();

        c.setTerm("2001.09.01", "19"); // 日期格式错误
        c.setTerm("2001-09-01", "-19"); // 周数格式错误
        c.setTerm("2001-09-01", "19"); // 正常设定
        // 存在排课

    }

    @Test
    public void testAddCourse() {
        CourseIntervalSet c = new CourseIntervalSet();

        c.addCourse("CS15213-CSAPP-Liu H.W-正心21-6aa"); // 格式错误
        c.addCourse("CS15213-CSAPP-Liu H.W-正心21-6"); // 正常添加
        c.addCourse("CS15213-CSAPP-Liu H.W-正心21-6"); // 已添加
    }

    @Test
    public void testArrangeCourse() {
        CourseIntervalSet c = new CourseIntervalSet();

        c.addCourse("CS1-软件构造-W.Z.J-正心21-6");
        c.addCourse("CS2-数据结构-Z.Y.Y-正心42-4");
        c.addCourse("CS3-形式语言-S.D.L-正心13-4");

        c.arrangeCourse("CS1", "1-8-10"); // 未设定学期信息

        c.setTerm("2021-04-01", "18");

        c.arrangeCourse("CS1", "1-8-12"); // 输入信息格式错误

        c.arrangeCourse("CS4", "1-8-10"); // 课程不存在（尚未添加）

        c.arrangeCourse("CS1", "1-8-10"); // 正常添加
        c.arrangeCourse("CS2", "1-8-10"); // 正常添加
        c.arrangeCourse("CS2", "7-19-21"); // 正常添加
        c.arrangeCourse("CS3", "1-8-10"); // 正常添加

        c.arrangeCourse("CS2", "6-19-21"); // 课程学时数已满

        c.arrangeCourse("CS1", "1-8-10"); // 重复添加到同一时段

    }

    @Test
    public void testCheckSchedule() {
        CourseIntervalSet c = new CourseIntervalSet();

        c.checkSchedule();

        c.addCourse("CS1-软件构造-W.Z.J-正心21-6");
        c.addCourse("CS2-数据结构-Z.Y.Y-正心42-4");
        c.addCourse("CS3-形式语言-S.D.L-正心13-4");

        c.setTerm("2021-04-01", "18");

        c.arrangeCourse("CS1", "1-8-10"); // 正常添加
        c.arrangeCourse("CS2", "1-8-10"); // 正常添加

        c.checkSchedule();

        c.arrangeCourse("CS2", "7-19-21"); // 正常添加
        c.arrangeCourse("CS3", "1-8-10"); // 正常添加

        c.checkSchedule();
    }

    @Test
    public void testShowSchedule() {
        CourseIntervalSet c = new CourseIntervalSet();

        c.showSchedule("2021-07-02"); // 尚未设定学期

        c.showSchedule("2021.07.02"); // 输入格式错误
        c.showSchedule("20011-01-01"); // 输入格式错误

        c.setTerm("2021-07-02", "18");

        c.showSchedule("2021-07-01"); // 查询日期在学期外

        c.showSchedule("2021-07-02"); // 当天无课

        c.addCourse("CS1-软件构造-W.Z.J-正心21-6");
        c.addCourse("CS2-数据结构-Z.Y.Y-正心42-4");
        c.addCourse("CS3-形式语言-S.D.L-正心13-4");

        c.arrangeCourse("CS1", "5-8-10"); // 正常添加
        c.arrangeCourse("CS2", "5-8-10"); // 正常添加
        c.arrangeCourse("CS2", "5-19-21"); // 正常添加
        c.arrangeCourse("CS3", "5-10-12"); // 正常添加

        c.showSchedule("2021-07-02"); // 当天有课
    }
}
