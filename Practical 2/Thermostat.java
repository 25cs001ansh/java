public class Thermostat {

    // (a) Instance variables
    private String location;
    private int temperature;

    // Constants
    private static final int MIN = 16;
    private static final int MAX = 30;

    // Static variable
    private static int activeCount = 0;


    // (b) Constructor with location and startTemp
    public Thermostat(String location, int startTemp) {

        this.location = location;

        // Check whether temperature is within range
        if (startTemp >= MIN && startTemp <= MAX) {
            this.temperature = startTemp;
        } else {
            this.temperature = 22;
        }

        // Increase active thermostat count
        activeCount++;
    }


    // (c) Constructor chaining
    public Thermostat(String location) {

        this(location, 22);
    }


    // (d) Increase temperature
    public void raise() {

        if (temperature < MAX) {
            temperature++;
        } else {
            System.out.println("Already at maximum (30)");
        }
    }


    // (e) Decrease temperature
    public void lower() {

        if (temperature > MIN) {
            temperature--;
        } else {
            System.out.println("Already at minimum (16)");
        }
    }


    // (f) Getter for temperature
    public int getTemperature() {
        return temperature;
    }


    // Static getter for active count
    public static int getActiveCount() {
        return activeCount;
    }


    // (g) Main method
    public static void main(String[] args) {

        // Create two thermostat objects
        Thermostat t1 = new Thermostat("Room", 22);

        Thermostat t2 = new Thermostat("Hall");


        System.out.println("Initial temperature: "
                + t1.getTemperature());


        // Raise temperature 10 times
        System.out.println("\nRaising temperature:");

        for (int i = 1; i <= 10; i++) {

            t1.raise();

            System.out.println("Temperature: "
                    + t1.getTemperature());
        }


        // Lower temperature 20 times
        System.out.println("\nLowering temperature:");

        for (int i = 1; i <= 20; i++) {

            t1.lower();

            System.out.println("Temperature: "
                    + t1.getTemperature());
        }


        // Print active thermostat count
        System.out.println("\nActive thermostats: "
                + Thermostat.getActiveCount());
    }
}