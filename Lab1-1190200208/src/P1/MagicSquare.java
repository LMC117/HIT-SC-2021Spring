package P1;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MagicSquare {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 5; i++) {
            System.out.println(Integer.toString(i) + ".txt:");
            System.out.println(isLegalMagicSquare(i + ".txt"));
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要生成幻方方阵的行列数n");
        int n = sc.nextInt();
        boolean flag = generateMagicSquare(n);
        if (flag) {
            System.out.println(Integer.toString(6) + ".txt:");
            System.out.println(isLegalMagicSquare(6 + ".txt"));
        } else System.out.println(flag);
    }

    // 判断数表是否是幻方矩阵
    public static boolean isLegalMagicSquare(String fileName) throws IOException {
        // 读取文件
        File file = new File("src/P1/txt/" + fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        // 按行读取字符串
        String str;
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        while ((str = br.readLine()) != null) {
            arrayList.add(str);
        }
        br.close();
        isr.close();
        // 对ArrayList中存储的字符串进行处理
        int rows, cols;
        rows = arrayList.size(); // 确定矩阵的行数
        //存在空格，直接返回false
        for (int i = 0; i < rows; i++) {
            if (arrayList.get(i).contains(" ")) {
                System.out.print("数字之间并非完全使用\\t分割!");
                return false;
            }
        }
        //确认矩阵的列数
        int cols_temp;
        cols = arrayList.get(0).split("\t+").length; //切分，(多个)tab被视为分隔符
        for (int i = 1; i < rows; i++) {
            cols_temp = arrayList.get(i).split("\t+").length;
            //列数不统一，直接返回false
            if (cols_temp != cols) {
                System.out.print("列数不统一,并非矩阵!");
                return false;
            }
        }
        //行列数不一致，直接返回false
        if (rows != cols) {
            System.out.print("行列数不一致！");
            return false;
        }
        int n = rows;
        int[][] square = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                String s = arrayList.get(i).split("\t+")[j];
                //包含除数字外的其他字符，如负号，小数点，字母，则直接返回false
                Pattern pattern = Pattern.compile("[0-9]*");
                if (!pattern.matcher(s).matches()) {
                    System.out.print("不全是正整数!");
                    return false;
                }
                // 为square赋值
                square[i][j] = Integer.parseInt(s);
            }
        // 根据幻方的性质，判断其是否是幻方
        int rows_value = 0;
        int rows_value_temp = 0, cols_value_temp = 0, diagonals_value_temp = 0;
        // 以第0行元素和作为基准值
        for (int i = 0; i < n; i++) {
            rows_value += square[0][i];
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rows_value_temp += square[i][j];
                cols_value_temp += square[j][i];
                diagonals_value_temp += square[j][n - 1 - j];
            }
            if (rows_value_temp != rows_value || cols_value_temp != rows_value || diagonals_value_temp != rows_value) {
                System.out.print("不是幻方！");
                return false;
            } else {
                rows_value_temp = 0;
                cols_value_temp = 0;
                diagonals_value_temp = 0;
            }
        }
        return true;
    }

    public static boolean generateMagicSquare(int n) {
        // 处理异常情况
        if (n <= 0 || n % 2 == 0)
            return false;
        int magic[][] = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;
        // 对n*n个位置进行填充，分别填上1~n*n的值
        // 起始的填充位置是第一行的中间
        for (i = 1; i <= square; i++) {
            magic[row][col] = i;
            // 每填充n个元素，就下移一行，继续填充
            if (i % n == 0)
                row++;
            else {
                // 边界检查，若到达上边界，则跳至下边界；若到达右边界，则跳至左边界；
                if (row == 0)
                    row = n - 1;
                else
                    row--;
                if (col == (n - 1))
                    col = 0;
                else
                    col++;
            }
        }
        // 在屏幕上输出幻方矩阵
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }
        // 写入文件
        String txtPath = "src/P1/txt/6.txt";
        try {
            File writeName = new File(txtPath);
            writeName.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
            for (int k = 0; k < n; k++) {
                for (int l = 0; l < n; l++) {
                    String s = Integer.toString(magic[k][l]);
                    if (l != n - 1)
                        out.write(s + '\t');
                    else
                        out.write(s + '\n');
                }
            }
            out.close();
            System.out.println("文件创建成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
