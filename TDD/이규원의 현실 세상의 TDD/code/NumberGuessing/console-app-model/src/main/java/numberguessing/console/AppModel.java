package numberguessing.console;

import java.util.stream.Stream;

import numberguessing.PositiveIntegerGenerator;

public final class AppModel {

    private final static String NEW_LINE = System.lineSeparator();
    private static final String SELECT_MODE_MESSAGE = "1: Single player game" + NEW_LINE + "2: Multiplayer game"
            + NEW_LINE + "3: Exit" + NEW_LINE + "Enter selection: ";

    interface Processor {
        Processor run(String input);
    }

    private final TextOutput output;
    private final PositiveIntegerGenerator generator;
    private boolean completed;
    private Processor processor;

    public AppModel(PositiveIntegerGenerator generator) {
        output = new TextOutput(SELECT_MODE_MESSAGE);
        this.generator = generator;
        completed = false;
        processor = this::processModeSelection;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String flushOutput() {
        return output.flushOutput();
    }

    public void processInput(String input) {
        processor = processor.run(input);
    }

    private Processor processModeSelection(String input) {
        if (input.equals("1")) {
            printLines("Single player game", "I'm thinking of a number between 1 and 100.", "Enter your guess: ");
            int answer = generator.generateLessThanOrEqualToHundread();
            return getSinglePlayerGameProcessor(answer, 1);
        } else if (input.equals("2")) {
            printLines("Multiplayer game", "Enter player names separated with commas: ");
            return startMultiPlayerGame();
        } else {
            completed = true;
            return null;
        }
    }

    private Processor startMultiPlayerGame() {
        return input -> {
            Object[] players = Stream.of(input.split(",")).map(String::trim).toArray();
            printLines("I'm thinking of a number between 1 and 100.", "Enter " + players[0] + "'s guess: ");
            int answer = generator.generateLessThanOrEqualToHundread();
            return getMultiPlayerGameProcessor(players, answer, 1);
        };
    }

    private Processor getMultiPlayerGameProcessor(Object[] players, int answer, int tries) {
        return input -> {
            int guess = Integer.parseInt(input);

            Object currentPlayer = players[(tries - 1) % players.length];
            Object nextPlayer = players[tries % players.length];

            if (guess < answer) {
                printLines(currentPlayer + "'s guess is too low.", "Enter " + nextPlayer + "'s guess: ");
                return getMultiPlayerGameProcessor(players, answer, tries + 1);
            } else if (guess > answer) {
                printLines(currentPlayer + "'s guess is too high.", "Enter " + nextPlayer + "'s guess: ");
                return getMultiPlayerGameProcessor(players, answer, tries + 1);
            } else {
                printLines("Correct! " + currentPlayer + " wins.", SELECT_MODE_MESSAGE);
                return this::processModeSelection;
            }
        };
    }

    private Processor getSinglePlayerGameProcessor(int answer, int tries) {
        return input -> {
            int guess = Integer.parseInt(input);
            if (guess < answer) {
                printLines("Your guess is too low.", "Enter your guess: ");
                return getSinglePlayerGameProcessor(answer, tries + 1);
            } else if (guess > answer) {
                printLines("Your guess is too high.", "Enter your guess: ");
                return getSinglePlayerGameProcessor(answer, tries + 1);
            } else {
                printLines("Correct! " + tries + (tries == 1 ? " guess." : " guesses."), SELECT_MODE_MESSAGE);
                return this::processModeSelection;
            }
        };
    }

    private void printLines(String... lines) {
        output.printLines(lines);
    }

}
