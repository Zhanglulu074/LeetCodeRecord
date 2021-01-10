import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public static void main(String[] args) {
        DynamicPrograming dp = new DynamicPrograming();
        List<String> test = new ArrayList<String>(){{
            add("aaaa");
            add("aaa");
        }};
        boolean res = dp.wordBreak3("aaaaaaa", test);
        System.out.println(res);
    }
}
