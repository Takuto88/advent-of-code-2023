package de.takuto.dec1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class AoCPuzzleTwo {
    private static final String INPUT_FILE = "src/main/resources/dec1/p1.input";

    private enum Direction {
        LEFT, RIGHT
    }


    public static void main(final String[] args) throws IOException {
        final Map<String, Integer> digitTable = new HashMap<>();
        digitTable.put("one", 1);
        digitTable.put("two", 2);
        digitTable.put("three", 3);
        digitTable.put("four", 4);
        digitTable.put("five", 5);
        digitTable.put("six", 6);
        digitTable.put("seven", 7);
        digitTable.put("eight", 8);
        digitTable.put("nine", 9);

        final AtomicInteger sum = new AtomicInteger(0);
        try (final Stream<String> lines = Files.lines(Path.of(INPUT_FILE))) {
            lines.forEach(line -> {
                final String leftMostDigit = find(line, digitTable, Direction.LEFT);
                final String rightMostDigit = find(line, digitTable, Direction.RIGHT);
                System.out.println("Line: " + line);
                System.out.println("Left: " + leftMostDigit + ", Right: " + rightMostDigit);
                sum.addAndGet(Integer.parseInt(leftMostDigit + rightMostDigit));
            });
        }

        System.out.println("Sum: " + sum);
    }

    private static String find(final String line, final Map<String, Integer> digitTable, final Direction direction) {
        final char[] chars = direction == Direction.LEFT ? line.toCharArray() :
            new StringBuilder(line).reverse().toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // Is it a digit?
            if (isDigit(chars[i])) {
                return String.valueOf(chars[i]);
            }

            // Is it a three letter digit?
            if (i + 3 < chars.length) {
                final String slice = getSlice(chars, i, i + 3, direction);
                if (digitTable.containsKey(slice)) {
                    return String.valueOf(digitTable.get(slice));
                }
            }

            // Is it a four letter digit?
            if (i + 4 < chars.length) {
                final String slice = getSlice(chars, i, i + 4, direction);
                if (digitTable.containsKey(slice)) {
                    return String.valueOf(digitTable.get(slice));
                }
            }

            // Is it a five letter digit?
            if (i + 5 < chars.length) {
                final String slice = getSlice(chars, i, i + 5, direction);
                if (digitTable.containsKey(slice)) {
                    return String.valueOf(digitTable.get(slice));
                }
            }
        }

        return "";
    }

    private static boolean isDigit(final char c) {
        return c >= 0x30 && c <= 0x39;
    }

    private static String getSlice(final char[] chars, final int start, final int end, final Direction direction) {
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(chars[i]);
        }
        return direction == Direction.LEFT ? sb.toString() : sb.reverse().toString();
    }

}

