import java.util.*;

public class Greedy {
    public static void main(String[] args) {
        HashMap<String, Set<String>> map = initial();
        Set<String> result = GetgreedyKey(map);
        System.out.println(Arrays.toString(result.stream().toArray()));
    }

    private static Set<String> GetgreedyKey(HashMap<String, Set<String>> map) {
        Set<String> all = map.get("all");
        map.remove("all");
        Set<String> collect = new HashSet<>();
        while (all.size() > 0) {
            int size = 0;
            String result = null;
            Set<String> set = map.keySet();
            for (String key : set) {
                Set<String> hashSet = new HashSet<>();
                hashSet.addAll(map.get(key));
                hashSet.retainAll(all);
                int keySize = hashSet.size();
                if (keySize > size) {
                    size = keySize;
                    result = key;
                }
            }
            all.removeAll(map.get(result));
            collect.add(result);
        }
        return collect;
    }

    public static HashMap<String, Set<String>> initial() {
        Set<String> K1 = new HashSet<>();
        Set<String> K2 = new HashSet<>();
        Set<String> K3 = new HashSet<>();
        Set<String> K4 = new HashSet<>();
        Set<String> K5 = new HashSet<>();
        K1.add("a");
        K1.add("c");
        K1.add("d");
        K1.add("b");

        K2.add("b");
        K2.add("c");
        K2.add("d");
        K2.add("e");

        K3.add("e");
        K3.add("d");

        K4.add("g");
        K4.add("h");

        K5.add("e");
        K5.add("h");
        K5.add("b");

        Set<String> all = new HashSet<>();
        all.addAll(K1);
        all.addAll(K2);
        all.addAll(K3);
        all.addAll(K4);
        all.addAll(K5);

        HashMap<String, Set<String>> map = new HashMap<>();
        map.put("K1", K1);
        map.put("K2", K2);
        map.put("K3", K3);
        map.put("K4", K4);
        map.put("K5", K5);
        map.put("all", all);
        return map;
    }
}
