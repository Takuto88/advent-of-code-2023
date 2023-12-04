package de.takuto.dec4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class AoCPuzzlePartOne {
    private static final String INPUT_FILE = "src/main/resources/dec4/puzzle.input";

    public static void main(final String[] args) {
        new AoCPuzzlePartOne().solve();
    }

    private void solve() {
        try (final var lines = Files.lines(Path.of(INPUT_FILE))) {
            final var totalPoints = new AtomicInteger(0);
            lines.forEach(line -> {
                final var splitByColon = line.split(":");
                final var splitBySpace = splitByColon[1].split(" ");
                final var name = splitByColon[0];

                final var numberSetWeHave = new HashSet<Integer>();
                final var numberSetOfWinningNumbers = new HashSet<Integer>();

                var isNumbersWeHave = false;
                for (var i = 2; i < splitBySpace.length; i++) {
                    if (splitBySpace[i].equals( "|")) {
                        isNumbersWeHave = true;
                        continue;
                    }

                    if (splitBySpace[i].equals("")) {
                        continue;
                    }

                    final var currentNumber = Integer.parseInt(splitBySpace[i]);
                    if (isNumbersWeHave) {
                        numberSetWeHave.add(currentNumber);
                        continue;
                    }

                    numberSetOfWinningNumbers.add(currentNumber);
                }

                final var points = new AtomicInteger(0);
                numberSetWeHave.stream()
                    .filter(numberSetOfWinningNumbers::contains)
                    .forEach(num -> points.set(points.get() == 0 ? 1 : points.get() * 2));

                System.out.println(name + " " + "is worth: " + points.get());
                totalPoints.addAndGet(points.get());
            });

            System.out.println("The entire stack is worth " + totalPoints.get() + " points.");
        } catch (final IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

}
