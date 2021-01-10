import java.util.*;

public class BasicGraphic {

    private Map<Character, LinkedList<Character>> graphMap = new HashMap<>();

    public void init() {
        LinkedList<Character> listS = new LinkedList<Character>() {{ add('r');add('w'); }};
        LinkedList<Character> listR = new LinkedList<Character>() {{ add('s');add('v'); }};
        LinkedList<Character> listV = new LinkedList<Character>() {{ add('r');}};
        LinkedList<Character> listW = new LinkedList<Character>() {{ add('s');add('x');add('t');}};
        LinkedList<Character> listX = new LinkedList<Character>() {{ add('t');add('w');add('u');add('y'); }};
        LinkedList<Character> listT = new LinkedList<Character>() {{ add('x');add('w');add('u'); }};
        LinkedList<Character> listY = new LinkedList<Character>() {{ add('x');add('u'); }};
        LinkedList<Character> listU = new LinkedList<Character>() {{ add('t');add('x'); add('y');}};
        graphMap.put('s', listS);
        graphMap.put('r', listR);
        graphMap.put('v', listV);
        graphMap.put('w', listW);
        graphMap.put('x', listX);
        graphMap.put('t', listT);
        graphMap.put('y', listY);
        graphMap.put('u', listU);
    }

    //深度优先搜索
    public void BFS(char start) {
        Queue<Character> queue = new LinkedList<>();
        queue.add(start);
        Map<Character, Integer> map = new HashMap<>();
        map.put('s', Integer.MAX_VALUE);
        map.put('r', Integer.MAX_VALUE);
        map.put('v', Integer.MAX_VALUE);
        map.put('w', Integer.MAX_VALUE);
        map.put('x', Integer.MAX_VALUE);
        map.put('t', Integer.MAX_VALUE);
        map.put('y', Integer.MAX_VALUE);
        map.put('u', Integer.MAX_VALUE);
        map.put(start, 0);
        Set<Character> visited = new HashSet<>();
        visited.add(start);
        while (!queue.isEmpty()) {
            char c = queue.poll();
            for (char x : graphMap.get(c)) {
                if (!visited.contains(x)) {
                    int oldDis = map.get(x);
                    map.put(x, Math.min(oldDis, map.get(c) + 1));
                    visited.add(x);
                    queue.offer(x);
                }
            }
        }
        int x = 1;
    }

    public void DFS(char start) {
        Set<Character> visited = new HashSet<>();
        visited.add(start);
        DFSHelper(start, visited);
    }

    public void DFSHelper(char start, Set<Character> visited) {
        for (char x : graphMap.get(start)) {
            if (!visited.contains(x)) {
                visited.add(x);
                System.out.println(x);
                DFSHelper(x, visited);
            }
        }
    }

    public static void main(String[] args) {
        BasicGraphic graphic = new BasicGraphic();
        graphic.init();
        graphic.DFS('s');
        int x = 1;

    }

}
