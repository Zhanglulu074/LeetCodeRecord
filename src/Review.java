import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Review {

    /*-------------------数组-------------------*/
    /*-------------------双指针-------------------*/
    //LC.11. 盛最多水的容器
    public int maxArea(int[] height) {
        int res = 0;
        int start = 0;
        int end = height.length - 1;
        while (start < end) {
            int startH = height[start];
            int endH = height[end];
            res = Math.max(res, Math.min(startH, endH) * (end - start));
            if (startH <= endH) {
                start++;
            } else {
                end--;
            }
        }
        return res;
    }

    //LC.75 颜色分类
    public void sortColors(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        int start = 0, end = nums.length - 1;
        for (int i = 0; i <= end; i++) {
            while (nums[i] == 2 && end > i) {
                swap(nums, i, end--);
            }
            if (nums[i] == 0) {
                swap(nums, i, start++);
            }
        }
    }


    //287. 寻找重复数
//    public int findDuplicate(int[] nums) {
//        for (int i = 0; i < nums.length - 1; i++) {
//            while(nums[i] - 1 != i) {
//                if (nums[i] == nums[nums[i] - 1]) {
//                    return nums[i];
//                }
//                swap(nums, nums[i] - 1, i);
//
//            }
//        }
//        return nums[nums.length - 1];
//    }

    public int findDuplicate(int[] nums) {
        int slow = 0, fast = 0;
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);
        slow = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }

    public void swap(int[] nums, int a, int b) {
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    //387. 字符串中的第一个唯一字符
    public int firstUniqChar(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int N = s.length();
        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);
            if (!map.containsKey(c)) {
                map.put(c, i);
            } else {
                map.put(c, -1);
            }
        }
        int res = N;
        for (char c :
                map.keySet()) {
            int te = map.get(c);
            if (te != -1 && te < res) {
                res = te;
            }
        }
        return res < N ? res : -1;
    }

    //347. 前 K 个高频元素
    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.containsKey(nums[i]) ?
                    (map.get(nums[i]) + 1) : 1);
        }
        Queue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<Map.Entry<Integer, Integer>>(new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        for (Map.Entry<Integer, Integer> entry :
                map.entrySet()) {
            queue.offer(entry);
            if (queue.size() > k) {
                queue.poll();
            }
        }
        int[] res = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            res[i] = queue.poll().getKey();
        }
        return res;
    }

    public int shortestDistance(String[] words, String word1, String word2) {
        int idx1 = -1;
        int idx2 = -1;
        int res = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                idx1 = i;
            } else if (words[i].equals(word2)) {
                idx2 = i;
            }
            if (idx1 >= 0 && idx2 >= 0) {
                res = Math.min(res, Math.abs(idx2 - idx1));
            }
        }
        return res;
    }


    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int curIdx = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[curIdx]) {
                nums[++curIdx] = nums[j];
            }
        }
        return curIdx + 1;
    }


    //80. 删除排序数组中的重复项 II
    public int removeDuplicatesII(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int curIdx = 0;
        int curLen = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[curIdx] || curLen < 2) {
                if (nums[i] != nums[curIdx]) {
                    curLen = 0;
                }
                nums[++curIdx] = nums[i];
                curLen++;
            }
        }
        return curIdx + 1;
    }

    //56. 合并区间
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return 0;
            }
        });
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        List<int[]> resList = new ArrayList<>();
        int[] cur = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            int[] test = intervals[i];
            if (test[0] > cur[1]) {
                resList.add(cur);
                cur = test;
            } else {
                cur[1] = Math.max(test[1], cur[1]);
            }
        }
        resList.add(cur);
        int[][] res = new int[resList.size()][2];
        int i = 0;
        for (int[] s :
                resList) {
            res[i++] = s;
        }
        return res;
    }

    //57. 插入区间
    public int[][] insert(int[][] intervals, int[] newInterval) {
        if (intervals == null || intervals.length == 0) {
            return new int[][]{newInterval};
        }
        boolean placed = false;
        int left = newInterval[0];
        int right = newInterval[1];
        List<int[]> resList = new ArrayList<>();
        for (int[] inter : intervals) {
            if (inter[0] > right) {
                if (!placed) {
                    resList.add(new int[]{left, right});
                    placed = true;
                }
                resList.add(inter);
            } else if (inter[1] < left) {
                resList.add(inter);
            } else {
                left = Math.min(inter[0], left);
                right = Math.max(inter[1], right);
            }
        }
        if (!placed) {
            resList.add(new int[]{left, right});
        }
        int[][] res = new int[resList.size()][2];
        int i = 0;
        for (int[] s :
                resList) {
            res[i++] = s;
        }
        return res;
    }

    //228. 汇总区间
    public List<String> summaryRanges(int[] nums) {
        List<String> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        int start = nums[0];
        int end = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] + 1) {
                end = nums[i];
            } else {
                res.add(start == end ? String.valueOf(start) : "" + start + "->" + end);
                start = nums[i];
                end = start;
            }
        }
        res.add(start == end ? String.valueOf(start) : "" + start + "->" + end);
        return res;
    }

    //剑指 Offer 42. 连续子数组的最大和
    public int maxSubArray(int[] nums) {
        int res = nums[0];
        int a = nums[0], b = 0;
        for (int i = 1; i < nums.length; i++) {
            b = Math.max(a + nums[i], nums[i]);
            res = Math.max(res, b);
            a = b;
        }
        return res;
    }

    //325. 和等于 k 的最长子数组长度
    public int maxSubArrayLen(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        int[] sums = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            sums[i + 1] = sums[i] + nums[i];
            map.put(sums[i + 1], i + 1);
        }
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(sums[i] + k)) {
                res = Math.max(res, map.get(sums[i] + k) - i + 1);
            }
        }
        return res;
    }

    //209. 长度最小的子数组
    public int minSubArrayLen(int s, int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int start = 0;
        int tmp = 0;
        int res = nums.length + 1;
        for (int i = 0; i < nums.length; i++) {
            tmp += nums[i];
            while (tmp >= s) {
                res = Math.min(res, i - start + 1);
                tmp -= nums[start++];
            }
        }
        return res == nums.length + 1 ? res : 0;
    }

    //238. 除自身以外数组的乘积
    public int[] productExceptSelf(int[] nums) {
        int[] products = new int[nums.length];
        products[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            products[i] = products[i - 1] * nums[i - 1];
        }
        int tmp = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            tmp *= nums[i + 1];
            products[i] *= tmp;
        }
        return products;
    }


    //152. 乘积最大子数组
    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = nums[0];
        int aMax = nums[0], bMax = 0;
        int aMin = nums[0], bMin = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] >= 0) {
                bMax = Math.max(nums[i], aMax * nums[i]);
                bMin = Math.min(nums[i], aMin * nums[i]);
            } else {
                bMax = Math.max(nums[i], aMin * nums[i]);
                bMin = Math.min(nums[i], aMax * nums[i]);
            }
            res = Math.max(res, bMax);
            aMax = bMax;
            aMin = bMin;
        }
        return res;
    }

    //插入排序
    public int[] insertSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int tmp = nums[i];
            int j = i - 1;
            while (j >= 0 && nums[j] >= tmp) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = tmp;
        }
        return nums;
    }

    //选择排序
    public int[] selectSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int tmp = nums[i];
            int target = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (tmp > nums[j]) {
                    tmp = nums[j];
                    target = j;
                }
            }
            nums[target] = nums[i];
            nums[i] = tmp;
        }
        return nums;
    }

    //冒泡排序(带优化)
    //循环两两比较，将小的交换到前面，直到无法交换为止
    public int[] bubbleSort(int[] nums) {
        boolean sorted = false;
        for (int i = nums.length - 1; i >= 1 && !sorted; i--) {
            sorted = true;
            for (int j = 1; j <= i; j++) {
                if (nums[j - 1] > nums[j]) {
                    int tmp = nums[j - 1];
                    nums[j - 1] = nums[j];
                    nums[j] = tmp;
                    sorted = false;
                }
            }
        }
        return nums;
    }

    //归并排序
    public int[] mergedSort(int[] nums) {
        if (nums.length <= 1) {
            return nums;
        }
        int len = nums.length;
        int mid = len / 2;
        int[] left = Arrays.copyOfRange(nums, 0, mid);
        int[] right = Arrays.copyOfRange(nums, mid, nums.length);
        left = mergedSort(left);
        right = mergedSort(right);
        int a = 0;
        int b = 0;
        int i = 0;
        while (a < left.length && b < right.length) {
            if (left[a] >= right[b]) {
                nums[i++] = right[b++];
            } else {
                nums[i++] = left[a++];
            }
        }
        if (a == left.length) {
            for (; i < nums.length; i++) {
                nums[i] = right[b++];
            }
        }
        if (b == right.length) {
            for (; i < nums.length; i++) {
                nums[i++] = left[a++];
            }
        }
        return nums;
    }

    //快速排序
    public int[] QuickSort(int[] nums) {
        QuickSortHelper(nums, 0, nums.length - 1);
        return nums;
    }

    public void QuickSortHelper(int[] nums, int l, int r) {
        if (r <= l + 1) {
            return;
        }
        int tail = nums[r];
        int start = l - 1;
        for (int i = l; i <= r - 1; i++) {
            if (nums[i] <= tail) {
                swap(nums, ++start, i);
            }
        }
        swap(nums, r, start + 1);
        QuickSortHelper(nums, l, start);
        QuickSortHelper(nums, start + 2, r);
    }

    public void swapInArray(int[] nums, int a, int b) {
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    //堆排序
    public int[] HeepSort(int[] nums) {
        int edge = (nums.length - 1) / 2;
        for (int i = edge; i >= 0; i--) {
            fixHeep(nums, i, nums.length);
        }
        for (int i = nums.length - 1; i >= 1; i--) {
            swapInArray(nums, 0, i);
            fixHeep(nums, 0, i);
        }
        return nums;
    }

    public void fixHeep(int[] nums, int root, int length) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;
        if (left < length && nums[left] > nums[largest]) {
            largest = left;
        }
        if (right < length && nums[right] > nums[largest]) {
            largest = right;
        }
        if (largest != root) {
            swapInArray(nums, largest, root);
            fixHeep(nums, largest, length);
        }
    }

    public int left(int n) {
        return 2 * n + 1;
    }

    public int right(int n) {
        return 2 * n + 2;
    }


    ///字符串题目

    //童字母异序词判断
    public boolean isAnagrame(String s1, String s2) {
        int[] count = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            count[s1.charAt(i) - 'a']++;
            count[s2.charAt(i) - 'a']--;
        }
        for (int num :
                count) {
            if (num != 0) {
                return false;
            }
        }
        return true;
    }


    //字符串题型1：子串 subString
    //LC.3 最长不重复子串
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Map<Character, Integer> map = new HashMap<>();
        int p2 = -1;
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                p2 = Math.max(p2, map.get(s.charAt(i)));
            }
            map.put(s.charAt(i), i);
            res = Math.max(res, i - p2);
        }
        return res;
    }

    //159. 至多包含两个不同字符的最长子串
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (s.length() < 3) {
            return s.length();
        }
        int left = 0;
        int right = 0;
        int res = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        while (right < s.length()) {
            map.put(s.charAt(right), right++);
            if (map.size() <= 2) {
                res = Math.max(res, right - left);
            }
            if (map.size() == 3) {
                int newLeft = Collections.min(map.values());
                map.remove(s.charAt(newLeft));
                left = newLeft + 1;
                res = Math.max(res, right - left);
            }
        }
        return res;
    }


    //32. 最长有效括号
    //这个题木始终把握一个原则，任意时刻stack里边存储的都是当前还未配对的(的索引，并且
    //当前考察点i与stack中所有元素之间的)均已被配对清除。
    public int longestValidParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        //当前考察子串始终为(j, i]
        int res = 0;
        for (int i = 0, j = -1; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                //这个好理解，发现一个)却没有(与之配对，那本轮结束，将j指向i，重新开始下一轮计数
                if (stack.isEmpty()) {
                    j = i;
                } else {
                    stack.pop();
                    //pop之后即为空，代表本轮为完美配对，长度为i - j
                    if (stack.isEmpty()) {
                        res = Math.max(res, i - j);
                    } else {
                        //pop以后不为空，说明本轮到目前位置，前面仍有一个未配对的（，那么，从这个元素开始
                        //到i为止的元素序列才是当前符合要求的序列
                        res = Math.max(res, i - stack.peek());
                    }
                }
            }
        }
        return res;
    }

    //22. 括号生成
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        totalLeft = n;
        generateParenthesisHelper(res, new StringBuilder(), n * 2, 0);
        return res;
    }

    private int totalLeft = 0;

    public void generateParenthesisHelper(List<String> res, StringBuilder sb, int total, int leftCnt) {
        if (total == 0) {
            res.add(sb.toString());

        }
        if (totalLeft > 0) {
            sb.append('(');
            totalLeft--;
            generateParenthesisHelper(res, sb,
                    total - 1, leftCnt + 1);
            sb.delete(sb.length() - 1, sb.length());
            totalLeft++;
        }
        if (leftCnt > 0) {
            sb.append(')');
            generateParenthesisHelper(res, sb,
                    total - 1, leftCnt - 1);
            sb.delete(sb.length() - 1, sb.length());
        }
    }

    //Lc 125. 验证回文串
    public boolean isPalindrome(String s) {
        int start = 0, end = s.length() - 1;
        while (start <= end) {
            while (start < end && !Character.isLetterOrDigit(s.charAt(start))) {
                start++;
            }
            while (end > start && !Character.isLetterOrDigit(s.charAt(end))) {
                end--;
            }
            char s1 = s.charAt(start);
            char s2 = s.charAt(end);
            if (Character.toLowerCase(s1) != Character.toLowerCase(s2)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    //火星人运算符
    public int marsCalculate(String s) {
        Stack<Integer> numStack = new Stack<>();
        Stack<Character> charStack = new Stack<>();
        int i = 0;
        while (i < s.length()) {
            if (Character.isDigit(s.charAt(i))) {
                int cur = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    cur = cur * 10 + s.charAt(i) - '0';
                    i++;
                }
                if (!charStack.isEmpty() && !numStack.isEmpty() && charStack.peek() == '$') {
                    charStack.pop();
                    int past = numStack.pop();
                    int res = past * 3 + cur + 2;
                    numStack.push(res);
                } else {
                    numStack.push(cur);
                }
            }
            if (i >= s.length()) {
                break;
            }
            if (s.charAt(i) == '$' || s.charAt(i) == '#') {
                if (numStack.isEmpty()) {
                    return -1;
                }
                charStack.push(s.charAt(i));
            }
            i++;
        }
        if (charStack.isEmpty()) {
            return numStack.pop();
        }
        int cur = 0;
        int past = numStack.get(0);
        for (int j = 1; j < numStack.size(); j++) {
            cur = numStack.get(j);
            cur = 2 * past + 3 * cur + 4;
            past = cur;
        }
        return cur;
    }

    /*--------------------------------链表题--------------------------------*/

    public void deleteNode(ListNode node) {
        if (node == null) return;
        node.val = node.next.val;
        node.next = node.next.next;
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.next.val == cur.val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    public ListNode deleteAllDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        while (head != null && head.next != null) {
            int cnt = 0;
            int curVal = head.val;
            while (head.next != null && head.next.val == curVal) {
                head = head.next;
                cnt++;
            }
            if (cnt >= 1) {
                pre.next = head.next;
            } else {
                pre.next = head;
                pre = pre.next;
            }
            head = head.next;
        }
        return dummy.next;
    }


    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur.next != null) {
            ListNode tmp = cur.next;
            cur.next = tmp.next;
            tmp.next = pre.next;
            pre.next = tmp;
        }
        return dummy.next;
    }

    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    public int[] nextGreaterElements(int[] nums) {
        Stack<Integer> stack = new Stack();
        int len = nums.length;
        int[] res = new int[nums.length];
        for (int i = 0; i < 2 * nums.length - 1; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i % len]) {
                res[stack.pop()] = nums[i % len];
                i++;
            }
        }
        return res;
    }

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

    class MyQueue {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        public MyQueue() {
        }

        public void push(int i) {
            stack1.push(i);
        }

        public int pop() {
            if (!stack2.isEmpty()) {
                return stack2.pop();
            }
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
            return stack2.pop();
        }

    }

    class LRUCache {

        private int capacity = 0;
        private int curCap = 0;
        private Map<Integer, Node> map = new HashMap();
        private Node head = null;
        private Node last = null;
        class Node {

            public int  val = 0;
            private int key = 0;
            public Node prev = null;
            public Node next = null;

            Node(int a, int b) {
                val = b;
                key = a;
            }
        }

        public LRUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            if (!map.containsKey(key)) {
                return -1;
            }
            Node node = map.get(key);
//            if (head == node) {
//                return node.val;
//            } else {
//                if (last == node) {
//                    node.prev.next = null;
//                    last = node.prev;
//                } else {
//                    node.prev.next = node.next;
//                    node.next.prev = node.prev;
//                }
//                node.next = head;
//                node.next.prev = node;
//                head = node;
//                node.prev = null;
//                return node.val;
//            }
            moveToHead(node);
            return node.val;
        }

        private void moveToHead(Node node) {
            if (head != node) {
                if (last == node) {
                    node.prev.next = null;
                    last = node.prev;
                } else {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
                node.next = head;
                node.next.prev = node;
                head = node;
                node.prev = null;
            }
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                map.get(key).val = value;
                moveToHead(map.get(key));
                return;
            }
            Node node = new Node(key, value);
            map.put(key, node);
            if (head == null) {
                head = node;
                last = node;
            } else {
                node.next = head;
                node.prev = null;
                head.prev = node;
                head = node;
            }
            curCap ++;
            while (curCap > capacity) {
                map.remove(last.key);
                Node tmp = last.prev;
                last.prev.next = null;
                last.prev = null;
                last = tmp;
                curCap--;
            }
        }

    }

    public TreeNode mRoot = null;

    public boolean isValidBST(TreeNode root) {
        mRoot = root;
        return isValidBSTHelper(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public boolean isValidBSTHelper(TreeNode root, long lower, long upper) {
        if (root == null) {
            return true;
        }
        return (((root.val > lower) && (root.val < upper)) || root == mRoot)
                && isValidBSTHelper(root.left, lower, Math.min(root.val, upper))
                && isValidBSTHelper(root.right, Math.max(root.val, lower), upper);
    }

    public List<int[]> towSum2(int[] nums, int sum) {
        List<int[]> list = new ArrayList<>();
        Arrays.sort(nums);
        int start = 0, end = nums.length - 1;
        while (start < end) {
            while (nums[start] + nums[end] < sum) {
                start++;
            }
            if (start >= end) {
                break;
            }
            if (nums[start] + nums[end] == sum) {
                list.add(new int[] {nums[start], nums[end]});
                start++;
                end--;
            }
        }
        return list;
    }

    public int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; ++i) {
            if (hashtable.containsKey(target - nums[i])) {
                return new int[]{hashtable.get(target - nums[i]), i};
            }
            hashtable.put(nums[i], i);
        }
        return new int[0];
    }

//    public List<Integer> traversal2(TreeNode root) {
//        List<Integer> res = new ArrayList<>();
//        if (root == null) {
//
//        }
//    }


    private Map<Integer, Integer> map = new HashMap();

    public int[] findOrder (int[] preOrder, int[] inOrder) {
        // write code here
        for (int i = 0; i < inOrder.length; i++) {
            map.put(inOrder[i], i);
        }
        List<Integer> resList = new ArrayList();
        int rootVal = findOrderHelper(preOrder, inOrder, 0, preOrder.length - 1, 0, preOrder.length - 1, resList);
        resList.add(rootVal);
        int[] res = new int[inOrder.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = resList.get(i);
        }
        return res;
    }

    public int findOrderHelper(int[] preOrder, int[] inOrder, int preL,
                               int preR, int inL, int inR, List<Integer> res) {
        if (preL >= preR) {
            return preOrder[preL];
        }
        int rootVal = preOrder[preL];
        int rootInIdx = map.get(rootVal);
        int leftSize = rootInIdx - inL;
        int rightSize = inR - rootInIdx;
        int left = findOrderHelper(preOrder, inOrder, preL + 1, preL  + leftSize, inL, rootInIdx - 1, res);
        int right = findOrderHelper(preOrder, inOrder, preL + leftSize + 1, preR, rootInIdx + 1, inR, res);
        res.add(left);
        res.add(right);
        return rootVal;
    }

    public static void main(String[] args) {
        Review review = new Review();
        int[] pre = {1, 2, 3};
        int[] after = {2, 1, 3};
        review.findOrder(pre, after);
    }

    //层序遍历(BFS)
    public void levelOderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        StringBuilder sb = new StringBuilder();
        TreeNode last = root, nLast = root;
        while (!queue.isEmpty()) {
            TreeNode temp = queue.poll();
            sb.append(temp.val).append(" ");
            if (temp.left != null) {
                queue.offer(temp.left);
                nLast = temp.left;
            }
            if (temp.right != null) {
                queue.offer(temp.right);
                nLast = temp.right;
            }
            if (temp == last) {
                last = nLast;
                sb.append("\n");
            }
        }
        System.out.println(sb.toString());
    }


    //后序遍历
    public void afterOderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        TreeNode cur = root;
        TreeNode prev = null;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            if (!stack.isEmpty()) {
                TreeNode temp = stack.peek();
                if (temp.right == null || prev == temp.right) {
                    stack.pop();
                    sb.append(temp.val);
                    prev = temp;
                } else {
                    cur = temp.right;
                }
            }
        }
        System.out.println(sb.toString());
    }

    //中序遍历：
    public void midOrderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            if (!stack.isEmpty()) {
                TreeNode tmp = stack.pop();
                sb.append(tmp.val);
                cur = tmp.right;
            }
        }
        System.out.println(sb.toString());
    }

    //先序列遍历：
    public void preOrderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            TreeNode tmp = stack.pop();
            sb.append(tmp.val);
            if (tmp.right != null) {
                stack.push(tmp.right);
            }
            if (tmp.left != null) {
                stack.push(tmp.left);
            }
        }
        System.out.println(sb.toString());
    }

    public boolean isBalanced(TreeNode root) {
        if (root != null) {
            return getHeight2(root) != -1;
        }
        return true;
    }

    public int getHeight(TreeNode root) {
        if (root != null) {
            return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        }
        return 0;
    }

    public int getHeight2(TreeNode root) {
        if (root != null) {
            int leftHeight = getHeight2(root.left);
            int rightHeight = getHeight2(root.right);
            if (leftHeight == -1
                    || rightHeight == -1
                    || Math.abs(leftHeight - rightHeight) > 1) {
                return -1;
            } else {
                return Math.max(leftHeight, rightHeight) + 1;
            }
        }
        return 0;
    }

    public static void printArray(int[] input) {
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + " ");
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
}
