public class CinemaShow {

    // (a) Instance variables
    private String title;
    private int seatsAvailable;
    private final int capacity;

    // Static variable shared by all CinemaShow objects
    private static int totalBooked = 0;


    // (b) Constructor with title and capacity
    public CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }


    // Constructor chaining
    // Default capacity = 100
    public CinemaShow(String title) {
        this(title, 100);
    }


    // (c) Book seats
    public boolean book(int n) {

        if (n <= seatsAvailable) {

            seatsAvailable = seatsAvailable - n;
            totalBooked = totalBooked + n;

            return true;

        } else {

            return false;
        }
    }


    // (d) Cancel seats
    public void cancel(int n) {

        seatsAvailable = seatsAvailable + n;

        // Seats should never be greater than capacity
        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }


    // (e) Getter for available seats
    public int getSeatsAvailable() {
        return seatsAvailable;
    }


    // Static getter for total booked seats
    public static int getTotalBooked() {
        return totalBooked;
    }


    // (f) Main method
    public static void main(String[] args) {

        // Create CinemaShow object
        CinemaShow show = new CinemaShow("Avengers", 10);


        // Booking 4 seats
        boolean result1 = show.book(4);

        System.out.println("Book 4 seats: " + result1);
        System.out.println("Seats available: "
                + show.getSeatsAvailable());


        // Booking 3 seats
        boolean result2 = show.book(3);

        System.out.println("\nBook 3 seats: " + result2);
        System.out.println("Seats available: "
                + show.getSeatsAvailable());


        // Trying to book more seats than available
        boolean result3 = show.book(5);

        System.out.println("\nBook 5 seats: " + result3);
        System.out.println("Seats available: "
                + show.getSeatsAvailable());


        // Cancel 2 seats
        show.cancel(2);

        System.out.println("\nCancel 2 seats");
        System.out.println("Seats available: "
                + show.getSeatsAvailable());


        // Booking again
        boolean result4 = show.book(4);

        System.out.println("\nBook 4 seats: " + result4);
        System.out.println("Seats available: "
                + show.getSeatsAvailable());


        // Print total successful bookings
        System.out.println("\nTotal successfully booked seats: "
                + CinemaShow.getTotalBooked());
    }
}