package p2;

public class ChatFilter {

    public static String filterLogs(String[] logs, String keyword) {

        int count = 0;

        StringBuilder report = new StringBuilder();

        // Check every log line
        for (String line : logs) {

            // Split into time, user, and message
            String[] parts = line.split(" ", 3);

            // Skip malformed lines
            if (parts.length < 3) {
                continue;
            }

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            // Case-insensitive keyword check
            if (message.toLowerCase().contains(keyword.toLowerCase())) {

                count++;

                // Add matching line to report
                report.append(time)
                      .append(" ")
                      .append(user)
                      .append(": ")
                      .append(message)
                      .append("\n");
            }
        }

        // Return final result
        return "Matches: " + count + "\n" + report;
    }
}