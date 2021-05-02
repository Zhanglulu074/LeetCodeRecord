
public class Test {
    public static void main(String[] args) {
        AbsTestList<Integer> list = new AbsTestList<Integer>();
//        list.showKey();
    }
}



class AbsTestList<T extends Number> {
    public AbsTestList() {

    }

    public void showKey(T key) {
        System.out.println(key.toString());
    }
}