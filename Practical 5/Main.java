abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double length, width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }
}

class Triangle extends Shape {
    double base, height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    double area() {
        return 0.5 * base * height;
    }
}

public class Main {
    public static void main(String[] args) {

        Shape[] shapes = {
            new Circle(5),
            new Rectangle(10, 4),
            new Triangle(6, 8),
            new Circle(3)
        };

        double total = 0;
        double largest = 0;

        for (Shape shape : shapes) {
            double currentArea = shape.area();

            System.out.println(
                shape.getClass().getSimpleName() +
                " Area = " + currentArea
            );

            total += currentArea;

            if (currentArea > largest) {
                largest = currentArea;
            }
        }

        System.out.println("\nTotal Area = " + total);
        System.out.println("Largest Area = " + largest);
    }
}