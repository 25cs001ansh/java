// Custom checked exception for insufficient stock
class OutOfStockException extends Exception {

    private int shortfall;

    public OutOfStockException(int shortfall) {

        super("Not enough stock. Shortfall: " + shortfall);

        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}


// Custom checked exception for invalid quantity
class InvalidQuantityException extends Exception {

    public InvalidQuantityException() {

        super("Quantity must be greater than 0.");
    }
}


// Warehouse class
class Warehouse {

    private String[] items = {
        "Pen",
        "Book",
        "Laptop",
        "Mouse"
    };

    private int[] stock = {
        10,
        5,
        2,
        8
    };


    public void issue(String item, int qty)
            throws OutOfStockException,
                   InvalidQuantityException {

        // Check invalid quantity
        if (qty <= 0) {
            throw new InvalidQuantityException();
        }


        // Search item
        for (int i = 0; i < items.length; i++) {

            if (items[i].equalsIgnoreCase(item)) {

                // Check available stock
                if (qty > stock[i]) {

                    int shortfall = qty - stock[i];

                    throw new OutOfStockException(shortfall);
                }


                // Issue item
                stock[i] = stock[i] - qty;

                System.out.println(
                    "Issued " + qty + " " + item
                );

                return;
            }
        }

        System.out.println("Item not found: " + item);
    }


    public void displayStock() {

        System.out.println("\nRemaining Stock:");

        for (int i = 0; i < items.length; i++) {

            System.out.println(
                items[i] + " = " + stock[i]
            );
        }
    }
}


// Main class
public class StockIssue {

    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse();


        String[] requestItems = {
            "Pen",
            "Book",
            "Laptop",
            "Mouse",
            "Pen"
        };


        int[] requestQty = {
            3,
            10,
            0,
            5,
            20
        };


        int successCount = 0;
        int failedCount = 0;


        // Process all requests
        for (int i = 0; i < requestItems.length; i++) {

            System.out.println(
                "\nRequest: "
                + requestItems[i]
                + " - Quantity: "
                + requestQty[i]
            );

            try {

                warehouse.issue(
                    requestItems[i],
                    requestQty[i]
                );

                successCount++;
            }

            catch (InvalidQuantityException e) {

                System.out.println(
                    "Failed: " + e.getMessage()
                );

                failedCount++;
            }

            catch (OutOfStockException e) {

                System.out.println(
                    "Failed: " + e.getMessage()
                );

                System.out.println(
                    "Shortfall: "
                    + e.getShortfall()
                );

                failedCount++;
            }
        }


        System.out.println("\n----------------------");

        System.out.println(
            "Successful requests: " + successCount
        );

        System.out.println(
            "Failed requests: " + failedCount
        );


        warehouse.displayStock();
    }
}