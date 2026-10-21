import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        String text = "the quick brown fox jumps over the lazy dog the fox barks and the dog runs";
        Map<String, Integer> freq = new HashMap<>();
        for (String w : text.toLowerCase().split("\\W+")) {
            if (!w.isEmpty()) {
                freq.merge(w, 1, Integer::sum);
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(freq.entrySet());
        list.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue).reversed()
                .thenComparing(Map.Entry::getKey)); 

        for (Map.Entry<String, Integer> e : list) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}