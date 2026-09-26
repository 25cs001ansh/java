// Functional interface
@FunctionalInterface
interface Notifier {

    void send(String message);
}


// Marker interface
interface Urgent {
}


// Email sender
class EmailNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println(
            "Email: " + message
        );
    }
}


// Urgent SMS sender
class SMSNotifier implements Notifier, Urgent {

    @Override
    public void send(String message) {
        System.out.println(
            "SMS: " + message
        );
    }
}


public class NotificationSenders {

    public static void main(String[] args) {

        // Email lambda
        Notifier email =
            message -> System.out.println(
                "Email: " + message
            );


        // SMS lambda
        Notifier sms =
            message -> System.out.println(
                "SMS: " + message
            );


        // Array of senders
        Notifier[] senders = {
            email,
            sms
        };


        String message =
            "Important announcement!";


        // Broadcast message
        for (Notifier sender : senders) {

            sender.send(message);
        }
    }
}