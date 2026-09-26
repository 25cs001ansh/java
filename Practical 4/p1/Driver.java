package p1;

public class Driver {

    public static void main(String[] args) {

        String[] passwords = {
            "abc",
            "Abcdefgh",
            "abcd1234",
            "Abcd1234!",
            "hello@"
        };

        for (String pw : passwords) {

            System.out.println("\nPassword: " + pw);

            System.out.println("Length >= 8: "
                    + PasswordChecker.hasLength(pw));

            System.out.println("Contains uppercase: "
                    + PasswordChecker.hasUppercase(pw));

            System.out.println("Contains digit: "
                    + PasswordChecker.hasDigit(pw));

            System.out.println("Contains special character: "
                    + PasswordChecker.hasSpecial(pw));

            System.out.println("Strength: "
                    + PasswordChecker.strength(pw));
        }
    }
}