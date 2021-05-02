import java.util.*;

public class DynamicPrograming {

    //509. 斐波那契数(1.递归)
    //这种方式的问题在于会发生很多重复性的计算，时间复杂度较高
    public int fib(int n) {
        if (n == 1) {
            return 1;
        } else if (n == 0) {
            return 0;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }

    //509. 斐波那契数(1.DP)
    //这种方式的问题在于会发生很多重复性的计算，时间复杂度较高
    public int fibDP(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int[] DP = new int[n + 1];
        DP[0] = 0;
        DP[1] = 1;
        for (int i = 2; i <= n; i++) {
            DP[i] = DP[i - 1] + DP[i - 2];
        }
        return DP[n];
    }

    //509. 斐波那契数(2.DP+空间优化)
    public int fib4(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int sum = a + b;
            a = b;
            b = sum;
        }
        return b;
    }

    //139. 单词拆分(1.递归回溯)
    public boolean wordBreak(String s, List<String> wordDict) {
        return wordBreakHelper(s, new HashSet<>(wordDict), 0);
    }

    public boolean wordBreakHelper(String s, HashSet<String> set, int index) {
        if (index == s.length()) {
            return true;
        }
        for (int i = index + 1; i <= s.length(); i++) {
            String test = s.substring(index, i);
            if (set.contains(s.substring(index, i)) && wordBreakHelper(s, set, i)) {
                return true;
            }
        }
        return false;
    }

    //139. 单词拆分(2.递归回溯+记忆化搜索)
    //这个地方加了个Boolean数组(长度与字符串长度相同)
    //用以记录从i到字符串末位的子串是否可以有字典中的词拼接而成
    //注意这里之所以用Boolean是为了多一个为初始化的状态，即
    //null, 还未查到，需要重写遍历判断
    //true, 已查过，可以成功拼接
    //false, 已查过，不可以成功拼接
    public boolean wordBreak2(String s, List<String> wordDict) {
        return wordBreakHelper2(s, new HashSet<>(wordDict), 0, new Boolean[s.length() + 1]);
    }

    public boolean wordBreakHelper2(String s, HashSet<String> set, int index, Boolean[] memo) {
        if (index == s.length()) {
            return true;
        }
        for (int i = index + 1; i <= s.length(); i++) {
            if (set.contains(s.substring(index, i))) {
                if (memo[i] == null) {
                    memo[i] = wordBreakHelper2(s, set, i, memo);
                }
                if (memo[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    //139. 单词拆分(3.纯DP)
    //dp[i]: 从i到结尾的字符串是否可以与字典匹配
    //dp[i] = (set.contains(s.substring(i, j)) && dp[j] when j = i + 1..end)
    public boolean wordBreak3(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[s.length() - 1] = set.contains(s.substring(s.length() - 1));
        dp[s.length()] = true;
        for (int i = s.length() - 2; i >= 0; i--) {
            dp[i] = false;
            for (int j = i + 1; j <= s.length(); j++) {
                if (set.contains(s.substring(i, j)) && dp[j]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[0];
    }

    public boolean wordBreak4(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    //300. 最长递增子序列
    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                }
            }
        }
        int res = 0;
        for (int value : dp) {
            res = Math.max(res, value);
        }
        return res;
    }

    //300. 最长递增子序列(贪心算法)
    //配合二分才有优势
    public int lengthOfLIS2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        List<Integer> tmp = new ArrayList<>();
        tmp.add(nums[0]);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > tmp.get(tmp.size() - 1)) {
                tmp.add(nums[i]);
            } else {
                int j = tmp.size() - 1;
                while (j >= 0 && tmp.get(j) >= nums[i]) {
                    j--;
                }
                if (j != tmp.size() - 1) {
                    tmp.set(j + 1, nums[i]);
                }

            }
        }
        return tmp.size();
    }

    //0-1背包
    public int baseBackpack(int[] w, int[] v, int N, int C) {
        int[][] dp = new int[N][C + 1];
        for (int i = 0; i <= C; i++) {
            dp[0][i] = i >= w[0] ? v[0] : 0;
        }
        for (int i = 1; i < N; i++) {
            for (int j = 0; j <= C; j++) {
                if (j < w[i]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
                }
            }
        }
        return dp[N - 1][C];
    }

    //0-1背包(空间复杂度优化)
    public int baseBackpackOp(int[] w, int[] v, int N, int C) {
        int[][] dp = new int[2][C + 1];
        for (int i = 0; i <= C; i++) {
            dp[0][i] = i >= w[0] ? v[0] : 0;
        }
        for (int i = 1; i < N; i++) {
            for (int j = 0; j <= C; j++) {
                int cur = i % 2;
                int past = cur ^ 1;
                if (j < w[i]) {
                    dp[cur][j] = dp[past][j];
                } else {
                    dp[cur][j] = Math.max(dp[past][j], dp[past][j - w[i]] + v[i]);
                }
            }
        }
        return dp[(N - 1) % 2][C];
    }

    //0-1背包(空间复杂度二次优化)
    public int baseBackpackOpMore(int[] w, int[] v, int N, int C) {
        int[] dp = new int[C + 1];
        for (int i = 0; i <= C; i++) {
            dp[i] = i >= w[0] ? v[0] : 0;
        }
        for (int i = 1; i < N; i++) {
            for (int j = C; j >= 0; j--) {
                if (j >= w[i]) {
                    dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
                }
                if (j == C && i == N - 1) {
                    break;
                }
            }
        }
        return dp[C];
    }

    //0-1 背包问题，去掉初始化步骤
    public int baseBackpackOpMoreWithoutInit(int[] w, int[] v, int N, int C) {
        int[] dp = new int[C + 1];
//        for (int i = 0; i <= C; i++) {
//            dp[i] = i >= w[0] ? v[0] : 0;
//        }
        for (int i = 0; i < N; i++) {
            for (int j = C; j >= w[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
                if (j == C && i == N - 1) {
                    break;
                }
            }
        }
        return dp[C];
    }


    //完全 背包问题，去掉初始化步骤
    public int completeBackpackOpMore(int[] w, int[] v, int N, int C) {
        int[] dp = new int[C + 1];
        for (int i = 0; i < N; i++) {
            for (int j = w[i]; j <= C; j++) {
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
            }
        }
        return dp[C];
    }

    public int multipleBackpack(int[] w, int[] v, int[] c, int N, int C) {
        int[] dp = new int[C + 1];
        for (int i = 0; i < N; i++) {
            for (int j = C; j >= w[i]; j--) {
                for (int k = 1; k <= c[i] && k * w[i] <= j; k++) {
                    dp[j] = Math.max(dp[j], dp[j - w[i]] + k * v[i]);
                }
            }
        }
        return dp[C];
    }


    //LC5. 最长回文子串
    //这题的特殊之处在于判断dp数组的内容不是直接的长度，而是：
    //dp[i][j] 从i到j的字符串是否为会问
    public String longestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        boolean[][] dp = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = true;
        }
        int resLength = 1;
        int resStart = 0;
        int resEnd = 0;
        for (int i = 1; i < s.length(); i++) {
            for (int j = 0; j + i < s.length(); j++) {
                int k = j + i;
                if ((k == j + 1 || dp[j + 1][k - 1]) && s.charAt(j) == s.charAt(k)) {
                    dp[j][k] = true;
                    if (k - j + 1 >= resLength) {
                        resStart = j;
                        resEnd = k;
                        resLength = k - j + 1;
                    }
                }
            }
        }
        return s.substring(resStart, resEnd + 1);
    }

    //LC5. 最长回文子串(优化遍历方案)
    public String longestPalindrome1(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        boolean[][] dp = new boolean[s.length()][s.length()];
        int resLength = 0;
        int resStart = 0;
        int resEnd = 0;
        for (int j = 0; j < s.length(); j++) {
            for (int i = 0; i <= j; i++) {
                if (s.charAt(i) == s.charAt(j) && ((j - i < 2) || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    if (j - i + 1 > resLength) {
                        resLength = j - i + 1;
                        resStart = i;
                        resEnd = j;
                    }
                }
            }
        }
        return s.substring(resStart, resEnd + 1);
    }

    //62. 不同路径
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    //62. 不同路径(一维化处理)
    public int uniquePaths1(int m, int n) {
        int[] dp = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) {
                    dp[j] = 1;
                } else {
                    dp[j] += dp[j - 1];
                }
            }
        }
        return dp[n - 1];
    }

    //追加：01背包问题
    public List<Integer> baseBackpackWithRes(int[] w, int[] v, int N, int C) {
        int[][] dp = new int[N][C + 1];
        String[][] dpHelper = new String[N][C + 1];
        for (int i = 0; i <= C; i++) {
            dp[0][i] = i >= w[0] ? v[0] : 0;
            dpHelper[0][i] = i > w[0] ? "0" : "";
        }
        for (int i = 1; i < N; i++) {
            for (int j = 0; j <= C; j++) {
                dpHelper[i][j] = "";
                if (j < w[i]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
                    if (dp[i - 1][j] > dp[i - 1][j - w[i]] + v[i]) {
                        dp[i][j] = dp[i - 1][j];
                        dpHelper[i][j] = dpHelper[i - 1][j];
                    } else {
                        dp[i][j] = dp[i - 1][j - w[i]] + v[i];
                        dpHelper[i][j] += dpHelper[i - 1][j - w[i]] + i;
                    }
                }
            }
        }
        return null;
    }

    //53. 最大子序和
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = nums[0];
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            res = Math.max(dp[i], res);
        }
        return res;
    }

    //152.
    public int maxProduct(int[] nums) {
        int[] dpMax = new int[nums.length];
        int[] dpMin = new int[nums.length];
        int res = nums[0];
        for (int i = 0; i < nums.length; i++) {
            dpMax[0] = nums[0];
            dpMin[0] = nums[0];
        }
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] >= 0) {
                dpMax[i] = Math.max(dpMax[i - 1] * nums[i], nums[i]);
                dpMin[i] = Math.min(dpMin[i - 1] * nums[i], nums[i]);
            } else {
                dpMax[i] = Math.max(dpMin[i - 1] * nums[i], nums[i]);
                dpMin[i] = Math.min(dpMax[i - 1] * nums[i], nums[i]);
            }
            res = Math.max(res, dpMax[i]);
        }
        return res;
    }

    //120. 三角形最小路径和（我的方法）
    //思路比较简单：
    //1. 状态变量为dp[i]停在第x行，第i个点是所需的最小路径和（这里已经经过二维到一维的优化）
    //   这个第x行将通过第一轮遍历实现。
    //2. 状态方程：dp[i] = Math.max(dp[i - 1], dp[i]) + triangel[x][i]
    //   这里有每一行的头部(只能去上一行的0)、尾部(只能去上一行的尾)两种情况需要区分讨论。
    //如此即可完成。
    public int minimumTotal(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        List<Integer> lastFloor = triangle.get(triangle.size() - 1);
        int[] dp = new int[lastFloor.size()];
        for (int i = 0; i < triangle.size(); i++) {
            List<Integer> curFloor = triangle.get(i);
            for (int j = curFloor.size() - 1; j >= 0; j--) {
                if (j == 0) {
                    dp[j] = dp[j] + curFloor.get(j);
                } else if (j < curFloor.size() - 1) {
                    dp[j] = Math.min(dp[j - 1] + curFloor.get(j), dp[j] + curFloor.get(j));
                } else {
                    dp[j] = dp[j - 1] + curFloor.get(j);
                }
                if (i == triangle.size() - 1) {
                    res = Math.min(res, dp[j]);
                }
            }
        }
        return res;
    }

    //120. 三角形最小路径和（官方解）
    //官方解的方案和我的方案完全一致，但是细节上有所优化，总的执行时间少了1ms左右。
    public int minimumTotal1(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        int[] dp = new int[triangle.size()];
        dp[0] = triangle.get(0).get(0);
        for (int i = 1; i < triangle.size(); i++) {
            dp[i] = dp[i - 1] + triangle.get(i).get(i);
            for (int j = i - 2; j > 0; j--) {
                dp[j] = Math.min(dp[j - 1], dp[j]) + triangle.get(i).get(j);
            }
            dp[0] = dp[0] + triangle.get(i).get(0);

        }
        for (int value : dp) {
            res = Math.min(value, res);
        }
        return res;
    }

    //322. 零钱兑换
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }

        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (i - coins[j] >= 0) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] == amount + 1? -1 :dp[amount];
    }

    //LC.377. 组合总和 Ⅳ
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 1; i < target + 1; i++) {
            int res = 0;
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] <= i) {
                    res += dp[i - nums[j]];
                }
            }
            dp[i] = res;
        }
        return dp[target];
    }


    public static void main(String[] args) {
        DynamicPrograming dp = new DynamicPrograming();
//        List<List<Integer>> test = new ArrayList<List<Integer>>(){{
//            add(Collections.singletonList(2));
//            add(Arrays.asList(3, 4));
//            add(Arrays.asList(6, 5, 7));
//            add(Arrays.asList(4, 1, 8, 3));
//        }};
        int[] test = {1, 2, 3};
        int res = dp.combinationSum4(test, 4);
        System.out.println(res);
    }
}
