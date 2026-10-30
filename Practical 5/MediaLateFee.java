// Abstract base class
abstract class Media {
    private String title;
    private int daysLate;

    public Media(String title, int daysLate) {
        this.title = title;
        this.daysLate = daysLate;
    }

    public String getTitle() {
        return title;
    }

    public int getDaysLate() {
        return daysLate;
    }

    // Abstract method to be overridden by each media type
    public abstract double computeLateFee();
}

// Subclass 1: Book (e.g., Rs. 2.0 per day late)
class Book extends Media {
    public Book(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    public double computeLateFee() {
        return getDaysLate() * 2.0;
    }
}

// Subclass 2: DVD (e.g., Rs. 10.0 per day late)
class DVD extends Media {
    public DVD(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    public double computeLateFee() {
        return getDaysLate() * 10.0;
    }
}

// Subclass 3: Magazine (e.g., Rs. 1.0 per day late)
class Magazine extends Media {
    public Magazine(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    public double computeLateFee() {
        return getDaysLate() * 1.0;
    }
}

// Main class to test the hierarchy and total batch late fee
public class MediaLateFee {

    public static void main(String[] args) {
        System.out.println("=== Library Returned Media Late Fee Batch ===");

        Media[] returnedBatch = {
            new Book("Clean Code", 4),
            new DVD("Inception", 3),
            new Magazine("National Geographic", 5),
            new Book("Java Complete Reference", 2)
        };

        double totalFees = 0.0;

        for (Media item : returnedBatch) {
            double fee = item.computeLateFee();
            totalFees += fee;
            System.out.printf("%-10s : %-25s | Days late: %2d | Fee: Rs. %.2f%n",
                    item.getClass().getSimpleName(), item.getTitle(), item.getDaysLate(), fee);
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("Total Late Fee for Batch: Rs. %.2f%n", totalFees);
    }
}

