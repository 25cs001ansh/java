package minibank.util;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import minibank.model.annotation.Audit;

public class AuditLogger {

    private AuditLogger() {
    }

    public static void logIfAudited(Object target, String methodName) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName) && method.isAnnotationPresent(Audit.class)) {
                System.out.println("[AUDIT] " + target.getClass().getSimpleName()
                        + "." + methodName + "() called at " + LocalDateTime.now());
                return;
            }
        }
    }
}