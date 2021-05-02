import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Class test = TestInterface.class;
        TestInterface test1 =  (TestInterface) Proxy.newProxyInstance(test.getClassLoader(), new Class[]{test}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("invoke test(_)");
                return null;
            }
        });
        test1.test();
    }
}

interface TestInterface {
    public void test();
}
