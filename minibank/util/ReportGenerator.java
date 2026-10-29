package minibank.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    private ReportGenerator() {
    }

    public static String generateReport(Path logsFolder, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        StringBuilder fileDetails = new StringBuilder();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder)) {
            for (Path logFile : stream) {
                try (BufferedReader reader = Files.newBufferedReader(logFile)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty()) {
                            continue;
                        }
                        String[] parts = line.split("\\s+");
                        if (parts.length != 3) {
                            continue;
                        }
                        String type = parts[0].toUpperCase();
                        long amount;
                        try {
                            amount = Long.parseLong(parts[2]);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        if (type.equals("DEPOSIT")) {
                            totalDeposits += amount;
                        } else if (type.equals("WITHDRAW")) {
                            totalWithdrawals += amount;
                        }
                    }
                }

                BasicFileAttributes attrs = Files.readAttributes(logFile, BasicFileAttributes.class);
                fileDetails.append("File: ").append(logFile.getFileName())
                        .append(", size: ").append(attrs.size())
                        .append(" bytes, last modified: ").append(attrs.lastModifiedTime())
                        .append("\n");
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("===== End-of-Day Report =====\n");
        report.append(fileDetails);
        report.append("Total Deposits    : ").append(totalDeposits).append("\n");
        report.append("Total Withdrawals : ").append(totalWithdrawals).append("\n");
        report.append("==============================\n");

        Files.write(reportFile, report.toString().getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return report.toString();
    }
}