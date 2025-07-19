package turingMachine;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the tape of a Turing Machine.
 * Manages the tape content and head position.
 * Uses a HashMap for efficient sparse tape representation.
 */
public class Tape {
    private final Map<Integer, Character> tape;
    private int headPosition;
    private final char blankSymbol;

    public Tape(char blankSymbol) {
        this.tape = new HashMap<>();
        this.headPosition = 0;
        this.blankSymbol = blankSymbol;
    }

    /**
     * Loads input string onto the tape starting from position 0
     */
    public void loadInput(String input) {
        tape.clear();
        headPosition = 0;
        for (int i = 0; i < input.length(); i++) {
            tape.put(i, input.charAt(i));
        }
    }

    /**
     * Reads the symbol at the current head position
     */
    public char read() {
        return tape.getOrDefault(headPosition, blankSymbol);
    }

    /**
     * Writes a symbol at the current head position
     */
    public void write(char symbol) {
        if (symbol == blankSymbol) {
            tape.remove(headPosition);
        } else {
            tape.put(headPosition, symbol);
        }
    }

    /**
     * Moves the tape head according to the specified direction
     */
    public void moveHead(MultiTapeTransition.Direction direction) {
        switch (direction) {
            case LEFT -> headPosition--;
            case RIGHT -> headPosition++;
            case STAY -> {}
        }
    }

    /**
     * Gets the current head position
     */
    public int getHeadPosition() {
        return headPosition;
    }

    /**
     * Gets the tape content between start and end positions (inclusive)
     */
    public String getTapeContent(int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            sb.append(tape.getOrDefault(i, blankSymbol));
        }
        return sb.toString();
    }

    /**
     * Gets the minimum position that contains a non-blank symbol
     */
    public int getMinPosition() {
        return tape.keySet().stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
    }

    /**
     * Gets the maximum position that contains a non-blank symbol
     */
    public int getMaxPosition() {
        return tape.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    /**
     * Clears the tape and resets head position
     */
    public void clear() {
        tape.clear();
        headPosition = 0;
    }

    /**
     * Gets the blank symbol used by this tape
     */
    public char getBlankSymbol() {
        return blankSymbol;
    }

    /**
     * Checks if the tape is empty (contains only blank symbols)
     */
    public boolean isEmpty() {
        return tape.isEmpty();
    }

    /**
     * Resets the head position to 0 without clearing tape content
     */
    public void resetHeadPosition() {
        headPosition = 0;
    }
}