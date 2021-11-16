package SubADT.DutyRoster;

import org.junit.Test;

public class DutyIntervalSetTest {

    /**
     * Tests that assertions are enabled.
     */
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    /*
     * Testing Strategy
     * testSetDate():
     * 测试如下几种情况：
     * 不按格式输入
     * 已排班
     * 起始日期大于结束日期
     *
     * testAddEmployee():
     * 测试如下几种情况：
     * 不按格式输入
     * 正常输入
     * 输入重名
     *
     * testDeleteEmployee():
     * 测试如下几种情况：
     * 不按格式输入
     * 输入的人不存在
     * 输入的人存在
     *
     * testManualRosters():
     * 测试如下几种情况：
     * 输入格式错误
     * 尚未设定时间
     * 正常输入
     * 时间输入错误
     * 输入的人已排班
     * 输入的人不存在
     * 输入的排班时间冲突
     * 输入的排班时间超范围
     *
     * testAutoRosters():
     * 已有排班
     * 没有员工
     * 没有设定起始时间
     * 正常情况（能被整除，不能被整除）
     *
     * testDeleteRoster():
     * 尚未排班
     * 输入错误
     * 不存在该员工
     * 时间格式错误
     * 不存在对应时间
     * 正常情况
     *
     * testCheckFullRoster():
     * 测试如下几种情况：
     * 排满
     * 未排满
     *
     *
     * testRosterVisualization():
     * 测试可视化
     */
    @Test
    public void testSetDate() {
        DutyIntervalSet a = new DutyIntervalSet();
        DutyIntervalSet b = new DutyIntervalSet();
        DutyIntervalSet c = new DutyIntervalSet();
        DutyIntervalSet d = new DutyIntervalSet();

//        a.setDate("aaaa","bbbb");
//        a.addEmployee();
//        a.autoRosters();
        a.setDate("2001-01-01", "2001-02-01");
        b.setDate("2001-01-01", "2001.02.01");
        c.setDate("2001-22-01", "2001-22-01");
        d.setDate("2001-02-01", "2001-01-01");
    }

    @Test
    public void testAddEmployee() {
        DutyIntervalSet a = new DutyIntervalSet();

        a.addEmployee("asdassss");
        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
    }

    @Test
    public void testDeleteEmployee() {
        DutyIntervalSet a = new DutyIntervalSet();

        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
        a.deleteEmployee("ZhangSan");
    }

    @Test
    public void testManualRosters() {
        DutyIntervalSet a = new DutyIntervalSet();

        a.manualRosters("ZhangSan","2000-01-01","2000-02-01"); // 尚未设定排班时间段

        a.setDate("2000-01-01","2000-02-25");

        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
        a.addEmployee("LuLiu{Noob,111-2111-1111}");

        a.manualRosters("ZhangSan","2000.01.01","2000-02-01");
        a.manualRosters("ZhangSan","2000-01-01","2000-02-30");
        a.manualRosters("ZhangSan","2000-01-01","2000-02-01");
        a.manualRosters("LiSi","2000-02-02","2000-02-11");
        a.manualRosters("WangWu","2000-02-12","2000-02-22");
        a.manualRosters("ZhangSan","2000-02-23","2000-02-23"); // 输入的人已排班
        a.manualRosters("a","2000-02-23","2000-02-23"); // 输入的人不存在
        a.manualRosters("LuLiu","2000-02-22","2000-02-22"); // 输入的排班时间冲突
        a.manualRosters("LuLiu","2000-02-23","2000-02-26"); // 输入的排班时间超范围

        a.rosterVisualization();
    }

    @Test
    public void testAutoRosters() {
        DutyIntervalSet a = new DutyIntervalSet();

        a.autoRosters(); // 尚未设定排班时间段

        a.setDate("2000-01-01","2000-02-03");

        a.autoRosters(); // 没有员工

        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
        a.addEmployee("LuLiu{Noob,111-2111-1111}");

        a.autoRosters();

        a.autoRosters(); // 已有排班
    }

    @Test
    public void testDeleteRoster(){
        /*
         *      * testDeleteRoster():
         * 尚未排班
         * 输入错误
         * 不存在该员工
         * 时间格式错误
         * 不存在对应时间
         * 正常情况
         */
        DutyIntervalSet a = new DutyIntervalSet();

        a.deleteRoster("a","2001-01-01"); // 尚未排班

        a.setDate("2001-01-01","2001-01-10");

        a.addEmployee("a{boss,111-1111-1111}");

        a.manualRosters("a","2001-01-01","2001-01-10");

        a.deleteRoster("a","2001.01.01"); // 输入错误
        a.deleteRoster("b","2001-01-01"); // 不存在该员工
        a.deleteRoster("a","2001.01.01"); // 时间格式错误
        a.deleteRoster("a","2001-01-02"); // 不存在对应时间
        a.deleteRoster("a","2001-01-01"); // 正常情况

        a.rosterVisualization();
    }

    @Test
    public void testCheckFullRoster() {
        DutyIntervalSet a = new DutyIntervalSet();

        a.setDate("2000-01-01","2000-01-10");

        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
        a.addEmployee("LuLiu{Noob,111-2111-1111}");

        a.manualRosters("ZhangSan","2000-01-01","2000-01-03");
        a.manualRosters("LiSi","2000-01-06","2000-01-06");
        a.manualRosters("WangWu","2000-01-08","2000-01-09");

        a.checkFullRoster();
    }

    @Test
    public void testRosterVisualization() {
        DutyIntervalSet a = new DutyIntervalSet();

        a.setDate("2000-01-01","2000-01-10");

        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
        a.addEmployee("LuLiu{Noob,111-2111-1111}");

        a.manualRosters("ZhangSan","2000-01-01","2000-01-03");
        a.manualRosters("LiSi","2000-01-06","2000-01-06");
        a.manualRosters("WangWu","2000-01-08","2000-01-09");

        a.rosterVisualization();
    }

    @Test
    public void testClear(){
        DutyIntervalSet a = new DutyIntervalSet();

        a.setDate("2000-01-01","2000-01-10");

        a.addEmployee("ZhangSan{Manger,139-0451-0000}");
        a.addEmployee("LiSi{Secretary,151-0101-0000}");
        a.addEmployee("WangWu{Associate Dean,177-2021-0301}");
        a.addEmployee("LuLiu{Noob,111-2111-1111}");

        a.manualRosters("ZhangSan","2000-01-01","2000-01-03");
        a.manualRosters("LiSi","2000-01-06","2000-01-06");
        a.manualRosters("WangWu","2000-01-08","2000-01-09");

        a.clear();
    }
}
