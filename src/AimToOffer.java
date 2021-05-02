import javax.sound.sampled.Line;
import java.nio.channels.Pipe;
import java.util.*;

public class AimToOffer {

    public int findRepeatNumber(int[] nums) {
        int[] set = new int[nums.length];
        for (int num : nums) {
            set[num]++;
            if (set[num] > 1) {
                return num;
            }
        }
        return 0;
    }

    //剑指 Offer 04. 二维数组中的查找
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int startX = matrix[0].length - 1;
        int startY = 0;
        while (startX >= 0 && startY <= matrix.length - 1) {
            if (matrix[startY][startX] > target) {
                startX--;
            } else if (matrix[startY][startX] < target) {
                startY++;
            } else {
                return true;
            }
        }
        return false;
    }

    //剑指 Offer 05. 替换空格
    public String replaceSpace(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c :
                s.toCharArray()) {
            if (c == ' ') {
                sb.append("%20");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //剑指 Offer 06. 从尾到头打印链表
    public int[] reversePrint(ListNode head) {
        if (head == null) {
            return new int[0];
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        int size = 0;
        while (head.next != null) {
            ListNode temp = head.next;
            head.next = temp.next;
            temp.next = dummy.next;
            dummy.next = temp;
            size++;
        }
        int[] res = new int[size + 1];
        ListNode start = dummy.next;
        for (int i = 0; i < res.length; i++) {
            res[i] = start.val;
            start = start.next;
        }
        return res;
    }

    //剑指 Offer 10- I. 斐波那契数列
    public int fib(int n) {
        int a = 1;
        int b = 1;
        int k = 1;
        while (k <= n) {
            int tmp = b;
            b = (a + b) % 1000000007;
            a = tmp;
            k++;
        }
        return a;
    }

    //剑指 Offer 11. 旋转数组的最小数字
    public int minArray(int[] numbers) {
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < numbers.length; i++) {
            res = Math.min(res, numbers[i]);
        }
        return res;
    }


    private Map<Integer, Integer> refPreMap = new HashMap<>();
    private Map<Integer, Integer> refInMap = new HashMap<>();

    //剑指 Offer 07. 重建二叉树
    //这个题的思路大概说一下：
    //首先科普：
    //所谓前序、中序、后序是指根节点在遍历中的位置：
    //前序：根左右
    //中序：左根右
    //后序：左右根
    //明确之后来看这个题的思路：给出前序和中序，要求还原一个树，那么：
    //首先，前序的第一个数字必定是根，找到根以后，寻在其在中序中，找到即可将这个根对应的左右子树分开。
    //这里是拿到了中序遍历中对应的左右子树，想要进行递归，还缺一个条件，即左右子树对应的根节点，
    //这个东西需要根据左右对应的节点数来看，这个节点树可以从上一波的中序数组中根节点的坐标来看。
    //这样拿到了左右子树的节点结合以及其根节点，下一步就可以直接进行递归了。
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < preorder.length; i++) {
            refInMap.put(inorder[i], i);
        }
        return buildTreeHelper(preorder, 0, preorder.length, 0);
    }

    public TreeNode buildTreeHelper(int[] preorder, int from, int to, int rootIdx) {
        if (from >= to) {
            return null;
        }
        int rootVal = preorder[rootIdx];
        int splitIdx = refInMap.get(rootVal);
        int leftLen = splitIdx - from;
        TreeNode resNode = new TreeNode(rootVal);
        resNode.left = buildTreeHelper(preorder, from, splitIdx, rootIdx + 1);
        resNode.right = buildTreeHelper(preorder, splitIdx + 1, to, rootIdx + leftLen + 1);
        return resNode;
    }

    //剑指 Offer 14- I. 剪绳子
    public int cuttingRope(int n) {
        if (n == 0) {
            return 0;
        }
        if (n <= 2) {
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], Math.max(i - j, dp[i - j]) * j);
            }
        }
        return dp[n];
    }

    //剑指 Offer 15. 二进制中1的个数
    public int hammingWeight(int n) {
        int res = 0;
        while (n != 0) {
            n &= n - 1;
            res++;
        }
        return res;
    }

    //剑指 Offer 16. 数值的整数次方
    //这个题主要就是考察快速幂，这里其实是中2分思想：
    //对于任意一个n^m，总是有两种情况：
    //m为奇数，结果f = n * (n ^ ((m - 1) / 2)) ^ 2
    //m为偶数，结果f = (n ^ ((m - 1) / 2))^ 2
    //这个逻辑可以不断递归拆分下去，这样做的好处在，
    //能以最快的速度逼近并求出结果。
    public double myPow(double x, int n) {
        if (x == 0) {
            return 0;
        }
        double res = 1;
        long N = n;
        if (N < 0) {
            N = -N;
            x = 1 / x;
        }
        double target = x;
        while (N > 0) {
            if ((N & 1) == 1) {
                res *= target;
            }
            target *= target;
            N = N >> 1;
        }
        return res;
    }

    public int[] printNumbers(int n) {
        int resSize = 9;
        while (--n > 0) {
            resSize = resSize * 10 + 9;
        }
        int[] res = new int[resSize];
        for (int i = 0; i < resSize; i++) {
            res[i] = i + 1;
        }
        return res;
    }

    public ListNode deleteNode(ListNode head, int val) {
        if (head != null && head.val == val) {
            return head.next;
        } else if (head == null) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        while (head.next != null && head.next.val != val) {
            head = head.next;
        }
        if (head.next != null) {
            head.next = head.next.next;
        }
        return dummy.next;
    }

    //剑指 Offer 21. 调整数组顺序使奇数位于偶数前面
    public int[] exchange(int[] nums) {
        int start = 0;
        for (int i = 0; i < nums.length; i++) {
            if ((nums[i] & 1) == 1) {
                int tmp = nums[i];
                nums[i] = nums[start];
                nums[start++] = tmp;
            }
        }
        return nums;
    }

    //剑指 Offer 25. 合并两个排序的链表
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
            cur.next = null;
        }
        cur.next = l1 == null ? l2 : l1;
        return dummy.next;
    }

    //剑指 Offer 31. 栈的压入、弹出序列
    //这个题的思路说下，首先来说，基本上还是要借助一个辅助栈
    //有个这个辅助栈后，做以下操作：
    //1.每次按照入栈顺序压入一个数
    //2.循环操作，若栈顶元素与出栈数组当前指定元素相同，则出栈，同时是将当前待出栈下标后移一位
    //  直到栈顶元素与出栈数组当前指定元素不同为止。
    //3.上述1，2循环执行直到最后一个入栈元素完成操作。
    //上述操作如果顺序合法，则最终的stack中的数应该全部成功出栈，stack应该为空。
    //因此，返回stack.isEmpty即可
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack<>();
        int pushIdx = 0;
        int popIdx = 0;
        while (pushIdx < pushed.length) {
            stack.push(pushed[pushIdx++]);
            while (!stack.isEmpty() && stack.peek() == popped[popIdx]) {
                stack.pop();
                popIdx++;
            }
        }
        return stack.isEmpty();
    }

    //剑指 Offer 32 - I. 从上到下打印二叉树
    public int[] levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<Integer> resList = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                continue;
            }
            resList.add(node.val);
            queue.offer(node.left);
            queue.offer(node.right);
        }
        int[] res = new int[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = resList.get(i);
        }
        return res;
    }

    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> tmp = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                if (node == null) {
                    continue;
                }
                queue.offer(node.left);
                queue.offer(node.right);
                tmp.add(node.val);
            }
            if (tmp.size() > 0) {
                res.add(tmp);
            }
        }
        return res;
    }

    public List<List<Integer>> levelOrder3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean reverse = false;
        while (!queue.isEmpty()) {
            LinkedList<Integer> tmp = new LinkedList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                if (node == null) {
                    continue;
                }
                queue.offer(node.left);
                queue.offer(node.right);
                if (reverse) {
                    tmp.addFirst(node.val);
                } else {
                    tmp.add(node.val);
                }
            }
            reverse = !reverse;
            if (tmp.size() > 0) {
                res.add(tmp);
            }
        }
        return res;
    }

    //剑指 Offer 33. 二叉搜索树的后序遍历序列
    public boolean verifyPostorder(int[] postorder) {
        return verifyPostorderHelper(postorder, 0, postorder.length - 1);
    }

    //剑指 Offer 33. 二叉搜索树的后序遍历序列
    public boolean verifyPostorderHelper(int[] postorder, int startIdx, int rootIdx) {
        if (rootIdx == 0) {
            return true;
        }
        if (rootIdx <= startIdx + 1) {
            return true;
        }
        int target = postorder[rootIdx];
        boolean isEdge = postorder[startIdx] > target;
        int leftRoot = startIdx;
        for (int i = startIdx + 1; i < rootIdx; i++) {
            if (!isEdge && postorder[i] > target) {
                isEdge = true;
                leftRoot = i - 1;
            }
            if (isEdge && postorder[i] < target) {
                return false;
            }
        }
        System.out.println("left search start = " + startIdx + " root = " + leftRoot);
        System.out.println("right search start = " + (startIdx == leftRoot ? leftRoot : leftRoot + 1) + " root = " + (rootIdx - 1));
        return verifyPostorderHelper(postorder, startIdx, leftRoot)
                && verifyPostorderHelper(postorder, startIdx == leftRoot ? leftRoot : leftRoot + 1, rootIdx - 1);
    }


    //剑指 Offer 38. 字符串的排列
    //这里是个典型的全排列问题，需要使用回溯，这里使用了一种较为巧妙的回溯，
    //从头到位使用一个list来进行中间变量存储，按照常规的回溯，每次回溯都需要从头到位遍历string,
    // 并把list中已有的char排除在外，但这种新型的方法采用的方案是维护下标，保证每次下标都指向最
    //最后一个已选中的元素，同时，每次选中时，将选中的元素与当前指针指向元素做交换，这样就可以始终
    //保持指针及之前的元素全部已被选中，而之后的未被选中，这样，只需要每次遍历只需要从指针之后看里
    //即可，注意，这个时候的剪枝就是交换元素了。
    //这里还有一个需要注意的点是，原字符串可能本身就存在重复字符，因此需要一个手段来排除重复排列，
    //具体手段就是，在针对统一位置的定位循环时，在循环体外初始化一个Set，存储当前位置已经选中过的
    //char元素，循环过程中若set无当前元素，则加入，否则跳过本次循环。
    List<String> res = new LinkedList<>();
    char[] c;

    public String[] permutation(String s) {
        c = s.toCharArray();
        dfs(0);
        return res.toArray(new String[res.size()]);
    }

    void dfs(int x) {
        if (x == c.length - 1) {
            res.add(String.valueOf(c)); // 添加排列方案
            return;
        }
        HashSet<Character> set = new HashSet<>();
        for (int i = x; i < c.length; i++) {
            if (set.contains(c[i])) continue; // 重复，因此剪枝
            set.add(c[i]);
            swap(i, x); // 交换，将 c[i] 固定在第 x 位
            dfs(x + 1); // 开启固定第 x + 1 位字符
            swap(i, x); // 恢复交换
        }
    }

    void swap(int a, int b) {
        char tmp = c[a];
        c[a] = c[b];
        c[b] = tmp;
    }

    //剑指 Offer 40. 最小的k个数
    public int[] getLeastNumbers(int[] arr, int k) {
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        int[] res = new int[k];
        for (int a : arr) {
            queue.offer(a);
            if (queue.size() >= k) {
                queue.poll();
            }
        }
        int i = 0;
        while (!queue.isEmpty()) {
            res[i++] = queue.poll();
        }
        return res;
    }

    //剑指 Offer 45. 把数组排成最小的数
    public String minNumber(int[] nums) {
        String[] test = new String[nums.length];
        int i = 0;
        for (int num : nums) {
            test[i] = String.valueOf(nums[i]);
            i++;
        }
        Arrays.sort(test, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (o1 + o2).compareTo(o2 + o1);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (String s :
                test) {
            sb.append(s);
        }
        return sb.toString();
    }

    //剑指 Offer 46. 把数字翻译成字符串
    public int translateNum(int num) {
        if (num < 10) {
            return 1;
        }
        int b = 1;
        int a = num % 100 < 26 && num % 100 >= 10 ? 2 : 1;
        num /= 10;
        while (num > 0) {
            int temp = a;
            if (num % 100 < 26 && num % 100 >= 10) {
                temp += b;
            }
            b = a;
            a = temp;
            num /= 10;
        }
        return a;
    }

    //剑指 Offer 50. 第一个只出现一次的字符
    public char firstUniqChar(String s) {
        HashMap<Character, Integer> map = new LinkedHashMap<>();
        for (char c :
                s.toCharArray()) {
            map.put(c, map.get(c) == null ? 1 : map.get(c) + 1);
        }
        for (char c :
                map.keySet()) {
            if (map.get(c) == 1) {
                return c;
            }
        }
        return ' ';
    }

    //剑指 Offer 50. 第一个只出现一次的字符
    //进阶玩法：这里还是使用LinkedHashMap，但聪明的地方在于，Map中存的不再是出现次数，而是boolean值
    //注意看下面这种写法，只有仅出现一次的元素对应value为true，未出现以及出现超过一次的均为false。
    public char firstUniqChar2(String s) {
        Map<Character, Boolean> dic = new LinkedHashMap<>();
        char[] sc = s.toCharArray();
        for (char c : sc)
            dic.put(c, !dic.containsKey(c));
        for (Map.Entry<Character, Boolean> d : dic.entrySet()) {
            if (d.getValue()) return d.getKey();
        }
        return ' ';
    }

    //剑指 Offer 66. 构建乘积数组
    public int[] constructArr(int[] a) {
        if (a == null || a.length == 0) {
            return new int[]{};
        }
        int[] helper = new int[a.length];
        helper[0] = 1;
        for (int i = 1; i < helper.length; i++) {
            helper[i] = helper[i - 1] * a[i - 1];
        }
        int endHelper = 1;
        for (int i = helper.length - 2; i >= 0; i--) {
            endHelper *= a[i + 1];
            helper[i] *= endHelper;
        }
        return helper;
    }

    //剑指 Offer 63. 股票的最大利润
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        int res = 0;
        int a = 0;
        int b = 0;
        for (int i = 1; i < prices.length; i++) {
            b = a + prices[i] - prices[i - 1];
            b = Math.max(b, 0);
            res = Math.max(res, b);
            a = b;
        }
        return res;
    }

    //剑指 Offer 56 - I. 数组中数字出现的次数
    public int[] singleNumbers(int[] nums) {
        int test = nums[0];
        for (int i = 1; i < nums.length; i++) {
            test ^= nums[i];
        }
        int mod = 1;
        while ((mod & test) == 0) {
            mod = mod << 1;
        }
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & mod) != 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[]{a, b};
    }

    //剑指 Offer 60. n个骰子的点数
    //动态规划，状态方程如下：
    //状态变量，dp[i][j] : i个骰子，投出k点的总组合数。
    //状态方程，dp[i][j] = sum(dp[i-1][k] k = 1..Math.min(j, 6)
    //具体思路其实是关注多出来的哪一个骰子到底投出的是几。
    //下一步优化，空间复杂度优化，可走二维->两行->一行的优化思路，
    //一行的做法就是从头到位使用一维数组，这样每次遍历的时候需要从右向左遍历。
    public double[] dicesProbability(int n) {
        int[][] dp = new int[n][6 * n + 1];
        for (int i = 1; i <= 6; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= 6 * (i + 1); j++) {
                int tmp = 0;
                for (int k = 1; k <= Math.min(j, 6); k++) {
                    tmp += dp[i - 1][j - k];
                }
                dp[i][j] = tmp;
            }
        }
        int sum = 0;
        for (int i = n; i <= 6 * n; i++) {
            sum += dp[n - 1][i];
        }
        double[] res = new double[5 * n + 1];
        for (int i = 0; i < 5 * n + 1; i++) {
            res[i] = (double) dp[n - 1][i + n] / (double) sum;
        }
        return res;
    }

    //剑指 Offer 48. 最长不含重复字符的子字符串
    //这个题作为标准的子字符串题，肯定是双指针/滑动窗口
    //这里的精髓在于，这里用的是双指针
    //一个p2, 一个i，i好理解，就是当前考察的字符索引
    //而这个p2有点门道，这里我们的策略是，
    //i一直向右遍历，只要不重复，res就可以一直++,
    //若重复，则考察上一次出现的索引位置old与当前索引位置i的差距是否形成新的最大值。若形成则更新
    //只要重复，那么解的考察起点就更新为p2与上一次重复的索引的最大值。
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




    //剑指 Offer 47. 礼物的最大价值
    public int maxValue(int[][] grid) {
        for (int i = 1; i < grid.length; i++) {
            grid[i][0] += grid[i - 1][0];
        }
        for (int i = 1; i < grid[0].length; i++) {
            grid[0][i] += grid[0][i - 1];
        }
        for (int i = 1; i < grid.length; i++) {
            for (int j = 1; j < grid[0].length; j++) {
                grid[i][j] = Math.max(grid[i - 1][j], grid[i][j - 1]) + grid[i][j];
            }
        }
        return grid[grid.length - 1][grid[0].length - 1];
    }

    //剑指 Offer 34. 二叉树中和为某一值的路径
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        pathSumHelper(root, res, new ArrayList<Integer>(), sum);
        return res;
    }

    private void pathSumHelper(TreeNode root, List<List<Integer>> res,
                               List<Integer> curPath, int sum) {
        if (root == null) {
            return;
        }
        curPath.add(root.val);
        if (root.left == null && root.right == null && sum == root.val) {
            res.add(new ArrayList<>(curPath));
        }
        pathSumHelper(root.left, res, curPath, sum - root.val);
        pathSumHelper(root.right, res, curPath, sum - root.val);
        curPath.remove(curPath.size() - 1);
    }

    class MaxQueue {
        Queue<Integer> q;
        Deque<Integer> d;

        public MaxQueue() {
            q = new LinkedList<Integer>();
            d = new LinkedList<Integer>();
        }

        public int max_value() {
            if (d.isEmpty()) {
                return -1;
            }
            return d.peekFirst();
        }

        public void push_back(int value) {
            while (!d.isEmpty() && d.peekLast() < value) {
                d.pollLast();
            }
            d.offerLast(value);
            q.offer(value);
        }

        public int pop_front() {
            if (q.isEmpty()) {
                return -1;
            }
            int ans = q.poll();
            if (ans == d.peekFirst()) {
                d.pollFirst();
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        AimToOffer offer = new AimToOffer();
        TreeNode root = new TreeNode(5);
        AimToOffer.MaxQueue queue = offer.new MaxQueue();
        queue.push_back(1);
        queue.push_back(1);
        queue.push_back(1);
        queue.push_back(1);
        queue.push_back(2);
        queue.push_back(4);
        queue.push_back(5);
        LinkedList<String> tet = null;
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


    //
    public class CQueue {

        Stack<Integer> stackA = new Stack<>();
        Stack<Integer> stackB = new Stack<>();

        public CQueue() {

        }

        public void appendTail(int value) {
            stackA.push(value);
        }

        public int deleteHead() {
            if (stackB.isEmpty()) {
                while (!stackA.isEmpty()) {
                    stackB.push(stackA.pop());
                }
            }
            return stackB.isEmpty() ? -1 : stackB.pop();
        }

    }

    public static void printListNodes(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
    }
}
