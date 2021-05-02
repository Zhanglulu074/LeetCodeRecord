import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.BitSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

class TestTest {
    private int a = 1;
    private int getA() {
        return a;
    }
}

public class CodeTest {
    public static void main(String[] args) {
        Class<TestTest> stringClass = TestTest.class;
        TestTest s = new TestTest();
        try {
            Method[] methods = stringClass.getDeclaredMethods();
            Method method = stringClass.getDeclaredMethod("getA");
//            method.setAccessible(false);
            int c =  (Integer) method.invoke(s);
            System.out.println(c);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    synchronized void performTest(int tag) {
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
    }
}

