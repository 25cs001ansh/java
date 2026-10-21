import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Form {

    // NotBlank annotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface NotBlank {
    }

    // MaxLength annotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface MaxLength {
        int value();
    }

    // SignupForm
    static class SignupForm {

        @NotBlank
        @MaxLength(10)
        private String username;

        @NotBlank
        private String email;

        @NotBlank
        @MaxLength(8)
        private String password;

        SignupForm(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    // Validation using Reflection
    static List<String> validate(Object obj)
            throws IllegalAccessException {

        List<String> errors = new ArrayList<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            Object value = field.get(obj);

            // Check @NotBlank
            if (field.isAnnotationPresent(NotBlank.class)) {

                if (value == null ||
                    value.toString().trim().isEmpty()) {

                    errors.add(
                        field.getName() + " must not be blank."
                    );
                }
            }

            // Check @MaxLength
            if (field.isAnnotationPresent(MaxLength.class)) {

                MaxLength maxLength =
                    field.getAnnotation(MaxLength.class);

                if (value != null &&
                    value.toString().length() > maxLength.value()) {

                    errors.add(
                        field.getName()
                        + " must not exceed "
                        + maxLength.value()
                        + " characters."
                    );
                }
            }
        }

        return errors;
    }

    // Display validation result
    static void check(String title, SignupForm form)
            throws IllegalAccessException {

        System.out.println();
        System.out.println("----- " + title + " -----");

        List<String> errors = validate(form);

        if (errors.isEmpty()) {

            System.out.println("Valid form");

        } else {

            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    // Main method
    public static void main(String[] args)
            throws IllegalAccessException {

        // Test 1: Valid form
        check(
            "Good Form",
            new SignupForm(
                "user123",
                "user@example.com",
                "ansh123"
            )
        );

        // Test 2: Blank fields
        check(
            "Blank Fields",
            new SignupForm(
                "",
                "",
                "abc"
            )
        );

        // Test 3: Maximum length exceeded
        check(
            "Exceeding Max Length",
            new SignupForm(
                "thisusernameistoolong",
                "user@example.com",
                "thispasswordistoolong"
            )
        );
    }
}