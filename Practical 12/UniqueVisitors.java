import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> logins = List.of("raj", "amit", "raj", "sara", "amit", "john", "sara");

        Set<String> unique = new HashSet<>(logins);
        Set<String> ordered = new LinkedHashSet<>(logins);

        System.out.println("Unique users: " + unique.size());
        System.out.print("First three distinct: ");
        int n = 0;
        for (String u : ordered) {
            if (n++ == 3) {
                break;
            }
            System.out.print(u + " ");
        }
        System.out.println();
    }
}