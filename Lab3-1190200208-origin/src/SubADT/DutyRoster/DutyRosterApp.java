package SubADT.DutyRoster;

import java.io.IOException;
import java.util.Scanner;

public class DutyRosterApp {
    static DutyIntervalSet DSet = new DutyIntervalSet();

    public static void main(String[] args) throws IOException {
        initial();
    }

    /**
     * 初始化
     */
    private static void initial() throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("#######################################");
        System.out.println("#      欢迎使用排班管理系统!              #");
        System.out.println("#      初次使用前,                      #");
        System.out.println("#      请先设定排班日期.                 #");
        System.out.println("#      按Y开始,按N退出.                 #");
        System.out.println("#######################################");
        String str = in.nextLine();
        if (str.equals("Y") || str.equals("y"))
            menu();
        else if (str.equals("N") || str.equals("n"))
            System.exit(0);
        else
            System.out.println("请重新输入！");
    }

    /**
     * 菜单
     */
    private static void menu() throws IOException {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Scanner in = new Scanner(System.in);

            while (true) {
                System.out.println("#######################################");
                System.out.println("请选择使用的功能:");
                System.out.println("1.设定排班日期（如要重设，请先确保没有排班）");
                System.out.println("2.添加员工");
                System.out.println("3.删除员工");
                System.out.println("4.手动排班");
                System.out.println("5.自动排班");
                System.out.println("6.删除排班");
                System.out.println("7.检查排班是否排满");
                System.out.println("8.查询排班详细信息");
                System.out.println("9.读取文件进行设定");
                System.out.println("0.退出");
                System.out.println("");
                System.out.println("             在选择使用功能后,可输入q返回菜单");
                System.out.println("#######################################");
                System.out.print("请输入要使用的功能(0~9):");

                String input = in.nextLine();

                switch (input) {
                    case "0" -> exit();
                    case "1" -> setDate();
                    case "2" -> addEmployee();
                    case "3" -> deleteEmployee();
                    case "4" -> manualRoster();
                    case "5" -> autoRoster();
                    case "6" -> deleteRoster();
                    case "7" -> checkFullRoster();
                    case "8" -> rosterVisualization();
                    case "9" -> fileProcess();
                    default -> System.out.println("输入有误，请重新输入！");
                }
            }
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

    private static void setDate() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);

        System.out.println("#######################");
        System.out.println("#      排班日期设定     #");
        System.out.println("#######################");

        boolean flag = false;
        do {
            System.out.print("请输入起始日期（格式YYYY-MM-DD）：");
            String start = in.nextLine();
            if (start.equals("q"))
                return;
            System.out.print("请输入结束日期（格式YYYY-MM-DD）：");
            String end = in.nextLine();
            if (end.equals("q"))
                return;
            flag = DSet.setDate(start, end);
        } while (!flag);
    }

    private static void addEmployee() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);

        System.out.println("#######################");
        System.out.println("#      添加新的员工     #");
        System.out.println("#######################");

        boolean flag = false;
        do {
            System.out.print("请输入员工信息（格式name{duty,phone}）：");
            String info = in.nextLine();
            if (info.equals("q"))
                return;
            flag = DSet.addEmployee(info);
        } while (!flag);
    }

    private static void deleteEmployee() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);

        System.out.println("#######################");
        System.out.println("#      删除原有员工     #");
        System.out.println("#######################");

        boolean flag = false;
        do {
            System.out.print("请输入待删除员工名字：");
            String info = in.nextLine();
            if (info.equals("q"))
                return;
            flag = DSet.deleteEmployee(info);
        } while (!flag);
    }

    private static void manualRoster() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);

        System.out.println("#######################");
        System.out.println("#       手动排班       #");
        System.out.println("#######################");

        boolean flag = false;
        do {
            System.out.print("请输入排班员工名字：");
            String name = in.nextLine();
            if (name.equals("q"))
                return;
            System.out.print("请输入排班开始日期（格式YYYY-MM-DD）：");
            String sStart = in.nextLine();
            if (sStart.equals("q"))
                return;
            System.out.print("请输入排班结束日期（格式YYYY-MM-DD）：");
            String sEnd = in.nextLine();
            if (sEnd.equals("q"))
                return;
            flag = DSet.manualRosters(name, sStart, sEnd);
        } while (!flag);
    }

    private static void autoRoster() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#       自动排班       #");
        System.out.println("#######################");

        boolean flag = false;
        do {
            flag = DSet.autoRosters();
        } while (!flag);
    }

    private static void deleteRoster() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner in = new Scanner(System.in);

        System.out.println("#######################");
        System.out.println("#       删除排班       #");
        System.out.println("#######################");

        boolean flag = false;
        do {
            System.out.print("请输入待删除排班员工名字：");
            String name = in.nextLine();
            if (name.equals("q"))
                return;
            System.out.print("请输入该人该次排班开始日期（格式YYYY-MM-DD）：");
            String start = in.nextLine();
            if (start.equals("q"))
                return;
            flag = DSet.deleteRoster(name, start);
        } while (!flag);
    }

    private static void checkFullRoster() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#    检查排班是否已满    #");
        System.out.println("#######################");

        DSet.checkFullRoster();
    }

    private static void rosterVisualization() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#        可视化        #");
        System.out.println("#######################");

        DSet.rosterVisualization();
    }

    private static void fileProcess() throws IOException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("#######################");
        System.out.println("#       文件输入        #");
        System.out.println("#######################");

        parserInput.main(null);
    }
}
