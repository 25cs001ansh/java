package p2;

import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        String[] logs = {
            "10:05 alice Hello there",
            "10:10 bob How are you?",
            "malformed",
            "10:20 charlie Welcome everyone"
        };

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        // Call ChatFilter method
        String result = ChatFilter.filterLogs(logs, keyword);

        System.out.println(result);

        sc.close();
    }
}