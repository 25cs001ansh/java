import java.util.*;

public class Leaderboard {

    // Returns the top-N entries sorted by score (descending), ties broken by name (ascending)
    public static List<Map.Entry<String, Integer>> getTopN(Map<String, Integer> scores, int n) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());

        Comparator<Map.Entry<String, Integer>> comparator =
                Comparator.<Map.Entry<String, Integer>, Integer>comparing(
                        Map.Entry::getValue, Comparator.reverseOrder()
                ).thenComparing(Map.Entry::getKey); // Alphabetical tie-breaker

        list.sort(comparator);

        return list.subList(0, Math.min(n, list.size()));
    }

    public static void main(String[] args) {
        System.out.println("=== Game Leaderboard System ===");

        // Player scores stored in a Map
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Riya", 850);
        scores.put("Ansh", 950);
        scores.put("Alice", 950);   // Tie with Ansh (Alice should rank ahead alphabetically)
        scores.put("Bob", 720);
        scores.put("Charlie", 850); // Tie with Riya (Charlie should rank ahead alphabetically)
        scores.put("David", 600);
        scores.put("Eve", 910);
        scores.put("Frank", 850);   // Another tie with 850

        System.out.println("Total players recorded: " + scores.size() + "\n");

        int topN = 5;
        System.out.println("Top " + topN + " Leaderboard (Highest score first, ties broken by name):");
        System.out.println("----------------------------------------------");
        System.out.printf("%-6s %-20s %-10s%n", "Rank", "Player Name", "Score");
        System.out.println("----------------------------------------------");

        List<Map.Entry<String, Integer>> topPlayers = getTopN(scores, topN);

        int rank = 1;
        for (Map.Entry<String, Integer> entry : topPlayers) {
            System.out.printf("#%-5d %-20s %-10d%n", rank++, entry.getKey(), entry.getValue());
        }
        System.out.println("----------------------------------------------");
    }
}

