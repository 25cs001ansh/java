import java.util.InputMismatchException;
import java.util.Scanner;

// Custom Exception
class DivideByZeroException extends Exception {

    public DivideByZeroException() {
        super("Error: Cannot divide by zero.");
    }
}

public class GuardedCalculator {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean success = false;

        while (!success) {

            try {

                System.out.print("Enter first number: ");
                double num1 = sc.nextDouble();

                System.out.print("Enter operator (+, -, *, /): ");
                char operator = sc.next().charAt(0);

                System.out.print("Enter second number: ");
                double num2 = sc.nextDouble();

                double result;

                switch (operator) {

                    case '+':
                        result = num1 + num2;
                        break;

                    case '-':
                        result = num1 - num2;
                        break;

                    case '*':
                        result = num1 * num2;
                        break;

                    case '/':
                        if (num2 == 0) {
                            throw new DivideByZeroException();
                        }

                        result = num1 / num2;
                        break;

                    default:
                        System.out.println("Invalid operator. Try again.");
                        continue;
                }

                System.out.println("Result = " + result);
                success = true;

            }

            catch (InputMismatchException e) {

                System.out.println(
                    "Invalid number input. Please enter numbers only."
                );

                sc.nextLine();
            }

            catch (DivideByZeroException e) {

                System.out.println(e.getMessage());
            }

            finally {

                System.out.println(
                    "Calculation attempt completed."
                );
            }
        }

        sc.close();
    }
}