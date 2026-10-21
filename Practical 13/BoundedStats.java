public class BoundedStats {

    static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }

        double sum = 0;
        for (N value : values) {
            sum += value.doubleValue();
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        Integer[] integers = {10, 20, 30};
        Double[] doubles = {2.5, 3.5, 4.0};
        Long[] longs = {100L, 200L, 300L};

        System.out.println("Integer average: " + average(integers));
        System.out.println("Double average: " + average(doubles));
        System.out.println("Long average: " + average(longs));
        System.out.println(
                "String[] is not accepted because String does not extend Number.");
    }
}
