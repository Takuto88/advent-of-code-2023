package de.takuto.dec2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AoCPuzzlePartOne {
    private static final String INPUT_FILE = "src/main/resources/dec2/puzzle.input";

    public static void main(final String[] args) {
        final var puzzle = new AoCPuzzlePartOne();
        puzzle.run();
    }

    private void run() {
        final var totalCubes = new CubeSet(12, 13, 14);

        try (final var lines = Files.lines(Path.of(INPUT_FILE))) {
            final var sumOfGameIds = new AtomicInteger(0);
            lines.forEach(line -> {
                final var cubeSetList = readCubeSets(line);
                var possible = true;
                for (final var cubeSet : cubeSetList) {
                    if (cubeSet.redCubes > totalCubes.redCubes
                        || cubeSet.greenCubes > totalCubes.greenCubes
                        || cubeSet.blueCubes > totalCubes.blueCubes) {
                        possible = false;
                        break;
                    }
                }

                if (possible) {
                    final var gameId = readGameId(line);
                    System.out.println("Game " + gameId + " is possible");
                    sumOfGameIds.addAndGet(gameId);
                }

            });
            System.out.println("Sum of game ids: " + sumOfGameIds);
        } catch (final IOException e) {
            System.exit(1);
        }

    }

    private static int readGameId(final String line) {
        final var chars = line.toCharArray();
        final var gameId = new StringBuilder();

        // Line starts with "Game xxx:", skip the "Game " part, then read the game id
        for (var i = 5; i < chars.length; i++) {
            if (chars[i] == ':') {
                break;
            }
            gameId.append(chars[i]);
        }

        return Integer.parseInt(gameId.toString());
    }

    private static List<CubeSet> readCubeSets(final String line) {
        final var chars = line.toCharArray();
        final var cubeSets = new LinkedList<CubeSet>();

        // Line starts with "Game xxx: ", so skip the first chars
        var redCubes = 0;
        var greenCubes = 0;
        var blueCubes = 0;
        final var amountString = new StringBuilder();
        for (var i = line.indexOf(':') + 1; i < chars.length; i++) {
            if(isDigit(chars[i])) {
                amountString.append(chars[i]);
                continue;
            }

            // Skip spaces between numbers and commas between cubes
            if(chars[i] == ' ' || chars[i] == ',') {
                continue;
            }

            // We've completed a set, so add it to the list and reset the counters
            if (chars[i] == ';') {
                cubeSets.add(new CubeSet(redCubes, greenCubes, blueCubes));
                redCubes = 0;
                greenCubes = 0;
                blueCubes = 0;
                continue;
            }

            // We've found a color indication
            switch(chars[i]) {
                case 'r':
                    redCubes = Integer.parseInt(amountString.toString());
                    i+= 2; // Skip the "ed" part
                    break;
                case 'g':
                    greenCubes = Integer.parseInt(amountString.toString());
                    i+= 4; // Skip the "reen" part
                    break;
                case 'b':
                    blueCubes = Integer.parseInt(amountString.toString());
                    i+= 3; // Skip the "lue" part
                    break;
            }
            amountString.setLength(0); // Reset string builder
        }

        // Add the last cube set and return
        cubeSets.add(new CubeSet(redCubes, greenCubes, blueCubes));

        return cubeSets;
    }

    private static boolean isDigit(final char c) {
        return c >= 0x30 && c <= 0x39;
    }

    private record CubeSet(int redCubes, int greenCubes, int blueCubes){}
}
