package SubADT.ProcessSchedule;

import java.util.Scanner;

public class ProcessScheduleApp {
    static ProcessIntervalSet PSet = new ProcessIntervalSet();

    public static void main(String[] args) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################################");
        System.out.println("欢迎使用进程调度系统！");
        System.out.println("#######################################");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            menu();
        }
    }

    private static void menu() {
        Scanner in = new Scanner(System.in);

        System.out.println("#######################################");
        System.out.println("请选择使用的功能:");
        System.out.println("1.添加进程");
        System.out.println("2.模拟调度（随机选择进程）");
        System.out.println("3.模拟调度（最短进程优先）");
        System.out.println("4.可视化调度结果");
        System.out.println("0.退出");
        System.out.println("#######################################");
        System.out.print("请输入要使用的功能(0~4):");

        String input = in.nextLine();

        switch (input) {
            case "0" -> exit();
            case "1" -> addProcess();
            case "2" -> RAschedule();
            case "3" -> SPschedule();
            case "4" -> visualization();
            default -> System.out.println("输入有误，请重新输入！");
        }
    }


    private static void exit() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#      欢迎下次使用     #");
        System.out.println("#######################");
        System.exit(0);
    }

    private static void addProcess() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#       添加进程       #");
        System.out.println("#######################");

        Scanner in = new Scanner(System.in);

        boolean flag;
        do {
            System.out.print("请输入添加进程的信息(ID-名称-最短执行时间-最大执行时间)：");
            String input = in.nextLine();
            flag = PSet.addProcess(input);
        } while (!flag);
    }

    private static void RAschedule() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#      随机进程调度     #");
        System.out.println("#######################");

        PSet.RAschedule();
    }

    private static void SPschedule() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#    最短进程优先调度    #");
        System.out.println("#######################");

        PSet.SPschedule();
    }

    public static void visualization() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#        可视化        #");
        System.out.println("#######################");

        PSet.visualization();
    }
}
