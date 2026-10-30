package p3;

public class Driver {

    public static void main(String[] args) {
        // (a) Template and parallel arrays
        String template = "Dear {name}, order {id} ships {date}.";
        String[] names = { "name", "id" };
        String[] values = { "Riya", "A07" };

        System.out.println("Template : " + template);

        // (b), (c) Fill template
        String result = TemplateFiller.fill(template, names, values);

        // (d) Print filled template
        System.out.println("Filled   : " + result);
    }
}

