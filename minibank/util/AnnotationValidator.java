package minibank.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import minibank.model.annotation.MaxLength;
import minibank.model.annotation.Positive;

public class AnnotationValidator {

    private AnnotationValidator() {
    }

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();

        // Walk up the class hierarchy so fields declared in a superclass
        // (e.g. balance in the abstract Account class) are checked too,
        // since getDeclaredFields() only returns fields of the exact class.
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive positive = field.getAnnotation(Positive.class);
                        if (value instanceof Number number && number.doubleValue() <= 0) {
                            errors.add(field.getName() + " " + positive.message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength maxLength = field.getAnnotation(MaxLength.class);
                        if (value instanceof String str && str.length() > maxLength.value()) {
                            errors.add(field.getName() + " exceeds max length of " + maxLength.value());
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add("Could not access field: " + field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}