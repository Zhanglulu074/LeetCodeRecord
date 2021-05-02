import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//有一种简易压缩算法：针对全部由小写英文字母组成的字符串，将其中连续超过两个相同字母的部分压缩为连续个数加该字母，其他
//部分保持原样不变。例如：字符串“aaabbccccd”经过压缩成为字符串“3abb4cd”。 请您编写解压函数，根据输入的字符串，判断其是否为合法压缩过的字符串，若输
//入合法则输出解压缩后的字符串，否则输出字符串“!error”来报告错误。
//输入描述：输入一行，为一个ASCII字符串，长度不会超过100字符，用例保证输出的字符串长度也不会超过100字符。
//输出描述：若判断输入为合法的经过压缩后的字符串，则输出压缩前的字符串；若输入不合法，则输出字符串“!error”。
//示例1：
//输入：4dff
//输出：ddddff

public class Main {
    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
//        String input = scan.nextLine();
        String input = "3a3b";
        int curNum = 0;
        char curChar = '_';
        //0 Ini
        //1 num
        //2 failed
        int curState = 0;
        StringBuilder sb = new StringBuilder();
        boolean isEnd = false;
        for (int j = 0; j < input.length(); j++) {
            char cur = input.charAt(j);
            switch (curState) {
                case 0:
                    if (Character.isLowerCase(cur)) {
                        if (cur == curChar) {
                            curNum++;
                            if (curNum >= 3) {
                                curState = 2;
                            }
                        } else {
                            curChar = cur;
                            curNum = 1;
                        }
                        sb.append(cur);
                        isEnd = true;
                    } else if (Character.isDigit(cur)) {
                        if (isEnd) {
                            curNum = 0;
                            isEnd = false;
                        }
                        curState = 1;
                        curNum = curNum * 10 + (cur - '0');
                    } else {
                        curState = 2;
                    }
                    break;
                case 1:
                    if (Character.isLowerCase(cur)) {
                        if (cur == curChar) {
                            curState = 2;
                            break;
                        }
                        curChar = cur;
                        for (int i = 0; i < curNum; i++) {
                            sb.append(curChar);
                        }
                        curState = 0;
                        isEnd = true;
                        if (curNum <= 2) {
                            curState = 2;
                        }
                    } else if (Character.isDigit(cur)) {
                        curNum = curNum * 10 + (cur - '0');
                    } else {
                        curState = 2;
                    }
                    break;
                case 2:
                    break;

            }
        }
        if (curState == 2 || curState == 1) {
            System.out.println("!error");
        } else {
            System.out.println(sb.toString());
        }
    }


}
