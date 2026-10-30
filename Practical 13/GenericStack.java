import java.util.Arrays;
import java.util.EmptyStackException;

public class GenericStack<T> {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public GenericStack() {
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    public GenericStack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.elements = new Object[initialCapacity];
    }

    // Push element onto the stack
    public void push(T item) {
        if (size == elements.length) {
            // Expand capacity dynamically
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        elements[size++] = item;
    }

    // Pop element from top of stack
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T item = (T) elements[--size];
        elements[size] = null; // Prevent memory leak
        return item;
    }

    // Peek element at top without removing
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return (T) elements[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Array-Backed Stack Demo ===\n");

        // 1. Reuse with String type
        System.out.println("--- Test 1: GenericStack<String> ---");
        GenericStack<String> textStack = new GenericStack<>();
        textStack.push("Google");
        textStack.push("Antigravity");
        textStack.push("Java Generics");

        System.out.println("Stack size: " + textStack.size());
        System.out.println("Top element (peek): " + textStack.peek());

        System.out.print("Popping elements: ");
        while (!textStack.isEmpty()) {
            System.out.print("[" + textStack.pop() + "] ");
        }
        System.out.println();

        // 2. Reuse with Integer type
        System.out.println("\n--- Test 2: GenericStack<Integer> ---");
        GenericStack<Integer> numberStack = new GenericStack<>(2);
        numberStack.push(10);
        numberStack.push(20);
        numberStack.push(30); // Triggers dynamic resize

        System.out.println("Stack size: " + numberStack.size());
        System.out.println("Top element (peek): " + numberStack.peek());

        System.out.print("Popping numbers: ");
        while (!numberStack.isEmpty()) {
            System.out.print("[" + numberStack.pop() + "] ");
        }
        System.out.println();

        // 3. Handling empty case sensibly
        System.out.println("\n--- Test 3: Sensible Empty Stack Handling ---");
        try {
            System.out.println("Attempting pop on empty stack...");
            numberStack.pop();
        } catch (EmptyStackException e) {
            System.out.println("Sensibly handled: EmptyStackException caught! Stack contains no items.");
        }
    }
}

