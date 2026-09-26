interface Switchable {

    void on();

    void off();

    // Default method
    default void toggle() {
        System.out.println("Device toggled");
    }
}


// Functional interface
@FunctionalInterface
interface SwitchDecision {

    boolean maySwitchOn(Switchable device, int hour);
}


// Fan implements Switchable
class Fan implements Switchable {

    @Override
    public void on() {
        System.out.println("Fan is ON");
    }

    @Override
    public void off() {
        System.out.println("Fan is OFF");
    }

    @Override
    public void toggle() {
        System.out.println("Fan toggled");
    }
}


// Light implements Switchable
class Light implements Switchable {

    @Override
    public void on() {
        System.out.println("Light is ON");
    }

    @Override
    public void off() {
        System.out.println("Light is OFF");
    }

    @Override
    public void toggle() {
        System.out.println("Light toggled");
    }
}


public class RemoteControl {

    public static void main(String[] args) {

        // Array of interface references
        Switchable[] devices = {
            new Fan(),
            new Light()
        };


        System.out.println("Toggling all devices:");

        // Loop through all devices
        for (Switchable device : devices) {
            device.toggle();
        }


        // Anonymous class
        SwitchDecision anonymousDecision =
            new SwitchDecision() {

                @Override
                public boolean maySwitchOn(
                        Switchable device, int hour) {

                    return hour >= 6 && hour <= 22;
                }
            };


        // Lambda expression
        SwitchDecision lambdaDecision =
            (device, hour) -> hour >= 8 && hour <= 20;


        int hour = 10;

        System.out.println("\nAnonymous class decision:");

        for (Switchable device : devices) {

            if (anonymousDecision.maySwitchOn(device, hour)) {
                device.on();
            } else {
                System.out.println(
                    "Device cannot switch ON at hour " + hour
                );
            }
        }


        System.out.println("\nLambda decision:");

        for (Switchable device : devices) {

            if (lambdaDecision.maySwitchOn(device, hour)) {
                device.on();
            } else {
                System.out.println(
                    "Device cannot switch ON at hour " + hour
                );
            }
        }
    }
}