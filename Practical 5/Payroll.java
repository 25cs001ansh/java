abstract class Employee {
    String name;
    int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee {
    double fixedSalary;

    FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    @Override
    double monthlySalary() {
        return fixedSalary;
    }
}

class PartTime extends Employee {
    int hours;
    double rate;

    PartTime(String name, int id, int hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee {
    double stipend;

    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    @Override
    double monthlySalary() {
        return stipend;
    }
}

public class Payroll {
    public static void main(String[] args) {

        Employee[] employees = {
            new FullTime("Ansh", 101, 50000),
            new PartTime("Rahul", 102, 80, 300),
            new Intern("Jenil", 103, 15000),
            new FullTime("Ayush", 104, 45000)
        };

        double total = 0;

        for (Employee e : employees) {
            double salary = e.monthlySalary();

            System.out.println("Name: " + e.name);
            System.out.println("ID: " + e.id);
            System.out.println("Monthly Salary: ₹" + salary);

            if (e instanceof Intern) {
                System.out.println("Note: This employee is an Intern.");
            }

            System.out.println("------------------------");

            total += salary;
        }

        System.out.println("Total Payroll: ₹" + total);
    }
}