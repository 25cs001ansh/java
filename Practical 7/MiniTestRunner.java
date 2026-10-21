import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

public class MiniTestRunner {

    static class SampleTests {

        @Run
        public void firstTest() {
            System.out.println("First test ran");
        }

        public void skippedMethod() {
            System.out.println("This method should not run");
        }

        @Run
        public void secondTest() {
            System.out.println("Second test ran");
        }
    }

    public static void main(String[] args) throws ReflectiveOperationException {
        SampleTests tests = new SampleTests();
        int count = 0;

        for (Method method : tests.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {
                method.invoke(tests);
                count++;
            }
        }

        System.out.println("Tests run: " + count);
    }
}
