package de.takuto.de.takuto.dec3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AoCPuzzlePartOne {

    private static final String INPUT_FILE = "src/main/resources/dec3/puzzle.input";

    public static void main(final String[] args) {
        new AoCPuzzlePartOne().solve();
    }

    public void solve() {
        final char[][] engineSchematic;
        try (final var lineStream = Files.lines(Path.of(INPUT_FILE))) {
            final var lines = lineStream.toList();
            engineSchematic = new char[lines.size()][lines.get(0).length()];

            // Copy lines into 2D array
            for (int i = 0; i < lines.size(); i++) {
                final var line = lines.get(i);
                for (int j = 0; j < line.length(); j++) {
                    engineSchematic[i][j] = line.charAt(j);
                }
            }

            var sumOfNumbers = 0;

            // Iterate over 2D array and find symbols
            for (int i = 0; i < engineSchematic.length; i++) {
                for (int j = 0; j < engineSchematic[i].length; j++) {
                    if (isSymbol(engineSchematic[i][j])) {
                        final var adjacentNumbers = adjacentNumbers(engineSchematic, i, j);
                        System.out.println("Symbol " + engineSchematic[i][j] + " at " + i + "," + j + " has adjacent numbers: " + adjacentNumbers);
                        sumOfNumbers += adjacentNumbers.stream().mapToInt(Integer::intValue).sum();
                    }
                }
            }

            System.out.println("Sum of all adjacent numbers: " + sumOfNumbers);

        } catch (final IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }


    private static boolean isDigit(final char c) {
        return c >= 0x30 && c <= 0x39;
    }

    private static boolean isSymbol(final char c) {
        return !isDigit(c) && c != '.';
    }

    // The use of a set de-duplicates the numbers if there is a number that starts diagonal to a symbol
    // and continues below it. This way we only count the number once.
    // If this changes to a list, this will break the algorithm.
    private static Set<Integer> adjacentNumbers(final char[][] engineSchematic, final int x, final int y) {
        final var adjacentNumbers = new HashSet<Integer>();

        // Left
        if (y > 0 && isDigit(engineSchematic[x][y - 1])) {
            adjacentNumbers.add(getNumber(engineSchematic, x, y - 1));
        }

        // Right
        if (y < engineSchematic[x].length - 1 && isDigit(engineSchematic[x][y + 1])) {
            adjacentNumbers.add(getNumber(engineSchematic, x, y + 1));
        }

        // Up
        if (x > 0 && isDigit(engineSchematic[x - 1][y])) {
            adjacentNumbers.add(getNumber(engineSchematic, x - 1, y));
        }

        // Down
        if (x < engineSchematic.length - 1 && isDigit(engineSchematic[x + 1][y])) {
            adjacentNumbers.add(getNumber(engineSchematic, x + 1, y));
        }

        // Up-Left
        if (x > 0 && y > 0 && isDigit(engineSchematic[x - 1][y - 1])) {
            adjacentNumbers.add(getNumber(engineSchematic, x - 1, y - 1));
        }

        // Up-Right
        if (x > 0 && y < engineSchematic[x].length - 1 && isDigit(engineSchematic[x - 1][y + 1])) {
            adjacentNumbers.add(getNumber(engineSchematic, x - 1, y + 1));
        }

        // Down-Left
        if (x < engineSchematic.length - 1 && y > 0 && isDigit(engineSchematic[x + 1][y - 1])) {
            adjacentNumbers.add(getNumber(engineSchematic, x + 1, y - 1));
        }

        // Down-Right
        if (x < engineSchematic.length - 1 && y < engineSchematic[x].length - 1 && isDigit(engineSchematic[x + 1][y + 1])) {
            adjacentNumbers.add(getNumber(engineSchematic, x + 1, y + 1));
        }

        return adjacentNumbers;
    }

    private static int getNumber(final char[][] engineSchematic, final int x, final int y) {
        if (!isDigit(engineSchematic[x][y])) {
            throw new IllegalArgumentException("Not a number: " + engineSchematic[x][y]);
        }

        // Walk left until we hit a non-digit
        int left = y > 0 ? y - 1 : 0;
        while (left >= 0 && isDigit(engineSchematic[x][left])) {
            left--;
        }

        // Walk right until we hit a non-digit
        int right = y + 1 < engineSchematic[x].length ? y + 1 : engineSchematic[x].length - 1;
        while (right < engineSchematic[x].length && isDigit(engineSchematic[x][right])) {
            right++;
        }

        final var digits = Arrays.copyOfRange(engineSchematic[x], left + 1, right);
        return Integer.parseInt(new String(digits));
    }

}
