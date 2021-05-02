import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.tools.javadoc.Start;
import javafx.util.Pair;
import org.omg.PortableInterceptor.INACTIVE;
import sun.print.CUPSPrinter;
import sun.tools.jstack.JStack;
import sun.tools.jstat.JStatLogger;
import sun.tools.jstat.Jstat;

import javax.swing.plaf.IconUIResource;
import java.nio.file.Path;
import java.sql.Struct;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    //第一题：两数之和
    public static int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> searchMap = new HashMap<>();
        int index = 0;
        for (int num : nums) {
            searchMap.put(target - num, index);
            index++;
        }
        index = 0;
        Integer counterIndex = 0;
        int[] ret = new int[2];
        for (int num : nums) {
            counterIndex = searchMap.get(num);
            if (counterIndex != null && index != searchMap.get(num)) {
                ret[0] = index;
                ret[1] = counterIndex;
            }
            index++;
        }
        return ret;
    }

    //第二题，两数相加
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return getNextSumNode(l1, l2, 0);
    }

    public static ListNode getNextSumNode(ListNode l1, ListNode l2, int past) {
        if (l1 == null && l2 == null) {
            return past == 0 ? null : new ListNode(past);
        }
        int ret = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + past;
        ListNode retNode = new ListNode(0);
        int retPast = 0;
        if (ret > 9) {
            retPast = 1;
            ret -= 10;
        }
        retNode.val = ret;
        retNode.next = getNextSumNode(l1 == null ? null : l1.next, l2 == null ? null : l2.next, retPast);
        return retNode;
    }

    //第三题，无重复字符最长子串

    public static int lengthOfLongestSubstring(String s) {
        int startIndex = 0;
        int retLength = 0;
        int currentLength = 0;
        Map<Character, Integer> posMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean isContain = posMap.containsKey(c);
            if (!isContain || posMap.get(c) < startIndex) {
                currentLength++;
            } else {
                startIndex = posMap.get(c) + 1;
                currentLength = i - startIndex + 1;
            }
            posMap.put(c, i);
            retLength = Math.max(retLength, currentLength);
        }
        return retLength;
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int size1 = nums1.length;
        int size2 = nums2.length;
        int index1 = -1;
        int index2 = -1;
        int count = 0;
        int lastNum = 0;
        int currentNum = 0;
        boolean isOdd = (size1 + size2) % 2 == 1;
        int midIndex = (size1 + size2 + 1) / 2;
        while (index1 + 1 < size1 && index2 + 1 < size2) {
            lastNum = currentNum;
            if (nums1[index1 + 1] <= nums2[index2 + 1]) {
                currentNum = nums1[++index1];
            } else {
                currentNum = nums2[++index2];
            }
            count++;
            if (count == midIndex && isOdd) {
                return currentNum;
            }
            if (count == midIndex + 1 && !isOdd) {
                return (currentNum + lastNum) / 2.0;
            }
        }
        index1++;
        index2++;
        if (count == midIndex && !isOdd) {
            if (index1 < size1) {
                return (currentNum + nums1[index1]) / 2.0;
            } else {
                return (currentNum + nums2[index2]) / 2.0;
            }
        }
        if (index1 < size1) {
            int index = midIndex - count + index1 - 1;
            if (isOdd) {
                return nums1[index];
            } else {
                return (nums1[index] + nums1[index + 1]) / 2.0;
            }
        } else {
            int index = midIndex - count + index2 - 1;
            if (isOdd) {
                return nums2[index];
            } else {
                return (nums2[index] + nums2[index + 1]) / 2.0;
            }
        }
    }

    //第四题 最长回文子串长度
    //错误解答：其实是两个字符串最长公共子串的解法。
    public static String longestPalindrome1(String s) {
        StringBuilder builder = new StringBuilder(s);
        String s2 = builder.reverse().toString();
        int maxIndex = 0;
        int maxLength = 0;
        int length = s.length();
        int[][] dp = new int[length + 1][length + 1];
        for (int i = 0; i < length + 1; i++) {
            dp[0][i] = 0;
            dp[i][0] = 0;
        }
        for (int i = 1; i < length + 1; i++) {
            for (int j = 1; j < length + 1; j++) {
                if (s.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        maxIndex = i;
                    }
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        return s.substring(maxIndex - maxLength, maxIndex);
    }

    //正确解法：正宗动态规划
    public static String longestPalindrome(String s) {
        if (s.equals("")) {
            return "";
        }
        int endIndex = 0;
        int maxLength = 1;
        int length = s.length();
        boolean dp[][] = new boolean[length][length];
        for (int i = 0; i < length - 1; i++) {
            dp[i][i] = true;
            dp[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
            if (dp[i][i + 1]) {
                endIndex = i + 1;
                maxLength = 2;
            }
        }
        dp[length - 1][length - 1] = true;
        int i = 0;
        int j = 2;
        while (j < length) {
            for (i = 0; i + j < length; i++) {
                if (dp[i + 1][i + j - 1] && (s.charAt(i) == s.charAt(i + j))) {
                    dp[i][i + j] = true;
                    if (j >= maxLength) {
                        maxLength = j + 1;
                        endIndex = i + j;
                    }
                } else {
                    dp[i][i + j] = false;
                }
            }
            j++;
        }
        return s.substring(endIndex - maxLength + 1, endIndex + 1);
    }

    //第6题 Z字型变换
    public static String convert(String s, int numRows) {
        if (s.equals("")) {
            return "";
        }
        if (numRows == 1) {
            return s;
        }
        List<StringBuilder> list = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            list.add(new StringBuilder(""));
        }
        int lastRow = 0;
        boolean isLastDown = true;
        list.get(0).append(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            if (isLastDown && (lastRow < numRows - 1)) {
                list.get(lastRow + 1).append(s.charAt(i));
                lastRow++;
                isLastDown = true;
            } else if (isLastDown || lastRow > 0) {
                list.get(lastRow - 1).append(s.charAt(i));
                lastRow--;
                isLastDown = false;
            } else {
                list.get(lastRow + 1).append(s.charAt(i));
                lastRow++;
                isLastDown = true;
            }
        }
        StringBuilder ret = new StringBuilder();
        list.forEach(ret::append);
        return ret.toString();
    }

    //第七题 整数反转
    public static int reverse(int x) {
//        String s = "" + Math.abs(x);
//        boolean isNeg = x < 0;
//        StringBuffer sb = new StringBuffer(s);
//        s = sb.reverse().toString();
//        try {
//            int x1 = Integer.parseInt(s);
//            return isNeg ? -x1 : x1;
//        } catch (Exception e) {
//            return 0;
//        }
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) return 0;
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) return 0;
            rev = rev * 10 + pop;
        }
        return rev;
    }

    //第八题 整数解析
    //正则表达式笨逼解法
//    public static int myAtoi(String str) {
//        String regex = " *([+\\-]?)(\\d+).*";
//        if (!str.matches(regex)) {
//            return 0;
//        }
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(str);
//        int ret = 0;
//        if (m.find()) {
//            boolean isNegative = m.group(1) != null && m.group(1).equals("-");
//            try {
//                int num = Integer.parseInt(m.group(2));
//                ret = isNegative ? -num : num;
//            } catch (NumberFormatException e) {
//                ret = isNegative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//            }
//        }
//        return ret;
//    }

    private static final int START = 0;
    private static final int SIGN = 1;
    private static final int NUM = 2;
    private static final int END = 3;

    public int[][] table = {
            {START, SIGN, NUM, END},
            {END, END, NUM, END},
            {END, END, NUM, END},
            {END, END, END, END},
    };

    private int getCol(char c) {
        if (c == ' ') {
            return START;
        } else if (c == '+' || c == '-') {
            return SIGN;
        } else if (c - '0' <= 9 && c - '0' >= 0) {
            return NUM;
        } else {
            return END;
        }
    }

    //有限状态机解法
    public int myAtoi(String str) {
        int curState = START;
        long ret = 0;
        boolean isNeg = false;
        for (char c : str.toCharArray()) {
            int col = getCol(c);
            int transition = table[curState][col];
            if (transition == END) {
                break;
            } else if (transition == SIGN) {
                isNeg = c == '-';
            } else if (transition == NUM) {
                int plus = c - '0';
                if (!isNeg && (ret * 10 + plus) > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
                if (isNeg && ret * 10 + plus > -(Integer.MIN_VALUE + 1)) {
                    return Integer.MIN_VALUE;
                }
                ret = ret * 10 + plus;
            }
            curState = transition;
        }
        return (int) (isNeg ? -ret : ret);
    }

    //第九题 回文数
    //笨逼解法
    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        if (x < 10) {
            return true;
        }
        int size = String.valueOf(x).length();
        int mode = (int) Math.pow(10.0, (float) (size / 2));
        int highNum = x / mode;
        int lowNum = x % mode;
        if (size % 2 != 0) {
            highNum /= 10;
        }
        int testNum = 0;
        while (highNum > 0) {
            int flag = highNum % 10;
            highNum /= 10;
            testNum = testNum * 10 + flag;
        }
        return testNum == lowNum;
    }

    //牛逼解法
    public boolean isPalindrome1(int x) {
        if (x < 0) {
            return false;
        }
        if (x % 10 == 0 && x != 0) {
            return false;
        }
        int test = 0;
        while (x > test) {
            int flag = x % 10;
            test = test * 10 + flag;
            x = x / 10;
        }
        return x == test || x == test / 10;
    }

    //开个小差，0-1背包问题。
    public int simplePack(List<Pair<Integer, Integer>> list, int capacity) {
        int ret = 0;
        int n = list.size();
        int[][] dp = new int[n][capacity];
        for (int i = 0; i < n; i++) dp[i][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < capacity; j++) {
                int weight = list.get(i).getKey();
                int value = list.get(i).getValue();
                if (weight > value) {
                    dp[i][j] = this.geDp(dp, i - 1, j);
                } else {
                    dp[i][j] = Math.max(this.geDp(dp, i - 1, j - weight) + value, this.geDp(dp, i - 1, j));
                }
                if (dp[i][j] >= ret) {
                    ret = dp[i][j];
                }
            }
        }
        return ret;
    }

    public int geDp(int[][] dp, int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        } else {
            return dp[i][j];
        }
    }


    //11.盛水最多的容器
    public int maxArea(int[] height) {
        int ret = 0;
        int start = 0;
        int end = height.length - 1;
        int left = 0;
        int right = 0;
        while (start < end) {
            left = height[start];
            right = height[end];
            ret = Math.max(Math.min(left, right) * (end - start), ret);
            if (left <= right) {
                start++;
            } else {
                end--;
            }
        }
        return ret;
    }

    //12. 整数转罗马数字

    public String intToRoman(int num) {
        List<Integer> mods = Arrays.asList(1000, 500, 100, 50, 10, 5, 1);
        List<Character> flags = Arrays.asList('M', 'D', 'C', 'L', 'X', 'V', 'I');
        int i = 0;
        int offset = 2;
        StringBuilder builder = new StringBuilder();
        while (num > 0 && i < mods.size()) {
            int mod = mods.get(i);
            char flag = flags.get(i);
            int cur = num / mod;
            int left = num % mod;
            offset = i % 2 == 1 ? (i + 1) : offset;
            int test = num / mods.get(offset);
            if (i >= 1 && test == (mods.get(i - 1) - mods.get(offset)) / mods.get(offset)) {
                builder.append(flags.get(offset)).append(flags.get(i - 1));
                num -= (mods.get(i - 1) - mods.get(offset));
                i += 1;
                continue;
            }
            for (int j = 0; j < cur; j++) {
                builder.append(flag);
            }
            num = left;
            i++;
        }
        return builder.toString();
    }


    //13：罗马转数字（原来用Map,后来看大神用switch快这么多）
    public int romanToInt(String s) {
        int size = s.length();
        int ret = 0;
        for (int i = size - 1; i >= 0; i--) {
            char c1 = s.charAt(i);
            int plus = getValue(c1);
            if (i > 0) {
                char c2 = s.charAt(i - 1);
                int plus2 = getValue(c2);
                if (plus2 < plus) {
                    plus -= plus2;
                    i--;
                }
            }
            ret += plus;
        }
        return ret;
    }

    private int getValue(char ch) {
        switch (ch) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

    //14: 最长公共前缀(看起来这个东西确实是没什么特别简单的办法了😂)
    public String longestCommonPrefix(String[] strs) {
        int index = 0;
        char base;
        int i = 0;
        if (strs.length == 0) {
            return "";
        }
        while (index < strs[0].length()) {
            base = strs[0].charAt(index);
            boolean shouldBreak = false;
            for (i = 0; i < strs.length; i++) {
                String s = strs[i];
                if (index >= s.length() || s.charAt(index) != base) {
                    shouldBreak = true;
                    break;
                }
            }
            if (shouldBreak) {
                break;
            }
            index++;
        }
        return strs[0].substring(0, index);
    }

    //15 三数之和

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> retList = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return retList;
        }
        Arrays.sort(nums);
        Map<Integer, Integer> map = new HashMap<>();
        Set<Integer> targetSet = new HashSet<>();
        Set<Integer> testSet = new HashSet<>();
        for (int i = 0; i <= nums.length - 3; i++) {
            map.clear();
            testSet.clear();
            int target = -nums[i];
            if (targetSet.contains(target)) {
                continue;
            }
            for (int j = i + 1; j < nums.length; j++) {
                map.put(target - nums[j], j);
            }
            for (int j = i + 1; j < nums.length; j++) {
                if (map.containsKey(nums[j])) {
                    int z = map.get(nums[j]);
                    if (z > j && !testSet.contains(nums[z])) {
                        retList.add(Arrays.asList(nums[i], nums[j], nums[z]));
                        testSet.add(nums[z]);
                    }
                }
            }
            targetSet.add(target);
        }
        return retList;
    }

    //20. 有效的括号
    public boolean isValid(String s) {
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (stack.size() == 0) {
                    return false;
                } else {
                    char temp = stack.pop();
                    if ((temp == '{' && c == '}')
                            || (temp == '(' && c == ')')
                            || (temp == '[' && c == ']')) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
        }
        return stack.size() == 0;
    }

    //20. 有效的括号(聪明做法)
    //这里的改进指出在于存进stack的不是原字符，而是对应的成对字符，这样比较的代码可以写得比较简洁。
    public boolean isValid1(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '{') {
                stack.push('}');
            } else if (c == '[') {
                stack.push(']');
            } else if (c == '(') {
                stack.push(')');
            } else {
                if (stack.isEmpty() || stack.pop() != c) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    //22 括号生成
    public List<String> generateParenthesis(int n) {
        List<String> ret = new ArrayList<>();
        if (n == 0) {
            return ret;
        }
        addList(1, 2 * n, "(", ret);
        return ret;
    }

    public void addList(int index, int n, String s, List<String> ret) {
        index++;
        if (index <= n) {
            addList(index, n, s + "(", ret);
            addList(index, n, s + ")", ret);
        }

        if (index > n) {
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                if (count < 0 || count > n / 2) {
                    break;
                }
                if (s.charAt(i) == '(') {
                    count++;
                } else if (s.charAt(i) == ')') {
                    count--;
                }
            }
            if (count == 0) {
                ret.add(s);
            }
        }
    }

    //17. 电话号码组合
    public List<String> letterCombinations(String digits) {
        List<String> retList = new ArrayList<>();
        if (digits.length() == 0) {
            return retList;
        }
        for (int i = 0; i < testList.get(digits.charAt(0) - '0' - 2).size(); i++) {
            getLetters(digits, 0, retList, i, "");
        }
        return retList;
    }

    private void getLetters(String digits, int index, List<String> retList, int charIndex, String currentRet) {
        currentRet += ("" + getChar(digits, index, charIndex));
        if (index + 1 < digits.length()) {
            int nextIndex = digits.charAt(index + 1) - '0';
            List<Character> nextList = testList.get(nextIndex - 2);
            for (int i = 0; i < nextList.size(); i++) {
                getLetters(digits, index + 1, retList, i, currentRet);
            }
        }
        if (currentRet.length() == digits.length()) {
            retList.add(currentRet);
        }
    }

    List<List<Character>> testList = new ArrayList<List<Character>>() {
        {
            add(Arrays.asList('a', 'b', 'c'));
            add(Arrays.asList('d', 'e', 'f'));
            add(Arrays.asList('g', 'h', 'i'));
            add(Arrays.asList('j', 'k', 'l'));
            add(Arrays.asList('m', 'n', 'o'));
            add(Arrays.asList('p', 'q', 'r', 's'));
            add(Arrays.asList('t', 'u', 'v'));
            add(Arrays.asList('w', 'x', 'y', 'z'));
        }
    };

    private char getChar(String digits, int index, int charIndex) {
        return testList.get(digits.charAt(index) - '0' - 2).get(charIndex);
    }

    //26. 删除排序数组中的重复项
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int retIndex = 0;
        int past = 0;
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            past = nums[retIndex];
            if (cur > past) {
                retIndex++;
                nums[retIndex] = cur;
            }
        }
        return retIndex + 1;
    }

    //19:删除链表的倒数第n个节点
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode endHead = head;
        ListNode startHead = head;
        ListNode tempNode = head;
        int index = 0;
        boolean startTracking = false;
        while (endHead.next != null) {
            if (index >= n - 1) {
                startTracking = true;
            }
            if (startTracking) {
                tempNode = startHead;
                startHead = startHead.next;
            }
            endHead = endHead.next;
            index++;
        }
        tempNode.next = startHead.next;
        if (startTracking) {
            return head;
        } else {
            return startHead.next;
        }
    }

    //23. 合并n个升序列表
    public ListNode mergeKLists(ListNode[] lists) {
        ListNode ret = null;
        int min = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < lists.length; i++) {
            ListNode node = lists[i];
            if (node == null) {
                continue;
            }
            if (node.val < min) {
                min = node.val;
                minIndex = i;
            }
        }
        if (minIndex != -1) {
            ret = lists[minIndex];
            lists[minIndex] = ret.next;
            ret.next = mergeKLists(lists);
        }
        return ret;
    }


    //24. 两两交换链表中的节点
    public ListNode swapPairs(ListNode head) {
        return swapNodes(head);
    }

    public ListNode swapNodes(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode next = head.next;
        ListNode newHead = next.next;
        next.next = head;
        head.next = swapNodes(newHead);
        return next;
    }

    //25. K个一组反转链表
    public ListNode reverseKGroup(ListNode head, int k) {
        return swapNodesForGroup(head, k);
    }

    public ListNode swapNodesForGroup(ListNode head, int count) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode temp = head.next;
        ListNode cur = head;
        ListNode next = head.next;
        ListNode prev = null;
        int i = 0;
        for (; i < count - 1; i++) {
            if (temp == null) {
                break;
            }
            next = temp;
            temp = next.next;
            next.next = cur;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        if (i != count - 1) {
            return swapNodesForGroup(cur, i + 1);
        } else {
            head.next = swapNodesForGroup(temp, count);
            return cur;
        }
    }

    private ListNode reverseListNodes(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        int i = 0;
        ListNode next = null;
        ListNode prev = null;
        ListNode cur = head;
        ListNode temp = head.next;
        while (temp != null) {
            next = temp;
            temp = next.next;
            next.next = cur;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return cur;
    }

    // 27. 移除元素
    public int removeElement(int[] nums, int val) {
        if (nums == null) {
            return 0;
        }
        int retIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            if (cur != val) {
                nums[retIndex] = cur;
                retIndex++;
            }
        }
        return retIndex;
    }

    //31. 下一个排列
    public void nextPermutation(int[] nums) {
        if (nums == null || nums.length == 1) {
            return;
        }
        boolean isReverse = true;
        int target = nums.length - 2;
        while (target >= 0) {
            int head = nums[target + 1];
            if (nums[target] < head) {
                isReverse = false;
                break;
            }
            target--;
        }
        if (!isReverse) {
            int targetValue = nums[target];
            for (int i = target + 1; i <= nums.length - 1; i++) {
                if (i == nums.length - 1 || (targetValue <= nums[i] && targetValue >= nums[i + 1])) {
                    int tempValue = nums[i];
                    nums[i] = nums[target];
                    nums[target] = tempValue;
                    break;
                }
            }
        }
        int start = target + 1;
        int end = nums.length - 1;
        while (start < end) {
            int temp = nums[end];
            nums[end] = nums[start];
            nums[start] = temp;
            start++;
            end--;
        }
    }

    //28. 实现 strStr()
    public int strStr(String haystack, String needle) {
        if (haystack == null || needle == null || needle.length() > haystack.length()) {
            return -1;
        }
        if (needle.equals("")) {
            return 0;
        }
        char mode = needle.charAt(0);
        int i;
        for (i = 0; i < haystack.length(); i++) {
            char c = haystack.charAt(i);
            if (c == mode && i + needle.length() <= haystack.length()) {
                if (haystack.substring(i, (i + needle.length())).equals(needle)) {
                    break;
                }
            }
        }
        return i > (haystack.length() - needle.length()) ? -1 : i;
    }

    //10.正则表达式
//    public boolean isMatch(String s, String p) {
//        if (s == null || p == null) {
//            return false;
//        }
//        int m = s.length();
//        int n = p.length();
//        if (m == 0 || n == 0) {
//            return false;
//        }
//        boolean[][] ret = new boolean[m + 1][n + 1];
////        for (int i = 0; i <= m; i++) {
////            ret[i][0] = true;
////        }
////        for (int i = 0; i <= n; i++) {
////            ret[0][i] = true;
////        }
//        ret[0][0] = true;
//        for (int i = 1; i <= m; i++) {
//            for (int j = 1; j <= n; j++) {
//                if (p.charAt(j - 1) != '*') {
//                    if (matches(s.charAt(i - 1), p.charAt(j - 1))) {
//                        ret[i][j] = ret[i - 1][j - 1];
//                    } else {
//                        ret[i][j] = false;                  }
//                } else {
//                    if (matches(s.charAt(i - 1), p.charAt(j - 2))) {
//                        ret[i][j] = ret[i - 1] [j];
//                    } else {
//                        ret[i][j] = ret[i][j - 2];
//                    }
//                }
//            }
//        }
//        return ret[m][n];
//    }
//
//    boolean matches(char c, char p) {
//        return p == '.' || (c == p);
//    }

    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();

        boolean[][] f = new boolean[m + 1][n + 1];
        f[0][0] = true;
        for (int i = 0; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (p.charAt(j - 1) == '*') {
                    if (j - 2 >= 0) {
                        f[i][j] = f[i][j - 2];
                    }
                    if (matches(s, p, i, j - 1)) {
                        //这里注意("a", "aa*"的用例)
                        f[i][j] = f[i][j] || f[i - 1][j];
//                        f[i][j] = f[i - 1][j];
                    }
                } else {
                    if (matches(s, p, i, j)) {
                        f[i][j] = f[i - 1][j - 1];
                    }
                }
            }
        }
        return f[m][n];
    }

    public boolean matches(String s, String p, int i, int j) {
        if (i == 0 || j == 0) {
            return false;
        }
        if (p.charAt(j - 1) == '.') {
            return true;
        }
        return s.charAt(i - 1) == p.charAt(j - 1);
    }

    /*---------------------------位运算章节---------------------------*/
    /*---------------------------异或---------------------------*/
    // 136 :只出现一次的数字
    public int singleNumber(int[] nums) {
        int ret = 0;
        for (int i = 0; i < nums.length; i++) {
            ret ^= nums[i];
        }
        return ret;
    }

    // 389: 找不同
    public char findTheDifference(String s, String t) {
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            ret ^= s.charAt(i);
        }
        for (int i = 0; i < t.length(); i++) {
            ret ^= t.charAt(i);
        }
        return (char) (ret);
    }

    /*---------------------------n&(n-1)---------------------------*/
    //191 位1的个数
    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);
            count++;
        }
        return count;
    }

    /*---------------------------n&1---------------------------*/
    public int hammingWeight1(int n) {
        int count = 0;
        while (n != 0) {
            if ((n & 1) != 0) {
                count++;
            }
            n = n >>> 1;
        }
        return count;
    }

    //318. 最大单词长度乘积
    public int maxProduct(String[] words) {
        int[] targets = new int[words.length];
        Map<Integer, Integer> resMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            String s = words[i];
            int val = 0;
            for (int j = 0; j < s.length(); j++) {
                val |= (1 << (s.charAt(j) - 'a'));
            }
            targets[i] = val;
            if (resMap.get(val) == null || resMap.get(val) <= s.length()) {
                resMap.put(val, s.length());
            }
        }
        int max = 0;
        for (int i : resMap.keySet()) {
            for (int j : resMap.keySet()) {
                if ((i & j) == 0 && resMap.get(i) * resMap.get(j) >= max) {
                    max = resMap.get(i) * resMap.get(j);
                }
            }
        }
        return max;
    }

    // 190. 颠倒二进制位
    public int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            int x = n & 1;
            n = n >>> 1;
            res |= x << (31 - i);
        }
        return res;
    }

    // 78. 子集
    public List<List<Integer>> subsets(int[] nums) {
        int sum = 1 << nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1; i < sum; i++) {
            List<Integer> curRes = new ArrayList<>();
            int k = 1;
            int count = 0;
            while (k <= i) {
                if ((k & i) != 0) {
                    curRes.add(nums[count]);
                }
                k = k << 1;
                count++;
            }
            res.add(curRes);
        }
        res.add(new ArrayList<>());
        return res;
    }


    /*---------------------------数学题---------------------------*/
    /*---------------------------数学题---------------------------*/
    /*---------------------------开方---------------------------*/
    //367. 有效平方数(二分查找)
    public boolean isPerfectSquare(int num) {
        if (num < 0) {
            return false;
        }
        if (num == 0 || num == 1) {
            return true;
        }
        int low = 1;
        int high = num;
        while (low <= high) {
            long mid = low + (high - low) / 2;
            if (mid * mid == num) {
                return true;
            }
            if (mid * mid > num) {
                high = (int) mid - 1;
            } else if (mid * mid < num) {
                low = (int) mid + 1;
            }
        }
        return false;
    }

    //367. 牛顿法(二分查找)
    public boolean isPerfectSquare1(int num) {
        if (num < 0) {
            return false;
        }
        if (num == 0 || num == 1) {
            return true;
        }
        long res = num;
        while (res * res > num) {
            res = (res + num / res) / 2;
            if (res * res == num) {
                return true;
            }
        }
        return false;
    }


    /*---------------------------练习题---------------------------*/
    // 8. 字符串转数字（非状态机解法）
    public int myAtoi1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        s = s.trim();
        int sign = 1;
        int start = -1;
        long res = 0;
        char initail = s.charAt(0);
        if (initail == '-') {
            sign = -1;
            start = 1;
        } else if (initail == '+') {
            sign = 1;
            start = 1;
        } else if (Character.isDigit(initail)) {
            start = 0;
        }
        if (start < 0 || start > s.length() - 1) {
            return 0;
        }
        while (start <= s.length() - 1 && Character.isDigit(s.charAt(start))) {
            int tmp = s.charAt(start) - '0';
            res = res * 10 + tmp;
            if (sign > 0 && res > Integer.MAX_VALUE) {
                return 0;
            }
            if (sign < 0 && res - 1 > Integer.MAX_VALUE) {
                return 0;
            }
            start++;
        }
        return sign * (int) res;
    }

    // 67. 二进制求和(我的垃圾答案)
    public String addBinary(String a, String b) {
        char[] res = new char[Math.max(a.length(), b.length())];
        String longStr = a.length() >= b.length() ? a : b;
        String shortStr = a.length() >= b.length() ? b : a;
        int offset = Math.abs(a.length() - b.length());
        boolean join = false;
        for (int i = longStr.length() - 1; i >= 0; i--) {
            int si = i - offset >= 0 ? shortStr.charAt(i - offset) - '0' : 0;
            int li = longStr.charAt(i) - '0';
            int tmp = si + li + (join ? 1 : 0);
            if (tmp > 1) {
                res[i] = (char) ('0' + (tmp - 2));
                join = true;
            } else {
                res[i] = (char) ('0' + tmp);
                join = false;
            }
        }
        String s = new String(res);
        if (join) {
            s = "1" + s;
        }
        return s;
    }

    // 67. 二进制求和(标准好答案)
    public String addBinary2(String a, String b) {
        StringBuffer ans = new StringBuffer();

        int n = Math.max(a.length(), b.length()), carry = 0;
        for (int i = 0; i < n; ++i) {
            carry += i < a.length() ? (a.charAt(a.length() - 1 - i) - '0') : 0;
            carry += i < b.length() ? (b.charAt(b.length() - 1 - i) - '0') : 0;
            ans.append((char) (carry % 2 + '0'));
            carry /= 2;
        }

        if (carry > 0) {
            ans.append('1');
        }
        ans.reverse();

        return ans.toString();
    }

    // 258. 各位相加
    public int addDigits(int num) {
        if (num < 10) {
            return num;
        }
        int res = 0;
        while (num >= 10) {
            res = 0;
            while (num > 0) {
                res += (num % 10);
                num /= 10;
            }
            num = res;
        }
        return res;
    }


    //43. 字符串相乘
    public String multiply(String num1, String num2) {
        int m = num1.length(), n = num2.length();
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        StringBuffer res = new StringBuffer("0");
        for (int i = n - 1; i >= 0; i--) {
            int c = num2.charAt(i) - '0';
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < n - 1 - i; j++) {
                buffer.append("0");
            }
            int next = 0;
            for (int j = m - 1; j >= 0; j--) {
                int d = num1.charAt(j) - '0';
                int mult = d * c + next;
                buffer.append(mult % 10);
                next = mult / 10;
            }
            if (next != 0) {
                buffer.append(next);
            }
            addString(res, buffer.reverse().toString());
        }
        return res.toString();
    }

    private void addString(StringBuffer origin1, String add) {
        String origin = origin1.toString();
        int n = Math.max(origin.length(), add.length());
        origin1.setLength(0);
        int carry = 0;
        for (int i = 0; i < n; i++) {
            int ai = origin.length() - 1 >= i ? origin.charAt(origin.length() - 1 - i) - '0' : 0;
            int bi = add.length() - 1 >= i ? add.charAt(add.length() - 1 - i) - '0' : 0;
            int tmp = ai + bi + carry;
            origin1.append(tmp % 10);
            carry = tmp / 10;
        }
        if (carry != 0) {
            origin1.append(carry);
        }
        origin1.reverse();
    }

    //29. 两数相除
    public int divide(int dividend, int divisor) {
        long res = 0;
        int sign = 1;
        if (dividend >= 0) {
            dividend = -dividend;
            sign = -sign;
        }
        if (divisor > 0) {
            divisor = -divisor;
            sign = -sign;
        }
        while (dividend <= divisor) {
            long f = 1;
            int tmp = divisor;
            while (dividend - tmp <= tmp) {
                tmp <<= 1;
                f <<= 1;
            }
            dividend -= tmp;
            res += f;
        }
        if (sign > 0 && res > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return sign > 0 ? (int) res : (int) (-res);
    }

    public int divide1(int dividend, int divisor) {
        //注意这个由于int的最大值和最小值绝对值差1导致的特殊情况，需要特殊处理
        if (dividend == Integer.MIN_VALUE && divisor == -1)
            return Integer.MAX_VALUE;

        boolean k = (dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0);
        int result = 0;
        dividend = -Math.abs(dividend);
        divisor = -Math.abs(divisor);
        while (dividend <= divisor) {
            int temp = divisor;
            int c = 1;
            while (dividend - temp <= temp) {
                temp = temp << 1;
                c = c << 1;
            }
            dividend -= temp;
            result += c;
        }
        return k ? result : -result;
    }

    // 69. x的平方根(二分)
    public int mySqrt(int x) {
        if (x <= 0) {
            return 0;
        }
        int low = 0;
        int high = x;
        int target = x;
        int ans = 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if ((long) mid * mid == target) {
                return mid;
            }
            if ((long) mid * mid <= target) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;

            }
        }
        return ans;
    }

    public int mySqrt1(int x) {
        int l = 0, r = x, ans = -1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if ((long) mid * mid <= x) {
                ans = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return ans;
    }

    public int mySqrt2(int x) {
        long target = x;
        while ((long) target * target > x) {
            target = (target + x / target) / 2;
        }
        return (int) target;
    }

    //50. Pow(即计算x的n次幂，这个题的关键在于将循环*n次的时间缩短，使用类似二分的思想)
    public double myPow(double x, int n) {
        double res = 1;
        long t = n;
        if (n < 0) {
            x = 1.0 / x;
            t = -t;
        }
        double target = x;
        while (t > 0) {
            if (t % 2 == 1) {
                res = res * target;
            }
            target = target * target;
            t /= 2;
        }
        return res;
    }


    /*---------------------------数组题---------------------------*/
    /*---------------------------数组题---------------------------*/


    /*---------------------------单向双指针---------------------------*/
    // 252. 会议室(要点在与先按照start排序，而后比较每个区间的start和上一个区间的end的大小关系，这里可以这么思维，要想会议不冲突，
    // 则排序以后每一长会议的start都必须大于等于上一长会议的end)
    public boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (x, y) -> x[0] - y[0]);
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i - 1][1] > intervals[i][0]) {
                return false;
            }
        }
        return true;
    }

    // 253. 会议室2
    // 这里的要点课上讲的是扫描线算法思想，我这边自己的理解在于，首先将会议开始、结束时间均排序
    // 然后设想这么一场景，每一个人在会议马上开始时到达时，需要去找一个最早的会议结束时间，然后
    // 进行判断，如果最接近结束的会议此时已经结束，则使用那个会议室（即不需要重开会议室），但是
    // 如果接近的会议仍然没有结束，则需要新开一个会议室。
    public int minMeetingRooms(int[][] intervals) {
        int[] starts = new int[intervals.length];
        int[] ends = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int end = 0;
        int res = 0;
        for (int i = 0; i < starts.length; i++) {
            if (starts[i] < ends[end]) {
                res++;
            } else {
                end++;
            }
        }
        return res;
    }

    // 209. 长度最小的子数组（子数组是指连续）
    public int minSubArrayLen(int s, int[] nums) {
        int res = Integer.MAX_VALUE;
        int sum = 0;
        int left = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            while (left <= i && sum >= s) {
                res = Math.min(res, i - left + 1);
                sum -= nums[left++];
            }
        }
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    //238. 除自身以外数组的乘积
    //这个题要求将int数组的每个元素转化为原数组中除它自己以外元素的乘积，这样就很容易想到前缀和*后
    //缀和
    //这个题的逻辑其实可以理解为先正向乘一遍，算出一个前缀积数组，而后再反向来一边，来一个后缀积数组
    //而后两个数组元素两两相乘即可得到结果，不过这样会有三个for循环，仔细研究一下，我们会发现，其实
    //计算后缀积的时候，没算完一个，可以直接乘上原来记录的前缀积得到结果，因此可以省去第二次for循环
    //直接得到结果。
    public int[] productExceptSelf(int[] nums) {
        if (nums == null || nums.length == 1) {
            return nums;
        }
        int[] res = new int[nums.length];
        res[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            res[i] = nums[i - 1] * res[i - 1];
        }
        int temp = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            temp *= nums[i + 1];
            res[i] *= temp;
        }
        return res;
    }

    /*---------------------------双向双指针---------------------------*/
    //11. 盛水最多的容器
    //这个题用双指针不难想到，但最关键的一点是左右指针推进的策略，这里简单概括就是：
    //永远有限推较短的那一边！！
    //永远有限推较短的那一边！！
    //永远有限推较短的那一边！！
    //重要的事情说三遍。
    public int maxArea1(int[] height) {
        int l = 0;
        int r = height.length - 1;
        int res = 0;
        while (l < r) {
            res = Math.max(Math.min(height[l], height[r]) * (r - l), res);
            if (height[l] < height[r]) {
                l++;
            } else {
                r--;
            }
        }
        return res;
    }

    //277. 搜寻名人
    //该题目大意为提供一个knows(int a, int b)的api用于判断a是否认识b，而后要求找出一个所有人都认识
    //但不认识所有人的人的编号(名人)
    //这里的题解巧妙的利用遍历的方式排除，
    public int findCelebrity(int n) {
        int res = 0;
        for (int i = 1; i < n; i++) {
            //若res认识i,则说明res非名人，i有可能为名人，重新赋值
            if (knows(res, i)) {
                res = i;
            }
        }
        //到这一部完成，实际上以及完成了排除法，要么res是名人，要们这个群体里边没有名人
        for (int i = 0; i < n; i++) {
            //继续排除res不是名人的可能性，如果res认识任何一个人，或者任何一个人不认识res，则res非名人
            //该群体没有名人。
            if (res != i && (knows(res, i) || !knows(i, res))) {
                return -1;
            }
        }
        return res;
    }

    // 75. 颜色分类(暴力版本)
    //直接采用一次基数排序+二次重排的方式解决
//    public void sortColors(int[] nums) {
//        if (nums == null || nums.length == 1) {
//            return;
//        }
//        int[] res = new int[nums.length];
//        for (int num : nums) {
//            res[num]++;
//        }
//        int start = 0;
//        for (int i = 0; i < res.length; i++) {
//            if (res[i] == 0) continue;
//            for (int j = start; j <= start + res[i] - 1; j++) {
//                nums[j] = i;
//            }
//            start += res[i];
//        }
//    }

    // 75. 颜色分类(进阶版本)
    //采用双指针加一次遍历交换的方式进行
    //基本思路为，遇0移动至头部，遇1移动至尾部，这里存在的问题在于，仅有一次遍历时，需要保证指针扫过的区域维持正确的排序
    //（因为后续的交换可能影响这部分区域。）
    //0移动至头部策略维持不变，关键在于2交换至尾部后，需要判定交换过来的数字：
    //1. 为1则无需处理
    //2. 为2则继续交换，直至不为2
    //3. 为0则继续交换至头部
    //4. 双指针相遇则循环结束。
    public void sortColors(int[] nums) {
        if (nums == null || nums.length == 1) {
            return;
        }
        int head = 0;
        int tail = nums.length - 1;
        for (int i = 0; i <= tail; i++) {
            int tmp;
            while (i <= tail && nums[i] == 2) {
                tmp = nums[i];
                nums[i] = nums[tail];
                nums[tail] = tmp;
                tail--;
            }
            if (nums[i] == 0) {
                tmp = nums[i];
                nums[i] = nums[head];
                nums[head] = tmp;
                head++;
            }
        }
    }

    //283. 移动零(我的思路)
    //我的思路是使用一个count变量记录当前0，这样可以直接将当前非0数前移count个为止即可到达正确为止，、
    //最后末位count个为止补0即可。
//    public void moveZeroes(int[] nums) {
//        int count = 0;
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] != 0) {
//                nums[i - count] = nums[i];
//            } else {
//                count ++;
//            }
//        }
//        for (int i = nums.length - 1; i >= nums.length - count ; i--) {
//            nums[i] = 0;
//        }
//    }

    //283. 移动零(优化思路)
    //优化思路采用一个单指针j执行重排后最后一个不为0的位置，如此开始默认为0，而后每遇到一个非0的数字就向上
    //加即可，最后直接从j开始直接向末位补0即可。
    //这两种思路其实从时间复杂度来讲没有本质的区别，但是显然优化思路代码更为简单清晰。
    public void moveZeroes(int[] nums) {
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[j++] = nums[i];
            }
        }
        while (j < nums.length) {
            nums[j++] = 0;
        }
    }

    //80. 移除数组中重复的元素2
    public int removeDuplicates2(int[] nums) {
        int left = 0;
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[left]) {
                nums[++left] = nums[i];
                count = 1;
            } else {
                if (count < 2) {
                    nums[++left] = nums[i];
                }
                count++;
            }
        }
        return left + 1;
    }

    //56. 合并区间
    //这个题算是有点意思，我自己解决的过程中是用了一些单指针+区间重叠判断的思想
    //总体收看是依据起点排序，而后从顺序遍历，遍历过程使用单指针指示当前处理完毕的节点
    //遍历过程中，不断比较指针指向位置与当前遍历节点是否重叠，若不重叠，则指针右移，将当前节点内容移动至指针节点处
    //若重叠，则指针不动，将指针处原区间扩充至覆盖当前节点区间。
    //最后，将原数组至指针处截断复制即可
    //遗漏点，最开始没有考虑到两个区间重叠时，可能存在前一个区间完全覆盖后一个区间的情况
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return intervals;
        }
        Arrays.sort(intervals, (x, y) -> x[0] - y[0]);
        int cur = 0;
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] > intervals[cur][1]) {
                intervals[++cur][0] = intervals[i][0];
                intervals[cur][1] = intervals[i][1];
            } else {
                intervals[cur][1] = Math.max(intervals[i][1], intervals[cur][1]);
            }
        }
        int[][] res = Arrays.copyOf(intervals, cur + 1);
        return res;
    }


    public boolean knows(int a, int b) {
        return false;
    }

    //57. 插入区间（开心，hard一遍过)
    //这个题最开始走了一些弯路，后来才发现要换一种思维方式来看这个东西
    //其实从最后的处理结果来看，我们最终结果是由三部分构成，
    //1. 插入节点处理后，节点之前的部分(可能为空)，这部分由原区间组成。
    //2. 插入节点(包含重叠区间进行并集之后的部分)
    //3. 插入节点之后的部分(可能为空)，这部分由原区间组成。
    //明确了上面这些东西以后，我们整个算法只需要拿到是三个东西就可以了。
    //1. start指针，指向第1部分的结尾节点
    //2. 进行重叠并集处理之后的新区间
    //3. end指针，指向第3部分的开始节点。
    //上面这种思维的好处在于，简化了整个问题流程，不需要再考虑各种特殊情况(如新区间插在队头，插在队尾，插在队中)带来的新数组长度的变化
    //这样问题转化之后其实就很简单了。
    public int[][] insert(int[][] intervals, int[] newInterval) {
        if (intervals == null || newInterval == null || newInterval.length == 1) {
            return intervals;
        }
        int cur = 0;
        int start = 0;
        //首先计算按照开始时间，新区间所处的位置
        for (int i = 0; i < intervals.length; i++) {
            if (newInterval[0] >= intervals[i][0]) {
                cur++;
            }
        }
        //start指针求解，这个分三种情况就行
        if (cur == 0) {
            start = -1;
        } else if (newInterval[0] > intervals[cur - 1][1]) {
            start = cur - 1;
        } else {
            start = cur - 2;
        }
        //end初始化为start指针的后一位
        int end = start + 1;
        //end指针求解+新区间重叠处理求解
        for (int i = start + 1; i < intervals.length; i++) {
            //一旦不再重叠，则这之后的区间都维持原状，无需处理。
            if (newInterval[1] < intervals[i][0]) {
                break;
            } else {
                //若存在重叠，则进行并集处理，同时end指针右移
                newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
                newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
                end++;
            }
        }
        //新区间长度，这个公式在纸上边写一写就很清楚了
        int[][] res = new int[intervals.length + start - end + 2][2];
        //按部就班，复制到新数组就可以了。
        for (int i = 0; i <= start; i++) {
            res[i][0] = intervals[i][0];
            res[i][1] = intervals[i][1];
        }
        res[start + 1][0] = newInterval[0];
        res[start + 1][1] = newInterval[1];
        for (int i = start + 2; i < res.length; i++) {
            res[i][0] = intervals[end][0];
            res[i][1] = intervals[end][1];
            end++;
        }
        return res;
    }

    //228. 汇总区间
    //这个题的基本套路还是简单的，这个数组中，对于两个相邻元素，无非是二者相差大于或等于1，
    //若等于1，则二者在同一区间
    //若大于1，则二者在不同区间
    //这题真正的难点在与想到使用while循环而非for循环，
    //最开始我的想法是使用for循环，每个迭代都判断一次，这样需要判定的特殊情况由很多（尤其是最后一个数单独一个区间的情况）
    //最好的做法还是使用while循环，以区间为单位，在一个区间遍历完成后，再去向下继续遍历下一个区间，这样就简单多了。
    public List<String> summaryRanges(int[] nums) {
        int start = 0, end = 0;
        if (nums == null) {
            return null;
        }
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < nums.length) {
            start = i;
            while (i + 1 < nums.length && nums[i + 1] == nums[i] + 1) {
                i++;
            }
            end = i;
            if (start != end) {
                res.add(nums[start] + "->" + nums[end]);
            } else {
                res.add("" + nums[start]);
            }
            i++;
        }
        return res;
    }


    //163. 缺失的区间
    //这个题与228类似，这里我才用了一点取巧的方式来规避循环后重新处理结尾的方法：
    //也就是复制数组，把upper+1加入到数组末位，这样在正常遍历过程中就可以把最后的结尾算进去。
    //机制和228类似，也是遍历区间，但不同的是遍历完一个区间后，输出的不是该区间，而是该区间与上一个区间的间隔。
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        int lastEnd = lower - 1;
        int curStart = 0;
        int i = 0;
        List<String> res = new ArrayList<>();
        if (nums == null) {
            return res;
        }
        int[] tmp = Arrays.copyOf(nums, nums.length + 1);
        tmp[nums.length] = upper + 1;
        while (i < tmp.length) {
            curStart = tmp[i];
            if (lastEnd + 1 < curStart - 1) {
                res.add((lastEnd + 1) + "->" + (curStart - 1));
            } else if (lastEnd == curStart - 2) {
                res.add("" + (lastEnd + 1));
            }
            while (i + 1 < tmp.length - 1 && tmp[i + 1] <= tmp[i] + 1) {
                i++;
            }
            lastEnd = tmp[i];
            i++;

        }
        return res;
    }

    //53. 最大子序和
    //这个东西有点诡异哈，看起来其实还是用动态规划快一些
    //状态变量选取"f(i) = 以第i个元素结尾的子数组的最大和"(注意这种形式在解决连续子数组问题时非常有用
    public int maxSubArray(int[] nums) {
        int[] sums = new int[nums.length];
        sums[0] = nums[0];
        int res = sums[0];
        for (int i = 1; i < nums.length; i++) {
            sums[i] = Math.max(nums[i], sums[i - 1] + nums[i]);
            res = Math.max(sums[i], res);
        }
        return res;
    }

    //325. 和等于 k 的最长子数组长度
    //这个题属于典型的子数组(分段处理)的题型
    //这里需要注意的一个小的技巧点在于，可以使用哈希表用于存储前缀和和其对应的索引，方便后续查找
    //基本思路是这样的，首先求各个为止对应的前缀和以及对应索引，存在HashMap中
    //而后二次遍历各个元素的前缀和，这个时候注意，与之对应的和为k的元素下标，可以直接在map中，查找得到。
    //这里查找的手段由从左到右，从右到左两种，如果从左到右，则查询key为当前前缀和加上k
    //如果从右到左，则查询key为前缀和-k。
    //更优的方案为从右到左，这样可以把控长度，一旦从右到做的遍历长度小于max，则可直接终止遍历。
    public int maxSubArrayLen(int[] nums, int k) {
        if (nums == null) {
            return 0;
        }
        Map<Integer, Integer> map = new HashMap<>();
        int[] sums = new int[nums.length + 1];
        int res = 0;
        map.put(0, 0);
        for (int i = 0; i < nums.length; i++) {
            sums[i + 1] = sums[i] + nums[i];
            map.put(sums[i + 1], i + 1);
        }

        for (int i = 0; i < sums.length; i++) {
            if (map.containsKey(sums[i] + k)) {
                res = Math.max(res, (map.get(sums[i] + k) - i));
            }
        }
        return res;
    }


    //152. 乘积最大子数组
    //这个题看着和那个最大子序和的题差不多，但其实有个陷阱：在于加和这个操作两个较大的相加一定大于两个较小的相加
    //但乘法不同，存在两个较小的相乘，大于两个较大的相乘的情况（负负得正）
    //所以思路的基本点在于：
    //f[i]仍然表示以第i个元素作为结尾的子数组的最大值，但是需要注意：
    //这里需要分情况讨论：
    //若nums[i] >= 0,则取的是max(nums[i], f[i-1])
    //若nums[i] < 0,则取的是max(nums[i], 以i-1为结尾的最小乘积)
    //也就是说，我们需要同时维护最大、最小两个dp数组方可。
    public int maxProduct(int[] nums) {
        int[] resListMax = new int[nums.length];
        int[] resListMin = new int[nums.length];
        resListMax[0] = nums[0];
        resListMin[0] = nums[0];
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            //关于下面这种分情况讨论，有一个稍微优化的方法：
            //例如针对resListMax[i]，可以直接取nums[i]、resListMax[i - 1] * nums[i]、 resListMin[i - 1] * nums[i]的最大值，避免一次判断
            //不过这里没有用，因为感觉上这个东西优化优先，其实只是停留在代码简洁程度层面，对真正意义上的运行效率没啥帮助。
            if (nums[i] >= 0) {
                resListMax[i] = Math.max(nums[i], resListMax[i - 1] * nums[i]);
                resListMin[i] = Math.min(nums[i], resListMin[i - 1] * nums[i]);
            } else {
                resListMax[i] = Math.max(nums[i], resListMin[i - 1] * nums[i]);
                resListMin[i] = Math.min(nums[i], resListMax[i - 1] * nums[i]);
            }
            res = Math.max(resListMax[i], res);
        }
        return res;
    }

    // 48. 旋转图像
    public void rotate(int[][] matrix) {
        rotateInY(matrix);
        rotateInPosCross(matrix);
    }

    public void rotateInX(int[][] matrix) {
        int N = matrix.length;
        for (int i = 0; i < matrix.length; i++) {
            int tmp = 0;
            for (int j = 0; j < N / 2; j++) {
                tmp = matrix[i][N - j - 1];
                matrix[i][N - j - 1] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
    }

    public void rotateInY(int[][] matrix) {
        int N = matrix.length;
        for (int i = 0; i < N / 2; i++) {
            int tmp = 0;
            for (int j = 0; j < matrix.length; j++) {
                tmp = matrix[N - i - 1][j];
                matrix[N - i - 1][j] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
        int i = 1;
    }

    public void rotateInPosCross(int[][] matrix) {
        int N = matrix.length;
        for (int i = 0; i < N; i++) {
            int tmp;
            for (int j = 0; j < i; j++) {
                tmp = matrix[j][i];
                matrix[j][i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
    }

    public void rotateInNegCross(int[][] matrix) {
        int N = matrix.length;
        for (int i = 0; i < N; i++) {
            int tmp;
            for (int j = 0; j < N - 1 - i; j++) {
                tmp = matrix[N - 1 - j][N - 1 - i];
                matrix[N - 1 - j][N - 1 - i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
    }

    //73. 矩阵置零
    //时间复杂度O(M*N)，空间复杂度O(M + N)
    public void setZeroes(int[][] matrix) {
        if (matrix == null) {
            return;
        }
        Set<Integer> setX = new HashSet<>();
        Set<Integer> setY = new HashSet<>();
        int NX = matrix.length;
        int NY = matrix[0].length;
        for (int i = 0; i < NX; i++) {
            for (int j = 0; j < NY; j++) {
                if (matrix[i][j] == 0) {
                    setX.add(i);
                    setY.add(j);
                }
            }
        }
        for (int x : setX) {
            for (int i = 0; i < NY; i++) {
                matrix[x][i] = 0;
            }
        }
        for (int y : setY) {
            for (int i = 0; i < NX; i++) {
                matrix[i][y] = 0;
            }
        }
    }

    //73. 矩阵置0
    // 优化方法：时间复杂度O(M*N)，空间复杂度O(1)
    // 这种做法的核心思路在于，使用矩阵每一行每一列的第一个元素作为标识位，监测到该行需要置0，则将第一行元素置0，这样不会有冲突
    // 不过有个地方需要注意，就是第一行第一列是互相冲突的，因此需要使用一个额外的标志位来标记第一列是否需要置0。
    public void setZeroes1(int[][] matrix) {
        if (matrix == null) {
            return;
        }
        boolean isColSet = false;
        int NX = matrix.length;
        int NY = matrix[0].length;
        for (int i = 0; i < NX; i++) {
            if (matrix[i][0] == 0) {
                isColSet = true;
            }
        }
        for (int i = 0; i < NX; i++) {
            for (int j = 1; j < NY; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        for (int i = 1; i < NX; i++) {
            for (int j = 1; j < NY; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        if (matrix[0][0] == 0) {
            for (int i = 0; i < NY; i++) {
                matrix[0][i] = 0;
            }
        }
        if (isColSet) {
            for (int i = 0; i < NX; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    // 134. 加油站(有点小困难呢，需要好好想想怎么证明)
    // 关键性的定理在于，如果总的加油大于总的花费，则一定存在解！(这个定理需要考虑一下怎么证明)
    public int canCompleteCircuit(int[] gas, int[] cost) {
//        int total = 0;
//        int cur = 0;
//        for (int i = 0; i < gas.length; i++) {
//            total += gas[i] - cost[i];
//            cur += gas[i] - cost[i];
//        }
        return -1;
    }

    /*---------------------------二分法---------------------------*/
    /*---------------------------二分法---------------------------*/


    public int binarySearch(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] > target) {
                end = mid - 1;
            } else if (nums[mid] < target) {
                start = mid + 1;
            } else {
                return mid;
            }
            System.out.println("start = " + start + " end = " + end);
        }
        System.out.println("start = " + start + " end = " + end);
        return -1;
    }

    public int binarySearch1(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] > target) {
                end = mid;
            } else if (nums[mid] < target) {
                start = mid + 1;
            } else {
                return mid;
            }
            System.out.println("start = " + start + " end = " + end);
        }
        System.out.println("start = " + start + " end = " + end);
        return -1;
    }

    public int binarySearch3(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start + 1 < end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] > target) {
                end = mid;
            } else if (nums[mid] < target) {
                start = mid;
            } else {
                return mid;
            }
            System.out.println("start = " + start + " end = " + end);
        }
        if (nums[start] == target) {
            return start;
        } else if (nums[end] == target) {
            return end;
        }
        System.out.println("start = " + start + " end = " + end);
        return -1;
    }


    // 35. 搜索插入位置
    public int searchInsert(int[] nums, int target) {
        if (nums == null) {
            return -1;
        }
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] < target) {
                start = mid + 1;
            } else if (nums[mid] > target) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return start;
    }

    // 278. 第一个错误的版本


    // 33. 搜索旋转排序数组
    public int search(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] >= nums[start]) {
                if (target >= nums[start] && target < nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else if (nums[mid] <= nums[end]) {
                if (target > nums[mid] && target <= nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    //81. 搜索旋转排序数组2
    public boolean search81(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return true;
            }
            //81这个可重复的条件容易出现start、mid、end的值都相同，从而导致后续判断错误，找了错误的半边进行搜索。
            if (nums[start] == nums[mid] && nums[mid] == nums[end]) {
                start++;
                end--;
                //这种方法看似暴力，实际上有他的道理在，这种兼顾两个半边的二分，其实是不怕通过+1,-1这种形式的缩减的
                //并且在这种条件下，start==mid==end，mid在最开始的判断中以及断定并非target所以不会漏判
            } else if (nums[mid] >= nums[start]) {
                //这里的else if是有大文章的，因为之前那波start++, end--的操作很容易搞出越界或者start<end的情况。
                //一次在操作完后，需要进入下一次循环先排除这些情况。
                if (target >= nums[start] && target < nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else if (nums[mid] <= nums[end]) {
                if (target > nums[mid] && target <= nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return false;
    }

    //74. 搜索二维矩阵
    //这个题比较简单，说是二维矩阵，其实只是一个变形后的一维矩阵而已，只要将index转变为二维矩阵坐标就可以了。
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int start = 0;
        int end = matrix.length * matrix[0].length - 1;
        int[] midCoord = new int[2];
        while (start <= end) {
            int mid = start + (end - start) / 2;
            get2DIndex(mid, midCoord, matrix[0].length);
            if (matrix[midCoord[0]][midCoord[1]] < target) {
                start = mid + 1;
            } else if (matrix[midCoord[0]][midCoord[1]] > target) {
                end = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    //240. 搜索二维数组2
    //这个题过于特殊，只能说看起来形式和思路有点想二分法，但其实压根不是二分法。
    //他的关键在于，由于矩阵的行和列都是递增的(但不同行、不同列没有明确的大小关系)
    //这个算法总的来说可以视为高级排除法，逐行、逐列的去排除，最终找到目标为止（或者找不到）
    public boolean searchMatrix240(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int row = 0;
        int col = matrix[0].length - 1;
        //注意这里row、col 分别从第一行和最后一列开始。
        while (col >= 0 && row < matrix.length) {
            if (target == matrix[row][col]) {
                return true;
            } else if (target < matrix[row][col]) {
                //若当前对应元素小于target，则必定不当前列(或之后),需要列下标col-1(因为单列之内是递增的)
                col--;
            } else {
                //若当前元素大于target，则必定不再当前行，必须是后面的行，因此行下表row+1
                row++;
            }
        }
        return false;
    }

    //374. 猜数字
    public int guessNumber(int n) {
        int start = 1;
        int end = n;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (guess(mid) == -1) {
                end = mid - 1;
            } else if (guess(mid) == 1) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    int guess(int num) {
        return -1;
    }

    public void get2DIndex(int index, int[] res, int cols) {
        res[0] = index / cols;
        res[1] = index - res[0] * cols;
    }

    //34. 在排序数组中查找元素的第一个和最后一个位置
    //这个题的关键点在第一个和最后一个这个东西：
    //对于普通的二分法而言，找到的目标值到底是第一个还是最后一个其实是没有保障的。
    //要想保障，就必须在找到一个目标值以后做个判断，
    //1. 如果想想左遍历，则需要看前一个值是否也是target，若是，则end = mid - 1,继续进行；若不是，则直接返回。
    //2. 如果想想右遍历，同上，逻辑反过来即可。
    //！！！！！！！！！分割线：
    //LC官方题解要更加精妙，他使用了第二中迭代方式left < right，我们知道，这种方式下：
    //只有nums[mid] > target的的时候，right才会收缩，其仅仅收缩至mid，不会继续收缩
    //这样，在规定向右搜索，只要修改条件，删除循环中的返回语句，则最终找到的left - 1（注意这里）默认就是最右边的元素。
    //而向左遍历则也比较简单，继续修改条件，同样去除直接返回的语句，改为当nums[mid] <= target时候，right = mid;
    //这样就有且仅有num[mid] < target的时候，left才前进一位，即可保证最终拿到的left值就是最左边的值。
    public int[] searchRange(int[] nums, int target) {
        int[] res = {-1, -1};
        if (nums == null) {
            return res;
        }
        int leftIndex = binarySearchLeft(nums, target, true);
        if (leftIndex == -1) {
            return res;
        } else if (leftIndex == nums.length - 1 || nums[leftIndex + 1] != target) {
            res[0] = leftIndex;
            res[1] = leftIndex;
            return res;
        }
        int rightIndex = binarySearchLeft(nums, target, false);
        res[0] = leftIndex;
        res[1] = rightIndex;
        return res;
    }

    public int binarySearchLeft(int[] nums, int target, boolean left) {
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] > target) {
                end = mid - 1;
            } else if (nums[mid] < target) {
                start = mid + 1;
            } else {
                if (left && mid >= 1 && nums[mid - 1] == target) {
                    end = mid - 1;
                } else if (!left && mid < nums.length - 1 && nums[mid + 1] == target) {
                    start = mid + 1;
                } else {
                    return mid;
                }
            }
        }
        return start;
    }


    //275. H指数
    //这个题是真没有想对路子，难点还在于找对判定条件，本题中的H指数成立的判定条件应该为：
    //citations[pivot] >= n - pivot
    //而找最大的H指数的判定边界条件应该为：
    //citations[pivot] == n - pivot
    //原因在于，在这种情况下，若是继续想左边着，则citations[pivot]不会再变大，而n - pivot必然变大
    //这样就不满足H指数的条件了，而向右找，则还能满足H指数的条件，但是所得的h指数只会变小。
    //这样直接满足这个条件的东西肯定就是我们需要的h参数了，但是。。。
    //而这样就存在找不到一个真的满足临界条件的解的情况（比如{1, 5, 5, 5, 5}最大的H指数为4，但4并不在数组中，一定找不到）
    //这样我们就需要结合三种迭代形式找不到的情况下的解来看了。
    //我们知道第一种迭代形式left <= right找不到时，最终真正的解应该落在[right, left]之间，
    //根据h指数的定义此时我们数组长度length - left，即可得到最终的h指数。
    public int hIndex(int[] citations) {
        int idx = 0, n = citations.length;
        int pivot, left = 0, right = n - 1;
        while (left <= right) {
            pivot = left + (right - left) / 2;
            if (citations[pivot] == n - pivot) return n - pivot;
            else if (citations[pivot] < n - pivot) left = pivot + 1;
            else right = pivot - 1;
        }
        System.out.println(left + " " + right);
        return n - left;
    }

    /*---------------------------链表题---------------------------*/
    /*---------------------------链表题---------------------------*/

    public ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dump = new ListNode(0);
        ListNode l1 = dump;
        ListNode l2 = head;
        while (l2 != null && l2.next != null) {
            l1.next = l2.next;
            ListNode newStart = l2.next.next;
            l2.next.next = l2;
            l2.next = newStart;
            l1 = l2;
            l2 = l2.next;
        }
        return dump.next;
    }

    //328. 奇偶链表
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode l1 = head;
        ListNode l2 = head.next;
        ListNode EventHead = l2;
        ListNode pre = l1;
        int i = 1;
        while (l2 != null) {
            l1.next = l2.next;
            pre = l1;
            l1 = l2;
            l2 = l2.next;
            i++;
        }
        if ((i & 1) == 0) {
            pre.next = EventHead;
        } else {
            l1.next = EventHead;
        }
        return head;
    }

    //206. 反转链表
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        while (head != null) {
            ListNode tmp = head.next;
            head.next = pre;
            pre = head;
            head = tmp;
        }
        return pre;
    }

    //206 反转链表（聪明的做法）
    //注意，至少仅就这个题目而言，不算什么聪明的做法，容易让人读起来很难理解：
    //这个地方算法的核心其实是：
    //每次都把下一个节点移向队首：
    //要做到这一点，收看需要一个假的头节点dummy，而后每次将head的下一个节点移向头节点
    //举例来说：
    //对于 1 -> 2 -> 3 -> 4 -> 5 -> 6
    //首先新增一个头节点0：
    //0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6
    //而后
    //(pre)0 -> (head)1 -> (tmp)2 -> 3 -> 4 -> 5 -> 6
    //我们把tmp移动向队首：
    //(pre)0 -> 2 -> (head)1 -> (tmp)3 -> 4 -> 5 -> 6
    //继续:
    //(pre)0 -> 3 -> 2 -> (head)1 -> (tmp)4 -> 5 -> 6
    //以此类推，最终得到结果
    //这个方法其实整体来说是比原方法复杂的多的，这个题没啥意义，主要针对下一题LC. 92
    public ListNode reverseList1(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = new ListNode(0);
        pre.next = head;
        while (head.next != null) {
            ListNode tmp = head.next;
            head.next = tmp.next;
            tmp.next = pre.next;
            pre.next = tmp;
        }
        return pre.next;
    }

    //92. 反转链表 II
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || head.next == null) {
            return head;
        }
        int curIdx = 0;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode originHead = dummy;
        while (curIdx < m - 1) {
            dummy = dummy.next;
            curIdx++;
        }
        ListNode head1 = dummy;
        ListNode pre = dummy;
        ListNode head2 = dummy.next;
        dummy = dummy.next;
        while (curIdx < n) {
            ListNode tmp = dummy.next;
            dummy.next = pre;
            pre = dummy;
            dummy = tmp;
            curIdx++;
        }
        head1.next = pre;
        head2.next = dummy;
        return originHead.next;
    }

    //92. 反转链表 II(聪明做法)
    //这个题我们上面的做法其实已经足够了，从时间复杂度来说也是最优了，但是代码略显臃肿
    //接下来我们来找一种更加简洁的写法：
    //我们注意到，只所以写得这么臃肿是因为，我们需要去记录局部翻转过程中需要断开的几个重要节点。
    //在翻转后再将他们重新连接起来，
    //这时候我们就会想，要是不需要这些步骤该多好啊！
    //于是我们就想到了206中专门写得那个极其难以理解的写法：
    //这个时候我们会发现，通过这种每次将需要翻转的元素移至队首的写法，似乎也不用考虑重新连接的问题：
    //因为这种写法可以保证，自始自终整个链表就没有被断开过，只是进行局部移位而已。
    //哎，这就是大神聪明的地方了：
    public ListNode reverseBetween1(ListNode head, int m, int n) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = new ListNode(0);
        ListNode dummy = pre;
        pre.next = head;
        ListNode cur = head;
        for (int i = 1; i < m; i++) {
            cur = cur.next;
            pre = pre.next;
        }
        for (int i = 0; i < n - m; i++) {
            ListNode tmp = cur.next;
            cur.next = tmp.next;
            tmp.next = pre.next;
            pre.next = tmp;
        }
        return dummy.next;
    }

    //25. K 个一组翻转链表(🐂逼迭代写法)
    public ListNode reverseKGroup1(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        int idx = 1;
        while (head.next != null) {
            if (idx % k == 0) {
                pre = head;
                head = head.next;
                idx++;
                continue;
            }
            ListNode tmp = head.next;
            head.next = tmp.next;
            tmp.next = pre.next;
            pre.next = tmp;
            idx++;
        }
        if (idx % k > 1) {
            head = pre.next;
            while (head.next != null) {
                ListNode tmp = head.next;
                head.next = tmp.next;
                tmp.next = pre.next;
                pre.next = tmp;
            }
        }
        return dummy.next;
    }

    //141. 环形列表
    //这个题目比较常规的方法是通过HashSet，遍历过程中，每次都将节点存在Set中，
    //最后无非两种结果：
    //1.遍历得到null,则无环，返回false
    //2.遍历得到set中已有的元素，则有环，返回true
    //另一个就是这种快慢指针法，双指针，同时出发，一个每次过一个节点
    //另一个每次过两个节点，若有环，则二者必定相遇，此时返回true;
    //若无环，则快指针必将先遍历至null，则返回false;
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    //142. 环形列表II
    //这个题也可用141那种笨办法，若是判定有环时，直接返回当时遍历的节点即可。
    //这样简单易懂，但是会引入O(N)N为链表长度的空间复杂度，
    //另一种延续快慢指针的做法，在判定到有环（即快慢指针相遇后）
    //将慢指针移动至链表开头，而后二者均以慢速遍历，则下一次相遇必定在环的起点
    //具体证明后续再写。可百度Flyod算法证明。
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }

    //83. 删除排序链表中的重复元素
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode originHead = head;
        while (head != null) {
            ListNode cur = head.next;
            while (cur != null && cur.val == head.val) {
                cur = cur.next;
            }
            head.next = cur;
            head = cur;
        }
        return originHead;
    }

    //82. 删除排序链表中的重复元素 II
    public ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        ListNode cur = head;
        while (cur != null) {
            int count = 0;
            ListNode tmp = cur;
            while (tmp.next != null && tmp.next.val == tmp.val) {
                tmp = tmp.next;
                count++;
            }
            if (count == 0) {
                prev = cur;
            } else {
                prev.next = tmp.next;
            }
            cur = tmp.next;
        }
        return dummy.next;
    }

    //82. 删除排序链表中的重复元素 II(聪明写法)
    //与我自己写得在本质上没有什么区别，但代码逻辑要简单的多：
    public ListNode deleteDuplicates3(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        while (pre.next != null && pre.next.next != null) {
            if (pre.next.next.val == pre.next.val) {
                int number = pre.next.val;
                while (pre.next != null && pre.next.val == number) {
                    pre.next = pre.next.next;
                }
            } else {
                pre = pre.next;
            }
        }
        return dummy.next;
    }


    //21. 合并两个有序链表(迭代写法)
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = new ListNode(l1.val);
                l1 = l1.next;
            } else {
                cur.next = new ListNode(l2.val);
                l2 = l2.next;
            }
            cur = cur.next;
        }
        if (l1 == null) {
            cur.next = l2;
        } else {
            cur.next = l1;
        }
        return dummy.next;
    }

    //22. 合并两个有序链表(递归写法)
    //递归的精髓在于拆解子问题
    public ListNode mergeTwoLists3(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        if (l1.val < l2.val) {
            //这个条件下，已经确定新链表的头即为l1，接下来的问题只需要确定之后的组成就行：
            //此时用于考察的链表变为l1.next和l2.
            l1.next = mergeTwoLists3(l1.next, l2);
            return l1;
        } else {
            //反之亦然
            l2.next = mergeTwoLists3(l1, l2.next);
            return l2;
        }
    }

    //234. 回文链表(听完具体思路，一遍编写直接AC，😄)
    //基本思路分三步走：
    //1. 找到链表中点(这一点最重要，实际采取的方法为快慢指针法，快指针跳2，慢指针跳1)
    //   这里需要注意，的确存在链表长度为奇数/偶数时，最终fast指针停留为止会有区别,
    //   但对我们本体中的需求无碍，我们只要保证fast指针最终停留位置的下一个即为下半部分链表的开始即可
    //   仔细想想，要是以这个为目的的话，链表长度的奇偶其实无所谓。
    //2. 将后半部分链表翻转(基本操作，无需多讲)
    //3. 前后链表比较，若有不同，则返回false
    //   这个地方有个需要注意的点在于，因为奇偶性的原因，后半段链表总是偏短，因此我们的遍历条件应该以后半段链表是否为空为终止条件。
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode newHead = slow.next;
        while (newHead != null && newHead.next != null) {
            ListNode tmp = newHead.next;
            newHead.next = tmp.next;
            tmp.next = slow.next;
            slow.next = tmp;
        }
        while (slow.next != null) {
            if (slow.next.val != head.val) {
                return false;
            } else {
                slow = slow.next;
                head = head.next;
            }
        }
        return true;
    }

    //61. 旋转链表
    //这个题最开始我想的是直接将末位k个元素移到头上即可，但没有考虑到k本身可能大于数组长度的问题。
    //后来自己想了一种方法，AC了，基本思路为遍历先得到链表长度，而后通过取余数计算出等价的k（k < n），
    //而后在遂行之前的思路即可。
    //到这里已经实现了O(N)的时间复杂度以及O(1)的空间复杂度，但是代码不简洁
    //后来看了官方解，用了另一种功能思路，在第一次遍历的过程中，自动将链表练成环
    //而后得到等价的k后，指针移动到新的链表头处断开即可。
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode cur = head;
        int count = 1;
        while (cur.next != null) {
            cur = cur.next;
            count++;
        }
        cur.next = head;
        int realOffset = k % count;
        ListNode tmp = head;
        for (int i = 0; i < count - realOffset - 1; i++) {
            tmp = tmp.next;
        }
        ListNode newHead = tmp.next;
        tmp.next = null;
        return newHead;
    }

    //86. 分隔链表
    public ListNode partition(ListNode head, int x) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode smaller = new ListNode(x - 1);
        ListNode originSmall = smaller;
        ListNode bigger = new ListNode(x + 1);
        ListNode originBig = bigger;
        while (head != null) {
            if (head.val < x) {
                smaller.next = new ListNode(head.val);
                smaller = smaller.next;
            } else {
                bigger.next = new ListNode(head.val);
                bigger = bigger.next;
            }
            head = head.next;
        }
        smaller.next = originBig.next;
        return originSmall.next;
    }


    //19:删除链表的倒数第n个节点(优化版本)
    //这个题相较于第一次做增加了一个技巧，就是在原链表头上加一个假节点，用以应对
    //删除的节点为原头节点的情况。
    public ListNode removeNthFromEnd2(ListNode head, int n) {
        if (head == null) {
            return head;
        }
        int idx = 1;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode tmp = head;
        ListNode tail = dummy;
        while (tmp != null) {
            tmp = tmp.next;
            idx++;
            if (idx > n + 1) {
                tail = tail.next;
            }
        }
        tail.next = tail.next == null ? null : tail.next.next;
        return dummy.next;
    }

    private int[] nums;

    public int pick(int target) {
        List<Integer> list = new ArrayList<>();
        Random ran = new Random();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                list.add(i);
            }
        }
        return list.get(ran.nextInt(list.size()));
    }

    //380. 常数时间插入、删除和获取随机元素
    //这个题和之前做的都不同，这个题考察的是一种设计的思想
    //大致要求是，设计一种数据结构，可在O(1)的时间复杂度内完成插入、删除、随机取一个数的操作
    //并且，该结构需要具备set的不可重复属性
    //这里的大体思路是：
    //首先：
    //插入、删除需要O(1)，那感觉是需要用到散列表
    //但是又要求随机取一个数也要O(1)，这个明显又是普通列表才有的特性。
    //因此，实际上是将普通列表和散列表结合起来使用的。
    //HashMap用于存储数字和其对应在list中的索引
    //list来存储真正的数据
    //这里设计到的一个坑，也是关键点在于！！！！
    //删除的地方不太好设计：
    //我们知道，当前条件下，我们是可以直接拿到所需删除元素的索引值的，所以从list中以O(1)的复杂度删除
    //特定元素并不难，但问题在于，这样，被删除元素之后的元素索引值整体减一，这一点有需要在map中得以体现
    //这样就失去了O(1)复杂度，因此要换一种思路！！！！
    //即我们并不真正从list中删除这个元素，而是将所需删除的元素与list的最后一个元素做调换
    //注意，由于我们不真正删除元素，因此所谓list的最后一个元素，指的是list的最后一个真正有效的元素
    //这里list的大小实际上是以map的size来做标记的。
    //当然，还有更为聪明的做法：
    //！！！
    //我们可以删除元素，但不是指定的那个，而是list的最后一个元素，同时记录其值，
    //然后把改值赋给需要删除的对应索引的元素即可。
    class RandomizedSet {

        private Random ran;
        private List<Integer> list = new ArrayList<>();
        private Map<Integer, Integer> map = new HashMap();

        /**
         * Initialize your data structure here.
         */
        public RandomizedSet() {
            ran = new Random();
        }

        /**
         * Inserts a value to the set. Returns true if the set did not already contain the specified element.
         */
        public boolean insert(int val) {
            if (!map.containsKey(val)) {
                list.add(map.size(), val);
                map.put(val, list.size() - 1);
                return true;
            }
            return false;
        }

        /**
         * Removes a value from the set. Returns true if the set contained the specified element.
         */
        public boolean remove(int val) {
            if (map.containsKey(val)) {
                int tmp = list.get(map.size() - 1);
                int originIdx = map.get(val);
                list.set(originIdx, tmp);
                map.put(tmp, originIdx);
                map.remove(val);
                return true;
            }
            return false;
        }

        /**
         * Get a random element from the set.
         */
        public int getRandom() {
            return list.get(ran.nextInt(map.size()));
        }
    }

    //蓄水池抽样法 n选1
    public int pick1(int[] nums) {
        int res = -1;
        Random random = new Random();
        for (int i = 0; i < nums.length; i++) {
            if (random.nextInt(i + 1) == 0) {
                res = i;
            }
        }
        return res;
    }

    //蓄水池抽样法 n选m
    public int[] pick2(int[] nums, int m) {
        int[] res = Arrays.copyOf(nums, m);
        Random ran = new Random();
        for (int i = m; i < nums.length; i++) {
            int randomIdx = ran.nextInt(i + 1);
            if (randomIdx < m) {
                res[randomIdx] = nums[i];
            }
        }
        return res;
    }

    //384. 洗牌
    class Shulffer {

        int[] nums;
        Random random;

        public Shulffer(int[] nums) {
            this.nums = nums;
            random = new Random();
        }

        /**
         * Resets the array to its original configuration and return it.
         */
        public int[] reset() {
            return nums;
        }

        /**
         * Returns a random shuffling of the array.
         */
        public int[] shuffle() {
            int[] clone = nums.clone();
            int N = clone.length;
            for (int i = 0; i < clone.length; i++) {
                int ran = random.nextInt(N - i) + i;
                int tmp = clone[ran];
                clone[ran] = clone[i];
                clone[i] = tmp;
            }
            return clone;
        }
    }

    //381. O(1) 时间插入、删除和获取随机元素 - 允许重复
    //这题也太鸡儿难了把。。。。。
    //第一个难点是方法，我想偏了，其实方法就是：
    //和380类似，还是使用hash表辅助，但是由于本题允许重复，hash表中的value不再是索引，而是索引的集合（key依然为元素值）
    //然后原始数据结构依然使用ArrayList存储全部元素（主要还是为了最后一步getRandom服务）.
    //同样，与380相同，最费劲的地方依旧是remove方法：
    //这里的remove策略与380大体相同，依旧是将需要remove的元素与list中的最后一个元素交换为止后，删除list中的最后一个元素
    //同时，需要从那个存储元素对应索引值的集合中将上述变化同步。(也就是从被删除元素对应集合中删除原索引，加入最后一个元素的对应集合中，
    // ！！！最重要的是，同样需要从最终元素的集合中删除原有的最后一个索引值！！！，这句话很重要啊！！！)
    //第一个难点其实还好，第二个难点就是具体实现了：：
    //首先上面说半天这个索引集合到底应该选择什么？
    //最开始我选择的是ArrayList，但很快发现这玩意有个致命漏洞，即为了保证删除元素不影响其他元素的索引
    //因此我们在删除元素时都需要将其和最后一个元素交换，同时也需要替换索引集合中的对应索引，
    //List的致命弱点在于，我们在交换时，是无法知道List中到底哪一个索引代表的是数组中最后一个元素的索引(
    // 照常规而言就是最后一个元素，但实际上在不断的删除操作中，可能会产生变化)
    //因此，我们必须使用Set，这样就可以很轻松的按照原始List的大小来决定删除哪一个元素了。
    //当然，这里边还有一大堆涉及到操作顺序的坑，最后经过一个小时的努力，最后终于写除了完全AC的值。
    //TIPS:针对于Set中不知道具体值，但必须随机删除一个元素是，需要使用迭代器。
    //我真的好胖胖呢。

    static class RandomizedCollection {
        private HashMap<Integer, Set<Integer>> mDataMap;
        private List<Integer> mList;
        private Random ran;

        /**
         * Initialize your data structure here.
         */
        public RandomizedCollection() {
            mDataMap = new HashMap<>();
            mList = new ArrayList<>();
            ran = new Random();
        }

        /**
         * Inserts a value to the collection. Returns true if the collection did not already contain the specified element.
         */
        public boolean insert(int val) {
            if (mDataMap.containsKey(val)) {
                mList.add(val);
                mDataMap.get(val).add(mList.size() - 1);
                return false;
            } else {
                mList.add(val);
                Set<Integer> tmp = new HashSet<>();
                tmp.add(mList.size() - 1);
                mDataMap.put(val, tmp);
                return true;
            }
        }

        /**
         * Removes a value from the collection. Returns true if the collection contained the specified element.
         */
        public boolean remove(int val) {
            if (!mDataMap.containsKey(val)) {
                return false;
            }
            int tailValue = mList.get(mList.size() - 1);
            int tailIdx = mList.size() - 1;
            int tmpIdx = tailIdx;
            Set<Integer> tailSet = mDataMap.get(tailValue);
            if (val != tailValue) {
                Set<Integer> tmp = mDataMap.get(val);
                Iterator<Integer> it = tmp.iterator();
                tmpIdx = it.next();
                it.remove();
                if (tmp.size() == 0) {
                    mDataMap.remove(val);
                }
                mList.set(tmpIdx, tailValue);
                tailSet.add(tmpIdx);
            }
            tailSet.remove(tailIdx);
            if (tailSet.size() == 0) {
                mDataMap.remove(tailValue);
            }
            mList.remove(mList.size() - 1);
            return true;
        }

        /**
         * Get a random element from the collection.
         */
        public int getRandom() {
            return mList.get(ran.nextInt(mList.size()));
        }
    }

    //138. 复制带随机指针的链表(现在还是一个比较笨的办法，等后面学了回溯再来搞起。)
    public Node copyRandomList(Node head) {
        if (head == null) {
            return head;
        }
        Map<Node, Node> map = new HashMap<>();
        Node dummy = new Node(0);
        Node cur = dummy;
        Node originHead = head;
        while (head != null) {
            cur.next = new Node(head.val);
            map.put(head, cur.next);
            cur = cur.next;
            head = head.next;
        }
        head = originHead;
        cur = dummy.next;
        while (head != null) {
            cur.random = map.get(head.random);
            cur = cur.next;
            head = head.next;
        }
        return dummy.next;
    }

    /*---------------------------栈---------------------------*/
    /*---------------------------栈---------------------------*/

    //71. 简化路径
    public String simplifyPath(String path) {
        if (path == null || path.length() == 0) {
            return path;
        }
        String[] pieces = path.split("/");
        Stack<String> stack = new Stack<>();
        for (String s :
                pieces) {
            if (s.equals(".") || s.length() == 0) {
                continue;
            } else if (s.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(s);
            }
        }
        StringBuilder res = new StringBuilder();
        if (stack.isEmpty()) {
            res.append("/");
        }
        while (!stack.isEmpty()) {
            res.insert(0, "/" + stack.pop());
        }
        return res.toString();
    }

    //394. 字符串解码
    //这个题的基本思路就是：
    //1. 首先用栈
    //2. 其次，因为字符串中有数字和字母组合两种形式，且代表不同意义，均需存储，因此必须使用两个不同的栈来存储。
    //3. 最后就是最难的实现部分，这个确实还是代码写得少，自己还是没有相处正确解法：
    //下面这套实现的核心就在于，任意时刻，数字栈中的栈定元素都是当前闭包的重复次数！！！
    //而字符串栈栈定的元素都是当前闭包之前(上一层闭包之内)的字符串。
    //上面这些东西自己第一次想的时候都能想个大概，但是结合起来就不行了，还是得看大神的解法
    //还是需要继续💪💪💪💪💪💪💪💪💪💪💪💪💪💪！！！！！！
    //总的来说，这个题除了基本思路以外，更多的是考察基本代码能力，还是得继续积累。
    public String decodeString(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        Stack<Integer> numStack = new Stack<>();
        Stack<String> stringStack = new Stack<>();
        int idx = 0;
        String res = "";
        while (idx < s.length()) {
            char ch = s.charAt(idx);
            if (Character.isDigit(ch)) {
                int num = 0;
                while (Character.isDigit(s.charAt(idx))) {
                    num = num * 10 + s.charAt(idx) - '0';
                    idx++;
                }
                numStack.push(num);
            } else if (ch == '[') {
                stringStack.push(res);
                res = "";
                idx++;
            } else if (ch == ']') {
                StringBuilder tmp = new StringBuilder(stringStack.pop());
                int count = numStack.pop();
                for (int i = 0; i < count; i++) {
                    tmp.append(res);
                }
                res = tmp.toString();
                idx++;
            } else {
                res += ch;
                idx++;
            }
        }
        return res;
    }


    //224. 基本计算器
    //首先庆祝一波，这个题在学习了课程以后，手撸直接过，😄😄😄😄😄
    //这个题基本形式与上面那个字符串解码就基本类似了，
    //就不需要过多的讲解了，
    //同样是遇到数字就while循环计算整个十进制数字的值
    //遇到+/-号对符号位进行置位
    //遇到(入栈，同时将res清零
    //遇到)出栈，计算当前累计res值.
    //完美
    //但是有个东西需要注意一下，就这个题而言，是有优化空间的：
    //本题中的符号可以使用1/-1表示，由于输入的形式都是合法的，因此我们可以直接将表征符号的数字也压入表征结果的数字栈中
    //只要按照一定的顺序，压栈/出栈，就能保证连续出栈时，拿到的第一个值就是符号位，第二个值是res。
    //当然，这里我没有这么改，因为这个东西本质上来说，并不会带来什么太高的好处，对时间/空间复杂的都没有本质上的改进。
    public int calculate(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int res = 0;
        int signal = 1;
        int idx = 0;
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> signalStack = new Stack<>();
        while (idx <= s.length() - 1) {
            char ch = s.charAt(idx);
            if (ch == '(') {
                stack.push(res);
                signalStack.push(signal);
                res = 0;
                signal = 1;
                idx++;
            } else if (ch == ')') {
                if (!stack.isEmpty()) {
                    int num = stack.pop();
                    int curS = signalStack.pop();
                    res = curS == 1 ? num + res : num - res;
                }
                idx++;
            } else if (ch == '+') {
                signal = 1;
                idx++;
            } else if (ch == '-') {
                signal = 2;
                idx++;
            } else if (Character.isDigit(ch)) {
                int num = 0;
                while (idx <= s.length() - 1 && Character.isDigit(s.charAt(idx))) {
                    num = num * 10 + s.charAt(idx) - '0';
                    idx++;
                }
                res = signal == 1 ? res + num : res - num;
            } else {
                idx++;
            }
        }
        return res;
    }

    //150. 逆波兰表达式求值
    public int evalRPN(String[] tokens) {
        if (tokens == null || tokens.length == 0) {
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        for (String s : tokens) {
            if (s.length() != 1 || Character.isDigit(s.charAt(0))) {
                int num = Integer.parseInt(s);
                stack.push(num);
            } else {
                int b = stack.pop();
                int a = stack.pop();
                if (s.equals("+")) {
                    stack.push(a + b);
                } else if (s.equals("-")) {
                    stack.push(a - b);
                } else if (s.equals("*")) {
                    stack.push(a * b);
                } else if (s.equals("/")) {
                    stack.push(a / b);
                }
            }
        }
        return stack.pop();
    }

    //42. 接雨水
    //这题太难，先抄个答案，再慢慢理解
    //为了方便说明，我们将每一个可以装水的区间都称为一个碗
    //这个解法最难以理解的地方在于，这个栈到底是存了写什么东西
    //这里，我的理解是，这个栈存的是：尚未当过"碗底"的index。
    //我们来看啊，从第一个柱子开始，如果整体高度呈现逐渐下降趋势，那么显然尚且不具备装水的条件
    //因此，这些柱子就会全部被压入，因为显然他们目前不能当作碗底。
    //而一旦出现一个高于上一个柱子的情况，则进入另一种循环，
    //此时，显然上一个柱子有可能称为碗底。
    //那么就把它从栈中推出，以其作为碗底，来计算可以装多少水
    //这个时候就简单一些了，只要找前一个没有做过碗底的柱子、碗底柱子和当前柱子尝试形成一个碗，试一下能装多少水
    //这一步完成，继续向前出栈，寻找下一个碗底，这个时候，如果碗底高度还是小于当前柱子，则可以继续做碗底，重复上面的计算
    //但是，如果是大于等于，则不可以做碗底，本次计算结束，
    //当前柱子入栈(没有做过碗底)，继续找下一个柱子即可。
    public int trap(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        int res = 0;
        int idx = 0;
        while (idx < height.length) {
            while (!stack.isEmpty() && height[stack.peek()] < height[idx]) {
                int bot = stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                int dis = idx - stack.peek() - 1;
                int high = Math.min(height[stack.peek()], height[idx]) - height[bot];
                res += dis * high;
            }
            stack.push(idx++);
        }
        return res;
    }

    //
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        int res = 0;
        int idx = 0;
        for (int i = 0; i <= heights.length; i++) {
            int hi = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && hi < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int start = stack.isEmpty() ? -1 : stack.peek();
                int area = height * (i - start - 1);
                res = Math.max(res, area);
            }
            stack.push(i);
        }
        return res;
    }


    //155. 最小栈
    class MinStack {

        Stack<Integer> stack;
        Stack<Integer> minStack;
        int min = Integer.MAX_VALUE;

        /**
         * initialize your data structure here.
         */
        public MinStack() {
            stack = new Stack<>();
            minStack = new Stack<>();
        }

        public void push(int x) {
            stack.push(x);
            if (x <= min) {
                minStack.push(min);
                min = x;
            }
        }

        public void pop() {
            int t = stack.pop();
            if (t == min) {
                min = minStack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return min;
        }
    }

    //232. 用栈实现队列
    class MyQueue {

        Stack<Integer> mainStack;
        Stack<Integer> subStack;

        /**
         * Initialize your data structure here.
         */
        public MyQueue() {
            mainStack = new Stack<>();
            subStack = new Stack<>();
        }

        /**
         * Push element x to the back of queue.
         */
        public void push(int x) {
            mainStack.push(x);
        }

        /**
         * Removes the element from in front of queue and returns that element.
         */
        public int pop() {
            if (subStack.isEmpty()) {
                while (!mainStack.isEmpty()) {
                    subStack.push(mainStack.pop());
                }
            }
            return subStack.pop();
        }

        /**
         * Get the front element.
         */
        public int peek() {
            if (subStack.isEmpty()) {
                while (!mainStack.isEmpty()) {
                    subStack.push(mainStack.pop());
                }
            }
            return subStack.peek();
        }

        /**
         * Returns whether the queue is empty.
         */
        public boolean empty() {
            return mainStack.isEmpty() && subStack.isEmpty();
        }
    }

    //225. 用队列实现栈
    class MyStack {

        private Queue<Integer> queue1;
        private Queue<Integer> queue2;

        /**
         * Initialize your data structure here.
         */
        public MyStack() {
            queue1 = new LinkedList<>();
            queue2 = new LinkedList<>();
        }

        /**
         * Push element x onto stack.
         */
        public void push(int x) {
            queue2.offer(x);
            while (!queue1.isEmpty()) {
                queue2.offer(queue1.poll());
            }
            Queue<Integer> tmp = null;
            tmp = queue1;
            queue1 = queue2;
            queue2 = tmp;

        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public int pop() {
            return queue1.poll();
        }

        /**
         * Get the top element.
         */
        public int top() {
            return queue1.peek();
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return queue1.isEmpty();
        }
    }

    //227. 表达式计算
    //基本思路如下：
    //这个题与224类似，但是需要追加考虑优先级的问题，乘除优先级高于加减
    //同样采用两个栈——符号栈和数字栈
    //先说说大概过程，就是先计算所有的乘除法，最后计算加减法，具体过程如下：
    //就是遇到+-*/都不用管，直接过
    //遇到数字，先迭代计算除他的值，而后判断符号栈顶的元素：
    //如果是*/则循环计算知道栈顶不是*/，将计算结果压如数字栈顶。
    //最后栈中剩余数字做加减法即可：
    //这个加减法的方法是，通过判断符号栈为+/-给数字栈顶元素加正负号进行相加即可。
    public int calculate2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Stack<Character> signals = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        int idx = 0;
        while (idx < s.length()) {
            char ch = s.charAt(idx);
            if (ch == '*' || ch == '/') {
                signals.push(ch);
                idx++;
            } else if (ch == '+' || ch == '-') {
                signals.push(ch);
                idx++;
            } else if (Character.isDigit(ch)) {
                int temp = 0;
                while (idx < s.length() && Character.isDigit(s.charAt(idx))) {
                    temp = temp * 10 + s.charAt(idx) - '0';
                    idx++;
                }
                nums.push(temp);
                while (!signals.isEmpty() && (signals.peek() == '*' || signals.peek() == '/')) {
                    int ac = nums.pop();
                    char signal = signals.pop();
                    if (signal == '*') {
                        nums.push(nums.pop() * ac);
                    } else {
                        nums.push(nums.pop() / ac);
                    }
                }
            } else {
                idx++;
            }
        }
        int res = 0;
        while (!signals.isEmpty()) {
            if (signals.pop() == '+') {
                res += nums.pop();
            } else {
                res += -nums.pop();
            }
        }
        return res + nums.pop();
    }

    //165. 比较版本号
    public int compareVersion(String version1, String version2) {
        String[] s1 = version1.split("\\.");
        String[] s2 = version2.split("\\.");
        int i = 0;
        for (i = 0; i < Math.max(s1.length, s2.length); i++) {
            int v1 = i >= s1.length ? 0 : StringToIntIgnoreZero(s1[i]);
            int v2 = i >= s2.length ? 0 : StringToIntIgnoreZero(s2[i]);
            if (v1 > v2) {
                return 1;
            } else if (v1 < v2) {
                return -1;
            }
        }
        return 0;
    }

    public int StringToIntIgnoreZero(String s) {
        int res = 0;
        int i = 0;
        while (i < s.length() && s.charAt(i) == '0') i++;
        for (int j = i; j < s.length(); j++) {
            res += 10 * res + s.charAt(j) - '0';
        }
        return res;
    }

    //168 Excel列表名称
    public String convertToTitle(int n) {
        StringBuilder builder = new StringBuilder();
        while (n > 0) {
            int tmp = (n - 1) % 26;
            n = (n - 1) / 26;
            builder.insert(0, (char) (tmp + 'A'));
        }
        return builder.toString();
    }

    //242. 有效的字母异位词(笨办法)
    public boolean isAnagram(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) {
            return false;
        }
        int[] sList = new int[26];
        int[] tList = new int[26];
        for (int i = 0; i < s.length(); i++) {
            sList[s.charAt(i) - 'a']++;
            tList[t.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (sList[i] != tList[i]) {
                return false;
            }
        }
        return true;
    }

    //242. 有效的字母异位词(升级方法)
    //空间复杂度更低，只使用一个辅助数组，具体方法为一个字符串做+计数，另一个做-计数
    //结果辅助数组全0，则为true 反之则为false
    public boolean isAnagram2(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) {
            return false;
        }
        int[] sList = new int[26];
        for (int i = 0; i < s.length(); i++) {
            sList[s.charAt(i) - 'a']++;
            sList[t.charAt(i) - 'a']--;
        }
        for (int l :
                sList) {
            if (l != 0) {
                return false;
            }
        }
        return true;
    }

    //76. 最小覆盖子串(笨办法，超时)
    public String minWindow(String s, String t) {
        List<Integer> template = new ArrayList<>();
        int[] templateList = new int[52];
        for (char ch :
                t.toCharArray()) {
            template.add(getCharIndex(ch));
            templateList[getCharIndex(ch)]++;
        }
        int[] tmp = new int[52];
        int start = 0;
        int end = 0;
        tmp[getCharIndex(s.charAt(0))]++;
        int resLength = s.length();
        int resStart = 0;
        boolean isValid = false;
        while (start <= end && end < s.length()) {
            while (start <= end && isTemplateValid(tmp, template, templateList)) {
                isValid = true;
                if (end - start + 1 < resLength) {
                    resLength = end - start + 1;
                    resStart = start;
                }
                tmp[getCharIndex(s.charAt(start))]--;
                start++;
            }
            while (end < s.length() - 1 && !isTemplateValid(tmp, template, templateList)) {
                end++;
                tmp[getCharIndex(s.charAt(end))]++;
            }
            if (end == s.length() - 1 && !isTemplateValid(tmp, template, templateList)) {
                break;
            }
        }
        return isValid ? s.substring(resStart, resStart + resLength) : "";
    }

    public int getCharIndex(char ch) {
        if (ch >= 'a') {
            return (ch - 'a' + 26);
        } else {
            return (ch - 'A');
        }
    }

    public boolean isTemplateValid(int[] tmp, List<Integer> template, int[] templateList) {
        boolean res = true;
        for (int a :
                template) {
            if (tmp[a] < templateList[a]) {
                return false;
            }
        }
        return res;
    }

    //76. 最小覆盖子串(好办法办法，超时)
    //与上面那个笨办法对比，其实思路已经对了，先说下基本思路
    //其实就是滑动窗口：
    //两个指针一个start，一个end，均初始化为0
    //总体上的循环是每次都向前推end，直至遍历完成：
    //这个过程中，每次碰到[start, end]之间的字符串满足条件（这里很重要）
    //则一步步向前推start，直到条件不满足，则继续下一步推end；
    //这个过程中，不断更新满足条件的最短子串长度以及其对应起始点。
    //这样，遍历完成时，最短子串就出现了。
    //这个聪明办法的关键在于这个判断满足条件的方法：
    //之前我的本办法是每次判断都需要遍历整个计数排序数组，确认与对应为止的字符个数均>=模版，则满足。
    //这样引入的时间复杂度过高。、
    //聪明办法采用的方式是，始终保持一个remainLen作为还需要匹配的字符个数，初始化为t的长度。
    //首先需对计数排序数组进行编码，遍历t，不断++完成初始化。
    //接下来，在更新过程中，end向右推时，每遇到一个字符，均对原计数排序数组对应位置-1；
    //这里，如果该字符在t中存在，则原位置数字应>0，此时-1时还需将remainLen-1(匹配成功一个字符，所需匹配字符-1)；
    //反之，若原位置字符不存在，仅-1，不需要更新remainLen。
    //这样就可以通过remainLen是否为0来判断是否满足条件。
    //若满足条件，则进入推动start的循环。
    //这个循环中，每次满足条件，均需更新最小子串长度及对应start
    //这里同样，推动start时候，如果被推出的字符在t中存在，则remainLen+1;
    //反之则正常推除，remainLen不需要处理。
    //如此，即可大大节省时间。
    public String minWindow1(String s, String t) {
        //还需匹配的字符总数
        int remainLen = t.length();
        int start = 0;
        int end = 0;
        int[] count = new int[256];
        for (char a : t.toCharArray()) {
            count[a]++;
        }
        int resLen = Integer.MAX_VALUE;
        int resStart = 0;
        for (start = 0, end = 0; end < s.length(); end++) {
            //这个条件只有当前元素在t中存在时才会满足，因此此时可以将所需匹配长度-1
            if (count[s.charAt(end)]-- > 0) {
                remainLen--;
            }
            while (remainLen == 0) {
                if (end - start + 1 < resLen) {
                    resLen = end - start + 1;
                    resStart = start;
                }
                if (++count[s.charAt(start++)] > 0) {
                    remainLen++;
                }
            }
        }
        return resLen == Integer.MAX_VALUE ? "" : s.substring(resStart, resStart + resLen);
    }

    //32. 最长有效括号
    public int longestValidParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        //当前考察子串始终为(j, i]
        int res = 0;
        for (int i = 0, j = -1; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                //到这里已经无法匹配括号了，上一轮结束，下一轮开始，将j定位至当前即可。
                if (stack.isEmpty()) {
                    j = i;
                } else {
                    stack.pop();
                    if (stack.isEmpty()) {
                        res = Math.max(res, i - j);
                    } else {
                        res = Math.max(res, i - stack.peek());
                    }
                }
            }
        }
        return res;
    }

    //125. 验证回文串
    public boolean isPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        int start = 0;
        int end = s.length() - 1;
        while (start <= end) {
            while (start < s.length() && !Character.isLetterOrDigit(s.charAt(start))) {
                start++;
            }
            while (end >= start && !Character.isLetterOrDigit(s.charAt(end))) {
                end--;
            }
            if (start > end) {
                return true;
            }
            char s1 = s.charAt(start);
            char s2 = s.charAt(end);
            if (s1 != s2 && Character.toLowerCase(s1) != Character.toLowerCase(s2)) {
                return false;
            } else {
                start++;
                end--;
            }
        }
        return true;
    }

    //392. 判断子序列
    public boolean isSubsequence(String s, String t) {
        int temp = 0;
        if (s == null || s.length() == 0) {
            return true;
        }
        if (t == null || t.length() == 0) {
            return false;
        }
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == s.charAt(temp)) {
                temp++;
                if (temp == s.length()) {
                    return true;
                }
            }
        }
        return temp == t.length();
    }

    //246. 中心对称数
    public boolean isStrobogrammatic(String num) {
        if (num == null || num.length() == 0) {
            return false;
        }
        HashMap<Character, Character> map = new HashMap<>();
        map.put('0', '0');
        map.put('1', '1');
        map.put('6', '9');
        map.put('8', '8');
        map.put('9', '6');
        int start = 0;
        int end = num.length() - 1;
        while (start <= end) {
            char chs = num.charAt(start);
            char che = num.charAt(end);
            if (!(map.containsKey(chs) && che == map.get(chs))) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    public String countAndSay(int n) {
        String res = "1";
        for (int i = 1; i < n; i++) {
            res = nextQoute(res);
        }
        return res;
    }

    public String nextQoute(String s) {
        int count = 0;
        char cur = s.charAt(0);
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        while (idx < s.length()) {
            cur = s.charAt(idx);
            while (idx < s.length() && s.charAt(idx) == cur) {
                count++;
                idx++;
            }
            sb.append((char) (count + '0')).append(cur);
            count = 0;
        }
        return sb.toString();
    }

    //48. 字母异位分词
    //这个是官方解，总的来说字母异位分词还是用计数排序来搞最优
    //但是官方的高明指出在于使用HashMap来进行不同类型分词的记录
    //并且使用每个单词对应的计数排序数组编码作为key(这样对每一类分词都有一个独一无二的key)
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> resMap = new HashMap<>();
        for (String os : strs) {
            int[] tmp = new int[26];
            for (char c : os.toCharArray()) {
                tmp[c - 'a']++;
            }
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i] > 0) {
                    sb.append((char) (i + 'a')).append("*").append(tmp[i]);
                }
            }
            if (!resMap.containsKey(sb.toString())) {
                resMap.put(sb.toString(), new ArrayList<>());
            }
            resMap.get(sb.toString()).add(os);
        }
        return new ArrayList<>(resMap.values());
    }

    //3. 无重复字符的最长子串
    public int lengthOfLongestSubstring2(String s) {
        int res = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int idx = 0;
        int cur = 0;
        while (idx < s.length()) {
            while (idx < s.length() && !map.containsKey(s.charAt(idx))) {
                map.put(s.charAt(idx), idx);
                idx++;
            }
            res = Math.max(res, map.size());
            if (idx == s.length()) {
                break;
            }
            int tmp = map.get(s.charAt(idx));
            for (int i = cur; i <= tmp; i++) {
                map.remove(s.charAt(i));
            }
            cur = tmp + 1;
        }
        return res;
    }

    //266. 回文排列
    public boolean canPermutePalindrome(String s) {
        int[] mode = new int[256];
        for (char ch :
                s.toCharArray()) {
            mode[ch]++;
        }
        boolean single = false;
        for (int t :
                mode) {
            if (single && t % 2 == 1) {
                return false;
            } else if (!single && t % 2 == 1) {
                single = true;
            }
        }
        return true;
    }

    //215. 数组中的第K个最大元素
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || k > nums.length) {
            return 0;
        }
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
        for (int i :
                nums) {
            queue.offer(i);
            if (queue.size() > k) {
                queue.poll();
            }
        }
        return queue.poll();
    }


    public int[][] merge2(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return intervals;
        }
        List<int[]> res = new ArrayList<>();
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        int start = intervals[0][0];
        int end = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= end) {
                end = Math.max(end, intervals[i][1]);
            } else {
                res.add(new int[]{start, end});
                start = intervals[i][0];
                end = intervals[i][1];
            }
        }
        res.add(new int[]{start, end});
        return res.toArray(new int[][]{});
    }

    //373. 查找和最小的K对数字
    //这个题的基调是优先队列不但给入，最后只去前k个即可，
    //但这个题时间复杂度的关键在于，遍历的方式：
    //我先后经历了全部遍历，仅遍历每个数组的前k个等方法：
    //但这些都不是最优的，下面这个才是最优的：
    //基本策略是，先将nums1中的前k个与num2中第0个组成par压入最小堆(同时将每个pair对应的nums2的index压入)；
    //之后逐步去除队列中的pair加入到返回列表中：
    //每次去一个pair，就针对这个pair，保持nums1索取数不变，num2所取数的index向右退役一位的形式，将对应pair及索引压入。
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums1.length == 0 || nums2.length == 0 || k == 0) {
            return res;
        }
        PriorityQueue<int[]> queue = new PriorityQueue<int[]>((o1, o2) -> o1[0] + o1[1] - o2[0] - o2[1]);
        for (int i = 0; i < nums1.length && i < k; i++) {
            queue.add(new int[]{nums1[i], nums2[0], 0});
        }
        while (!queue.isEmpty() && k-- > 0) {
            int[] cur = queue.poll();
            res.add(new ArrayList<>(Arrays.asList(cur[0], cur[1])));
            if (cur[2] == nums2.length - 1) continue;
            queue.add(new int[]{cur[0], nums2[cur[2] + 1], cur[2] + 1});
        }
        return res;
    }


    //88. 合并两个有序数组
    //这个鬼东西，逻辑本身很简单，就是从nums1的末端（合并后的索引）开始
    //从后向前，每次都取两个数组尾巴上的较大值，
    //如此这边，最终一定会有一个数组的数字先被取完，这个时候就直接将身下一个数组的数字补到开头即可。
    /*难点在于怎么将最开始臃肿的代码省略回到这个地方。*/
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx = m + n - 1;
        int idx1 = m - 1;
        int idx2 = n - 1;
        while (idx >= 0 & idx1 >= 0 && idx2 >= 0) {
            nums1[idx--] = nums1[idx1] >= nums2[idx2] ? nums1[idx1--] : nums2[idx2--];
        }
        while (idx >= 0) {
            nums1[idx--] = idx1 >= 0 ? nums1[idx1--] : nums2[idx2--];
        }
    }

    //347. 前 K 个高频元素
    public int[] topKFrequent(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0 || k == 0) {
            return res.stream().mapToInt(Integer::valueOf).toArray();
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
        for (int key : map.keySet()) {
            queue.offer(new int[]{key, map.get(key)});
            if (queue.size() > k) {
                queue.poll();
            }
        }
        while (!queue.isEmpty()) {
            res.add(queue.poll()[0]);
        }
        return res.stream().mapToInt(Integer::valueOf).toArray();
    }

    //46. 全排列
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        helper(res, new ArrayList<>(), nums);
        return res;
    }

    public void helper(List<List<Integer>> res, List<Integer> numsList, int[] nums) {
        if (numsList.size() == nums.length) {
            res.add(new ArrayList<>(numsList));
            return;
        }
        for (int num :
                nums) {
            if (numsList.contains(num)) {
                continue;
            }
            numsList.add(num);
            helper(res, numsList, nums);
            numsList.remove(numsList.size() - 1);
        }
    }

    //17. 电话号码的字母组合
    public List<String> letterCombinations2(String digits) {
        List<String> res = new ArrayList<>();
        if (digits == null || digits.length() == 0) {
            return res;
        }
        int count = digits.length();
        StringBuilder sb = new StringBuilder();
        getDigitHelp(res, sb, 0, digits);
        return res;
    }

    public void getDigitHelp(List<String> res, StringBuilder sb, int count, String digit) {
        if (count == digit.length()) {
            res.add(sb.toString());
            System.out.println(sb.toString());
            return;
        }
        char c = digit.charAt(count);
        List<Character> test = testList.get(c - '2');
        count++;
        for (char ch :
                test) {
            sb.append(ch);
            getDigitHelp(res, sb, count, digit);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    //78. 子集
    public List<List<Integer>> subsets2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        List<Integer> cache = new ArrayList<>();
        subSetsHelper(res, nums, cache, 0);
        return res;
    }

    public void subSetsHelper(List<List<Integer>> res, int[] nums, List<Integer> cache, int curIdx) {
        res.add(new ArrayList<>(cache));
        for (int i = curIdx; i < nums.length; i++) {
            cache.add(nums[i]);
            subSetsHelper(res, nums, cache, i + 1);
            cache.remove(cache.size() - 1);
        }
    }

    //77. 组合
    //这个还是基本的回溯法（剪枝dfs），但是这个题有个小技巧
    //多加一个越界判断，可以省很多时间复杂度
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (k == 0 || n == 0) {
            return res;
        }
        combineHelper(res, new ArrayList<Integer>(), k, n, 1);
        return res;
    }

    public void combineHelper(List<List<Integer>> res, List<Integer> cache, int remainCount, int base,
                              int idx) {
        if (remainCount == 0) {
            res.add(new ArrayList<>(cache));
            return;
        }
        //越界判断在这里。
        if (idx > base - remainCount + 1) {
            return;
        }
        for (int i = idx; i <= base; i++) {
            cache.add(i);
            combineHelper(res, cache, remainCount - 1, base, i + 1);
            cache.remove(cache.size() - 1);
        }
    }

    //47. 全排列 II
    //这个地方顺手改进了46题普通全排列的算法
    //原来的回溯法在进行剪枝dfs时，为了方便最后一步剪枝，必须使用list结构存储已挑选的元素(这样只需直接删除list的最后一个元素即可)
    //这就引入了新的问题，排列过程需要判断当前元素是否已经选中过，如果用list，需要遍历判断，所需时间过长
    //由此引入一个新的机制，直接使用一个list存储全部元素，使用一个指针指向当前已经选中的元素个数对应位置（如当前以选中3个，则指针指向第三个元素）
    //在进行下一选择时，可直接从第3+1个元素开始遍历选起，每次选择完毕以后，将选中的元素与第3+1个元素交换，即可维持该数据结构。
    //之后在进行剪枝的时候，就可以方向操作之前的交换即可，这样做的另一个好处就是，用来标记的这个list在遍历完一边后，可直接加入答案数组。
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        List<Integer> tags = new ArrayList<>();
        for (int num :
                nums) {
            tags.add(num);
        }

        permuteUniqueHelper(res, tags, 0, 0);
        return res;
    }

    public void permuteUniqueHelper(List<List<Integer>> res, List<Integer> tags, int curIdx, int n) {
        if (n == tags.size()) {
            res.add(new ArrayList<>(tags));
        }
        Set<Integer> set = new HashSet<>();
        for (int i = curIdx; i < tags.size(); i++) {
            if (set.contains(tags.get(i))) {
                continue;
            }
            set.add(tags.get(i));
            Collections.swap(tags, curIdx, i);
            permuteUniqueHelper(res, tags, curIdx + 1, n + 1);
            Collections.swap(tags, curIdx, i);
        }
    }

    //60. 排列序列
    //丢，这题的回溯主要考察的还是逻辑思维能力把。
    //基本思路是，对于n个数的全排列而言，首位的会有n种可能行
    //且每一种可能性都有(n - 1)!种情况。
    //由此，对于一个n的全排列种的第k个数，其首位数字是可以计算出来的。
    //首位数字计算完成后，问题进一步转为为一个子问题
    //如此以此类推，递归调用即可得到最终的结果。
    public String getPermutation(int n, int k) {
        StringBuilder sb = new StringBuilder();
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        getPermutationHelper(sb, list, n, k, n, getRanking(n));
        return sb.toString();
    }

    public void getPermutationHelper(StringBuilder sb, List<Integer> cache, int n, int k, int base, int cacheRank) {
        if (sb.length() == base) {
            return;
        }
        int x = n != 0 ? cacheRank / n : 1;
        int rank1 = (k - 1) / x;
        int rank2 = (k - 1) % x;
        sb.append(cache.remove(rank1));
        getPermutationHelper(sb, cache, n - 1, rank2 + 1, base, x);
    }


    public int getRanking(int n) {
        if (n == 1 || n == 0) {
            return 1;
        } else {
            return n * getRanking(n - 1);
        }
    }

    //90. 子集2
    //基本套路与子集1几乎完全相同，加了个重复条件的秘诀就在于，在同一层级的遍历中排除相同元素即可。还有就是要注意需要排序的。
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        Arrays.sort(nums);
        subsetsWithDupHelper(res, new ArrayList<Integer>(), nums, 0);
        return res;
    }

    public void subsetsWithDupHelper(List<List<Integer>> res, List<Integer> cache, int[] nums, int curIdx) {
        res.add(new ArrayList<>(cache));
        for (int i = curIdx; i < nums.length; i++) {
            if (i > curIdx && nums[i] == nums[i - 1]) {
                continue;
            }
            cache.add(nums[i]);
            subsetsWithDupHelper(res, cache, nums, i + 1);
            cache.remove(cache.size() - 1);
        }
    }

    //39. 组合总和
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (candidates == null || candidates.length == 0) {
            return res;
        }
        Arrays.sort(candidates);
        combinationSumHelper(res, new ArrayList<Integer>(), candidates, target, 0);
        return res;
    }

    public void combinationSumHelper(List<List<Integer>> res, List<Integer> cache, int[] candidates, int target, int curIdx) {
        if (target == 0) {
            res.add(new ArrayList<>(cache));
            return;
        } else if (target < 0) {
            return;
        }
        if (curIdx > candidates.length - 1) {
            return;
        }
        combinationSumHelper(res, cache, candidates, target, curIdx + 1);
        if (target - candidates[curIdx] >= 0) {
            cache.add(candidates[curIdx]);
            combinationSumHelper(res, cache, candidates, target - candidates[curIdx], curIdx);
            cache.remove(cache.size() - 1);
        }
    }

    //40. 组合总和 II(我的算法)
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (candidates == null || candidates.length == 0) {
            return res;
        }
        Arrays.sort(candidates);
        combination2SumHelper(res, new ArrayList<>(), candidates, target, 0);
        return res;
    }

    public void combination2SumHelper(List<List<Integer>> res, List<Integer> cache, int[] candidates, int target, int curIdx) {
        if (target == 0) {
            res.add(new ArrayList<>(cache));
            return;
        }
        if (target < 0 || curIdx > candidates.length - 1) {
            return;
        }
        for (int i = curIdx; i < candidates.length; i++) {
            if (i > curIdx && candidates[i] == candidates[i - 1]) {
                continue;
            }
            cache.add(candidates[i]);
            combination2SumHelper(res, cache, candidates, target - candidates[i], i + 1);
            cache.remove(cache.size() - 1);
        }
    }

    //40. 组合总和 II(优化算法)
    List<int[]> freq = new ArrayList<int[]>();
    List<List<Integer>> ans = new ArrayList<List<Integer>>();
    List<Integer> sequence = new ArrayList<Integer>();

    public List<List<Integer>> combinationSum21(int[] candidates, int target) {
        Arrays.sort(candidates);
        for (int num : candidates) {
            int size = freq.size();
            if (freq.isEmpty() || num != freq.get(size - 1)[0]) {
                freq.add(new int[]{num, 1});
            } else {
                ++freq.get(size - 1)[1];
            }
        }
        dfs(0, target);
        return ans;
    }

    public void dfs(int pos, int rest) {
        if (rest == 0) {
            ans.add(new ArrayList<Integer>(sequence));
            return;
        }
        if (pos == freq.size() || rest < freq.get(pos)[0]) {
            return;
        }

        dfs(pos + 1, rest);

        int most = Math.min(rest / freq.get(pos)[0], freq.get(pos)[1]);
        for (int i = 1; i <= most; ++i) {
            sequence.add(freq.get(pos)[0]);
            dfs(pos + 1, rest - i * freq.get(pos)[0]);
        }
        for (int i = 1; i <= most; ++i) {
            sequence.remove(sequence.size() - 1);
        }
    }

    //216. 组合总和 III
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        combinationSum3Helper(res, new ArrayList<>(), n, k, 1);
        return res;
    }

    public void combinationSum3Helper(List<List<Integer>> res, List<Integer> cache, int target, int count, int curValue) {
        if (target == 0 && count == 0) {
            res.add(new ArrayList<>(cache));
            return;
        }
        if (target > count * 9 || target < count) {
            return;
        }
        if (curValue > 9) {
            return;
        }
        for (int i = curValue; i < 10; i++) {
            cache.add(i);
            combinationSum3Helper(res, cache, target - i, count - 1, i + 1);
            cache.remove(cache.size() - 1);
        }
    }

    //351. 安卓系统手势解锁
    private int mRes;
    private List<List<Integer>> testRes = new ArrayList<>();
    private HashMap<Integer, int[]> standardMap = new HashMap<>();

    {
        standardMap.put(1, new int[]{1, 1});
        standardMap.put(2, new int[]{1, 2});
        standardMap.put(3, new int[]{1, 3});
        standardMap.put(4, new int[]{2, 1});
        standardMap.put(5, new int[]{2, 2});
        standardMap.put(6, new int[]{2, 3});
        standardMap.put(7, new int[]{3, 1});
        standardMap.put(8, new int[]{3, 2});
        standardMap.put(9, new int[]{3, 3});
    }


    public int numberOfPatterns(int m, int n) {
        List<Integer> cache = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            cache.add(i);
        }
        mRes = 0;
        numberOfPatternsHelper(cache, m, n, 0, 0);
        return mRes;
    }

    public void numberOfPatternsHelper(List<Integer> cache, int max, int min, int curIdx, int curP) {
        if (curIdx >= min && curIdx <= max) {
            mRes++;
            testRes.add(new ArrayList<>(cache.subList(0, (Math.min(curIdx, cache.size())))));
        } else if (curIdx > max) {
            return;
        }
        for (int i = curIdx; i < cache.size(); i++) {
            if (curIdx == 1) {
                int x = 2;
            }
            if (curIdx == 0 || isPatternValid(cache, cache.get(i), curIdx)) {
                Collections.swap(cache, i, curIdx);
                numberOfPatternsHelper(cache, max, min, curIdx + 1, i + 1);
                Collections.swap(cache, i, curIdx);
            }
        }
    }

    public boolean isPatternValid(List<Integer> cache, int end, int curIdx) {
        int start = cache.get(curIdx - 1);
        HashSet<Integer> set = new HashSet<>(cache.subList(0, curIdx));
        boolean isTest = false;
        int[] startAx = standardMap.get(start);
        int[] endAx = standardMap.get(end);
        isTest = (Math.max(Math.abs(startAx[0] - endAx[0]), Math.abs(startAx[1] - endAx[1]))) > 1;
        if (isTest && (start + end) % 2 == 0) {
            return set.contains((start + end) / 2);
        } else {
            return !set.contains(end);
        }
    }


    //112. 路径总和
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }
        if (root.left == null && root.right == null && targetSum == root.val) {
            return true;
        }
        if (root.left != null || root.right != null) {
            return hasPathSum(root.left, targetSum - root.val)
                    || hasPathSum(root.right, targetSum - root.val);
        }
        return false;
    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] test = new int[]{10,1,2,7,6,1,5};
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        System.out.println(solution.hasPathSum(root, 3));
    }

    public static ListNode generateListNodes(int[] nums) {
        ListNode head = new ListNode(nums[0]);
        ListNode retHead = head;
        for (int i = 1; i < nums.length; i++) {
            head.next = new ListNode(nums[i]);
            head = head.next;
        }
        return retHead;
    }

    public static void printListNodes(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
    }

    public static void printList(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
            ;
        }
    }
}
