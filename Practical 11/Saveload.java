import java.io.*;
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int marks;
    transient String password;

    public Student(String name, int marks, String password) {
        this.name = name;
        this.marks = marks;
        this.password = password;
    }

    public String toString() {
        return "Name: " + name + ", Marks: " + marks + ", Password: " + password;
    }
}

public class Saveload {
    public static void main(String[] args) {
        Student[] list ={
            new Student("ansh", 85, "ansh123"),
            new Student("Bob", 90, "bob456"),
            new Student("Charlie", 78, "charlie789")
        };
        String file = "students.dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(list);
        } catch (IOException e){
            System.out.println("save failed: " + e.getMessage()); return;    
        } 
        System.out.println("saved");
        for (Student s : list){
            System.out.println(" "+ s);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Student[] loaded = (Student[]) in.readObject();
            System.out.println("loaded(fresh objects):");
            for (Student s : loaded){
                System.out.println(" "+ s);
            }
            System.out.println("same object? " + (list[0] == loaded[0]));
            System.out.println("Name/marks survived? " + (list[0].name.equals(loaded[0].name) && list[0].marks == loaded[0].marks));
            System.out.println("Transient field saved? " + (loaded[0].password != null));
        } catch (IOException | ClassNotFoundException e){
            System.out.println("load failed: " + e.getMessage()); return;    
        }
    }
};
    