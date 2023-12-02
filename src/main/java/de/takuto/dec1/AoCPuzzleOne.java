package de.takuto.dec1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class AoCPuzzleOne {
    private static final String INPUT_FILE = "src/main/resources/dec1/p1.input";

    public static void main(final String[] args) throws IOException {

        final AtomicInteger sum = new AtomicInteger(0);
        try (final Stream<String> lines = Files.lines(Path.of(INPUT_FILE))) {
            lines.forEach(line -> {
                final String leftMostDigit = findLeftMost(line);
                final String rightMostDigit = findRightMost(line);
                sum.addAndGet(Integer.parseInt(leftMostDigit + rightMostDigit));
            });
        }

        System.out.println("Sum: " + sum);
    }

    private static String findLeftMost(final String line) {
        final char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // Is it a digit?
            if (chars[i] < 0x30 || chars[i] > 0x39) {
                continue;
            }
            return String.valueOf(chars[i]);
        }
        return "";
    }

    private static String findRightMost(final String line) {
        final char[] chars = line.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            // Is it a digit?
            if (chars[i] < 0x30 || chars[i] > 0x39) {
                continue;
            }
            return String.valueOf(chars[i]);
        }
        return "";
    }
}
