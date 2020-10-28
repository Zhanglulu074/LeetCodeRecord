import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.tools.javadoc.Start;
import javafx.util.Pair;
import org.omg.PortableInterceptor.INACTIVE;
import sun.tools.jstack.JStack;

import java.util.*;
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

    //有效的括号
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
        for (int i = 0; i < starts.length; i++) {
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


    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] test = {{1, 3}};
        int[] test1 = {-1, 1};
//        List<List<Integer>> list = solution.subsets(test);
//        for (List<Integer> list1 : list) {
//            for (int a : list1) {
//                System.out.print(a + " ");
//            }
//            System.out.println("");
//        }
        int[][] res = solution.insert(test, test1);
        for (int[] num : res) {
            System.out.println(num[0] + " " + num[1]);
        }
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
