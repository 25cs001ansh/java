import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class FileTreeReport {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public static void generateReport(Path folderToScan, Path outputFile) {
        StringBuilder report = new StringBuilder();
        report.append("========================================================================================\n");
        report.append("                        NIO FILE TREE AUDIT REPORT                                      \n");
        report.append(" Scanned Directory : ").append(folderToScan.toAbsolutePath()).append("\n");
        report.append(" Generated At      : ").append(FORMATTER.format(Instant.now())).append("\n");
        report.append("========================================================================================\n");
        report.append(String.format("%-35s %-15s %-22s %s%n", "File Name", "Size (bytes)", "Last Modified", "Relative Path"));
        report.append("----------------------------------------------------------------------------------------\n");

        AtomicInteger fileCount = new AtomicInteger(0);
        AtomicLong totalBytes = new AtomicLong(0);

        try (Stream<Path> stream = Files.walk(folderToScan)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    fileCount.incrementAndGet();
                    totalBytes.addAndGet(attrs.size());

                    String name = path.getFileName().toString();
                    if (name.length() > 33) {
                        name = name.substring(0, 30) + "...";
                    }

                    String sizeStr = String.valueOf(attrs.size());
                    String modTime = FORMATTER.format(attrs.lastModifiedTime().toInstant());
                    String relPath = folderToScan.relativize(path).toString();

                    report.append(String.format("%-35s %-15s %-22s %s%n", name, sizeStr, modTime, relPath));
                } catch (IOException e) {
                    report.append("Could not read attributes for: ").append(path).append("\n");
                }
            });

            report.append("----------------------------------------------------------------------------------------\n");
            report.append("SUMMARY:\n");
            report.append("  Total Files Scanned : ").append(fileCount.get()).append("\n");
            report.append("  Total Size          : ").append(totalBytes.get()).append(" bytes (")
                  .append(String.format("%.2f KB", totalBytes.get() / 1024.0)).append(")\n");
            report.append("========================================================================================\n");

            // Write report to file
            Files.writeString(outputFile, report.toString());

            // Print report to console
            System.out.println(report);
            System.out.println("Report successfully written to: " + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error walking directory: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Path scanDir = Paths.get(".");
        Path reportOut = Paths.get("Practical 11", "file_tree_report.txt");

        System.out.println("Generating file tree report using NIO...");
        generateReport(scanDir, reportOut);
    }
}

