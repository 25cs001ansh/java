import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ColumnMapper {

    // 1. Define @Column(name) annotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Column {
        String name();
    }

    // Sample class to populate
    static class Student {
        @Column(name = "roll_no")
        private int rollNo;

        @Column(name = "full_name")
        private String name;

        @Column(name = "marks")
        private double marks;

        @Column(name = "grade") // Column intentionally missing in input row
        private String grade;

        public Student() {}

        @Override
        public String toString() {
            return "Student [rollNo=" + rollNo + ", name=" + name +
                   ", marks=" + marks + ", grade=" + (grade == null ? "(default: null)" : grade) + "]";
        }
    }

    // 2. Populate object's fields from header row and data row using reflection
    public static <T> T populate(Class<T> clazz, String[] headers, String[] dataRow)
            throws ReflectiveOperationException {
        T instance = clazz.getDeclaredConstructor().newInstance();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column col = field.getAnnotation(Column.class);
                String columnName = col.name();

                // Find matching column index in headers
                int matchIndex = -1;
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equalsIgnoreCase(columnName)) {
                        matchIndex = i;
                        break;
                    }
                }

                field.setAccessible(true);

                // Handle missing column or populate value
                if (matchIndex != -1 && matchIndex < dataRow.length) {
                    String rawValue = dataRow[matchIndex];
                    Object convertedValue = convertType(field.getType(), rawValue);
                    field.set(instance, convertedValue);
                } else {
                    System.out.println("[Warning] Column '" + columnName + "' is missing in header/data. Leaving field as default.");
                }
            }
        }

        return instance;
    }

    // Simple type converter helper
    private static Object convertType(Class<?> type, String value) {
        if (value == null) return null;
        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        return value;
    }

    public static void main(String[] args) throws ReflectiveOperationException {
        System.out.println("=== Reflective Column Mapper ===");

        // Header row and matching data row
        String[] headers = { "roll_no", "full_name", "marks" };
        String[] dataRow = { "101", "Ansh", "92.5" };

        System.out.println("Headers  : " + Arrays.toString(headers));
        System.out.println("Data Row : " + Arrays.toString(dataRow));
        System.out.println();

        // Populate Student object
        Student student = populate(Student.class, headers, dataRow);

        System.out.println("\nPopulated Object Result:");
        System.out.println(student);
    }
}

