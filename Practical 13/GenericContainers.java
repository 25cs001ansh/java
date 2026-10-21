class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericContainers {

    static <T> T first(T[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        return values[0];
    }

    public static void main(String[] args) {
        Box<String> messageBox = new Box<>();
        messageBox.set("Generics provide type safety");
        System.out.println("Box: " + messageBox.get());

        Box<Integer> numberBox = new Box<>();
        numberBox.set(42);
        int number = numberBox.get();
        System.out.println("Integer box: " + number);

        Pair<String, Integer> student = new Pair<>("Ansh", 95);
        String name = student.getFirst();
        int marks = student.getSecond();
        System.out.println("Pair: " + name + ", " + marks);

        String[] names = {"Ansh", "Mia", "Noah"};
        Integer[] scores = {85, 92, 78};
        System.out.println("First name: " + first(names));
        System.out.println("First score: " + first(scores));
    }
}
