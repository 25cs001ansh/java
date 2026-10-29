import java.util.List;
import java.util.Scanner;

/**
 * Presentation / UI Layer: Interactive Console Menu for Practical 14.
 * Interacts with ItemService to perform operations.
 */
public class ItemApp {

    private static final ItemService service = new ItemService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     Practical 14: Layered Java Application       ");
        System.out.println("           (Model - Service - Menu Main)          ");
        System.out.println("==================================================");

        // Preload some initial items for demo convenience
        loadSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice (1-7): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleAddItem();
                case "2" -> handleViewAllItems();
                case "3" -> handleFindItem();
                case "4" -> handleRemoveItem();
                case "5" -> handleUpdatePrice();
                case "6" -> handleInventoryStats();
                case "7" -> {
                    System.out.println("\nExiting Practical 14 App. Goodbye!");
                    running = false;
                }
                default -> System.out.println("[!] Invalid option. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Add New Item");
        System.out.println("2. View All Items");
        System.out.println("3. Find Item by ID");
        System.out.println("4. Remove Item");
        System.out.println("5. Update Item Price");
        System.out.println("6. View Inventory Statistics (Count & Value)");
        System.out.println("7. Exit");
        System.out.println("---------------------------------------------");
    }

    private static void handleAddItem() {
        System.out.println("\n--- Add New Item ---");
        try {
            System.out.print("Enter Item ID (e.g. ITM101): ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter Item Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Price (INR): ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            Item newItem = new Item(id, name, price, qty);
            service.addItem(newItem);
            System.out.println("[OK] Item successfully added! Current count: " + service.getCount());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric format for price or quantity.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void handleViewAllItems() {
        System.out.println("\n--- All Items in Inventory ---");
        List<Item> items = service.getAllItems();
        if (items.isEmpty()) {
            System.out.println("(No items found. Inventory is empty.)");
            return;
        }

        System.out.printf("%-10s %-22s %-14s %-8s%n", "ID", "Name", "Price", "Qty");
        System.out.println("---------------------------------------------------------");
        for (Item item : items) {
            System.out.printf("%-10s %-22s Rs.%-10.2f %-8d%n",
                    item.getId(), item.getName(), item.getPrice(), item.getQuantity());
        }
        System.out.println("Total items: " + service.getCount());
    }

    private static void handleFindItem() {
        System.out.println("\n--- Find Item by ID ---");
        System.out.print("Enter Item ID to search: ");
        String id = scanner.nextLine().trim();

        try {
            Item item = service.findById(id);
            System.out.println("[OK] Found: " + item);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void handleRemoveItem() {
        System.out.println("\n--- Remove Item ---");
        System.out.print("Enter Item ID to remove: ");
        String id = scanner.nextLine().trim();

        try {
            service.removeItem(id);
            System.out.println("[OK] Item '" + id + "' successfully removed. Remaining count: " + service.getCount());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void handleUpdatePrice() {
        System.out.println("\n--- Update Item Price ---");
        try {
            System.out.print("Enter Item ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter New Price: ");
            double newPrice = Double.parseDouble(scanner.nextLine().trim());

            service.updatePrice(id, newPrice);
            System.out.println("[OK] Price updated successfully! Updated item: " + service.findById(id));
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric format for price.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void handleInventoryStats() {
        System.out.println("\n--- Inventory Statistics ---");
        System.out.println("Total Item Count       : " + service.getCount());
        System.out.printf("Total Inventory Value  : Rs. %.2f%n", service.calculateTotalValue());
    }

    private static void loadSampleData() {
        try {
            service.addItem(new Item("ITM001", "Ballpoint Pen", 15.00, 50));
            service.addItem(new Item("ITM002", "Spiral Notebook", 65.50, 20));
            service.addItem(new Item("ITM003", "Scientific Calculator", 850.00, 5));
        } catch (Exception ignored) {
        }
    }
}
