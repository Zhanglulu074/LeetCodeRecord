import java.security.AccessController;
import java.util.BitSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class CodeTest {
    public static void main(String[] args) {
//        CodeTest test = new CodeTest();
//        Object lock = new Object();
//        Thread t1 = new Thread(() -> {
//            System.out.println("线程1准备进入");
//            test.performTest(1);
//        });
//        Thread t2 = new Thread(() -> {
//            System.out.println("线程2准备进入");
//            test.performTest(2);
//        });
//        t1.start();
//        t2.start();
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        int[] nums = {0, 1, 2, 3, 0, 1, 2, 3, 7};
//        int ret = 0;
//        for (int i = 0; i < nums.length; i++) {
//            ret ^= nums[i];
//        }
//        System.out.println(ret);
        String s = "abc";
        System.out.println("0" + s);
        for (char c : s.toCharArray()) {
            System.out.println(c);
        }
    }

    synchronized void performTest(int tag) {
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
    }
}

