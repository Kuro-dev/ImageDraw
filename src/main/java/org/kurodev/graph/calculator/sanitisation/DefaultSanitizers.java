package org.kurodev.graph.calculator.sanitisation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DefaultSanitizers implements Sanitizer {
    INFER_MULTIPLY_FOR_VARS {
        private final Pattern pattern = Pattern.compile("\\d[a-zA-Z]");

        @Override
        public String sanitize(String formula) {
            StringBuilder builder = new StringBuilder();
            Matcher m = pattern.matcher(formula);
            int lastEnd = 0;
            while (m.find()) {
                int start = m.start() + 1;
                builder.append(formula, lastEnd, start)
                        .append("*")
                        .append(formula.charAt(m.end() - 1));
                lastEnd = m.end();
            }
            builder.append(formula.substring(lastEnd));
            return builder.toString();
        }
    }
}
