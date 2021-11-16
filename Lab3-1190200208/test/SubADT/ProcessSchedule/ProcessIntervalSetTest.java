package SubADT.ProcessSchedule;

import org.junit.Test;

public class ProcessIntervalSetTest {
    
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
     * testAddProcess():
     * 添加进程不符合格式
     * 添加进程时间错误
     * 添加进程已存在
     * 正常添加进程
     *
     * testRAschedule():
     * 无进程
     * 正常调度
     * 已调度过
     *
     * testSPschedule():
     * 无进程
     * 正常调度
     * 已调度过
     *
     * testVisualization():
     * 无进程
     * 无调度
     * 有进程且有调度
     *
     */

    @Test
    public void testAddProcess(){
        ProcessIntervalSet p = new ProcessIntervalSet();

        p.addProcess("aaasdsadas");
        p.addProcess("0-init-0-0.02"); // 时间错误
        p.addProcess("0-init-0-22"); // 正常添加
        p.addProcess("0-init_2-0-121221"); // 已添加过

        p.visualization();
    }

    @Test
    public void testRAschedule(){
        ProcessIntervalSet p = new ProcessIntervalSet();

        p.RAschedule(); // 无进程

        p.addProcess("0-init-60-100");
        p.addProcess("1-GUI-5-30");
        p.addProcess("2-System Start-10-40");
        p.addProcess("3-Defender-40-60");

        p.RAschedule();

        p.visualization();

        p.RAschedule(); // 已调度过
    }

    @Test
    public void testSPschedule(){
        ProcessIntervalSet p = new ProcessIntervalSet();

        p.SPschedule(); // 无进程

        p.addProcess("0-A-90-100");
        p.addProcess("1-B-5-30");
        p.addProcess("2-C-10-40");
        p.addProcess("3-D-40-60");

        p.SPschedule();

        p.visualization();

        p.SPschedule(); // 已调度过
    }

    @Test
    public void testVisualization(){
        ProcessIntervalSet p = new ProcessIntervalSet();

        p.visualization(); // 无进程

        p.addProcess("0-A-10-100");
        p.addProcess("1-B-5-30");
        p.addProcess("2-C-10-40");
        p.addProcess("3-D-40-60");

        p.visualization(); // 无调度

        p.SPschedule();

        p.visualization();
    }
}
