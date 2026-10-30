package p3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateFiller {

    // (b) and (c) Fill placeholders using regex Pattern and Matcher
    public static String fill(String template, String[] names, String[] values) {
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String placeholderKey = matcher.group(1);
            String replacement = "[?]";

            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(placeholderKey)) {
                    if (i < values.length && values[i] != null) {
                        replacement = values[i];
                    }
                    break;
                }
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}

