public class ResourceDemo {

    // Custom AutoCloseable resource
    static class DatabaseConnection implements AutoCloseable {
        private final String connectionId;

        public DatabaseConnection(String connectionId) {
            this.connectionId = connectionId;
            System.out.println("[Resource] Connected to database: " + connectionId);
        }

        public void executeQuery(String sql) {
            System.out.println("[Resource] Executing query: " + sql);
            if (sql.contains("INVALID")) {
                throw new RuntimeException("Query Execution Failed: Syntax error near 'INVALID'");
            }
            System.out.println("[Resource] Query executed successfully.");
        }

        @Override
        public void close() {
            System.out.println("[Resource] AutoCloseable: DatabaseConnection " + connectionId + " is now CLOSED.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test Case 1: Normal Execution without Exception ===");
        try (DatabaseConnection conn = new DatabaseConnection("DB_PROD_1")) {
            conn.executeQuery("SELECT * FROM accounts;");
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        System.out.println("\n=== Test Case 2: Exception thrown inside Try-with-Resources ===");
        try (DatabaseConnection conn = new DatabaseConnection("DB_PROD_2")) {
            conn.executeQuery("SELECT * FROM INVALID_SYNTAX_TABLE;");
        } catch (Exception e) {
            // Proves that close() was executed BEFORE this catch block,
            // and the original exception is the one caught and reported!
            System.out.println("\n[Catch Block] Caught Reported Exception: " + e.getMessage());
        }

        System.out.println("\n=== Demonstration Completed Successfully ===");
    }
}

