package turingMachine;

import java.util.Arrays;

/**
 * Represents a transition in a multi-tape Turing machine.
 * Each transition specifies what to read, write, and which direction to move for each tape.
 */
public class MultiTapeTransition {
    private final String currentState;
    private final char[] readSymbols;
    private final String nextState;
    private final char[] writeSymbols;
    private final Direction[] directions;

    public enum Direction {
        LEFT, RIGHT, STAY
    }

    public MultiTapeTransition(String currentState, char[] readSymbols, String nextState,
                               char[] writeSymbols, Direction[] directions) {
        if (readSymbols.length != writeSymbols.length ||
                readSymbols.length != directions.length) {
            throw new IllegalArgumentException("All arrays must have the same length");
        }

        this.currentState = currentState;
        this.readSymbols = Arrays.copyOf(readSymbols, readSymbols.length);
        this.nextState = nextState;
        this.writeSymbols = Arrays.copyOf(writeSymbols, writeSymbols.length);
        this.directions = Arrays.copyOf(directions, directions.length);
    }

    // Getters
    public String getCurrentState() { return currentState; }
    public char[] getReadSymbols() { return Arrays.copyOf(readSymbols, readSymbols.length); }
    public String getNextState() { return nextState; }
    public char[] getWriteSymbols() { return Arrays.copyOf(writeSymbols, writeSymbols.length); }
    public Direction[] getDirections() { return Arrays.copyOf(directions, directions.length); }

    public int getNumTapes() { return readSymbols.length; }

    @Override
    public String toString() {
        return String.format("(%s, %s) -> (%s, %s, %s)",
                currentState, Arrays.toString(readSymbols),
                nextState, Arrays.toString(writeSymbols), Arrays.toString(directions));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MultiTapeTransition that = (MultiTapeTransition) obj;
        return currentState.equals(that.currentState) &&
                Arrays.equals(readSymbols, that.readSymbols) &&
                nextState.equals(that.nextState) &&
                Arrays.equals(writeSymbols, that.writeSymbols) &&
                Arrays.equals(directions, that.directions);
    }

    @Override
    public int hashCode() {
        int result = currentState.hashCode();
        result = 31 * result + Arrays.hashCode(readSymbols);
        result = 31 * result + nextState.hashCode();
        result = 31 * result + Arrays.hashCode(writeSymbols);
        result = 31 * result + Arrays.hashCode(directions);
        return result;
    }
}