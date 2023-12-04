package de.takuto.dec4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class AoCPuzzlePartTwo {
    private static final String INPUT_FILE = "src/main/resources/dec4/puzzle.input";

    public static void main(final String[] args) {
        new AoCPuzzlePartTwo().solve();
    }

    private void solve() {
        try (final var lines = Files.lines(Path.of(INPUT_FILE))) {
            final var amountWinningNumsOfCard = new HashMap<Integer, Integer>();
            final var amountOfCardCopies = new HashMap<Integer, Integer>();

            lines.forEach(line -> {
                final var splitByColon = line.split(":");
                final var splitBySpace = splitByColon[1].split(" ");
                final var name = splitByColon[0]
                    .replaceFirst("Card", "")
                    .replaceAll(" ", "");
                final var cardNum = Integer.parseInt(name);

                final var numberSetWeHave = new HashSet<Integer>();
                final var numberSetOfWinningNumbers = new HashSet<Integer>();

                var isNumbersWeHave = false;
                for (var i = 0; i < splitBySpace.length; i++) {
                    if (splitBySpace[i].equals("|")) {
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

                final var winningNumbers = numberSetWeHave.stream()
                    .filter(numberSetOfWinningNumbers::contains)
                    .toList()
                    .size();

                amountWinningNumsOfCard.put(cardNum, winningNumbers);
                amountOfCardCopies.put(cardNum, 1);
            });

            amountWinningNumsOfCard.keySet()
                .stream()
                .filter(key -> amountWinningNumsOfCard.get(key) > 0)
                .forEach(cardNumber -> {
                    final var cardsBelow = amountWinningNumsOfCard.get(cardNumber);
                    System.out.println("Card " + cardNumber + " has " + cardsBelow + " winning numbers.");
                    for (var i = 1; i <= cardsBelow; i++) {
                        final var currentCard = cardNumber + i;
                        amountOfCardCopies.compute(currentCard, (key, val) -> val + amountOfCardCopies.get(cardNumber));
                        final var currentCopies = amountOfCardCopies.get(currentCard);
                        System.out.println("\t\t" + cardNumber + "...Card " + currentCard + " has " + currentCopies + " copies.");
                    }
                });

            final var totalScratchCards = amountOfCardCopies.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println("The entire stack contains " + totalScratchCards+ " cards.");
        } catch (final IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}
