import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class LogAnalyzer {
    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("logs");
        Files.createDirectories(dir);
        Files.write(dir.resolve("a.txt"),
                List.of("INFO start", "ERROR disk full", "INFO ok"),
                StandardCharsets.UTF_8);
        Files.write(dir.resolve("b.txt"),
                List.of("ERROR timeout", "WARN slow", "ERROR crash", "INFO done"),
                StandardCharsets.UTF_8);
        Files.write(dir.resolve("c.txt"),
                List.of("INFO hello", "INFO bye"),
                StandardCharsets.UTF_8);

        String keyword = "ERROR";
        int totalLines = 0;
        int matches = 0;

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path p : ds) {
                int fileLines = 0;
                int fileMatches = 0;
                try (BufferedReader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileLines++;
                        if (line.contains(keyword)) {
                            fileMatches++;
                        }
                    }
                }

                BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
                System.out.printf("%-6s lines=%d  %s=%d  size=%d bytes%n",
                        p.getFileName(), fileLines, keyword, fileMatches, attr.size());
                totalLines += fileLines;
                matches += fileMatches;
            }
        }
        System.out.println("Total lines  : " + totalLines);
        System.out.println("Lines with '" + keyword + "': " + matches);
    }
}