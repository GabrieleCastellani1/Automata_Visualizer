package turingMachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Core logic class for Multi-Tape Turing Machine.
 * Handles the execution logic, state management, and transition processing for multiple tapes.
 */
public class MultiTapeTuringMachineLogic {
    private final Set<String> states;
    private final Set<Character> alphabet;
    private final Set<Character> tapeAlphabet;
    private final Map<String, Map<String, MultiTapeTransition>> transitionFunction;
    private final String initialState;
    private final Set<String> acceptStates;
    private final Set<String> rejectStates;
    private final char blankSymbol;
    private final int numTapes;

    private String currentState;
    private final Tape[] tapes;
    private boolean isRunning;
    private boolean isAccepted;
    private boolean isRejected;
    private int stepCount;
    private static final int MAX_STEPS = 10000;

    public MultiTapeTuringMachineLogic(Set<String> states, Set<Character> alphabet,
                                       Set<Character> tapeAlphabet, String initialState,
                                       Set<String> acceptStates, Set<String> rejectStates,
                                       char blankSymbol, int numTapes) {
        if (numTapes < 1) {
            throw new IllegalArgumentException("Number of tapes must be at least 1");
        }

        this.states = new HashSet<>(states);
        this.alphabet = new HashSet<>(alphabet);
        this.tapeAlphabet = new HashSet<>(tapeAlphabet);
        this.initialState = initialState;
        this.acceptStates = new HashSet<>(acceptStates);
        this.rejectStates = new HashSet<>(rejectStates);
        this.blankSymbol = blankSymbol;
        this.numTapes = numTapes;
        this.transitionFunction = new HashMap<>();

        this.tapes = new Tape[numTapes];
        for (int i = 0; i < numTapes; i++) {
            this.tapes[i] = new Tape(blankSymbol);
        }

        reset();
    }

    /**
     * Adds a multi-tape transition rule to the machine
     */
    public void addTransition(MultiTapeTransition transition) {
        if (!states.contains(transition.getCurrentState()) ||
                !states.contains(transition.getNextState())) {
            throw new IllegalArgumentException("Invalid state in transition");
        }

        if (transition.getReadSymbols().length != numTapes ||
                transition.getWriteSymbols().length != numTapes ||
                transition.getDirections().length != numTapes) {
            throw new IllegalArgumentException("Transition must specify actions for all tapes");
        }

        for (char symbol : transition.getReadSymbols()) {
            if (!tapeAlphabet.contains(symbol)) {
                throw new IllegalArgumentException("Invalid read symbol in transition: " + symbol);
            }
        }

        for (char symbol : transition.getWriteSymbols()) {
            if (!tapeAlphabet.contains(symbol)) {
                throw new IllegalArgumentException("Invalid write symbol in transition: " + symbol);
            }
        }

        String readKey = String.valueOf(transition.getReadSymbols());
        transitionFunction
                .computeIfAbsent(transition.getCurrentState(), k -> new HashMap<>())
                .put(readKey, transition);
    }

    /**
     * Loads input string onto the first tape and resets the machine
     */
    public void loadInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        // Validate input alphabet - only check non-empty strings
        if (!input.isEmpty()) {
            for (char c : input.toCharArray()) {
                if (!alphabet.contains(c)) {
                    throw new IllegalArgumentException("Invalid character in input: " + c);
                }
            }
        }

        reset();
        tapes[0].loadInput(input);
    }

    /**
     * Loads input string onto a specific tape without resetting other tapes
     */
    public void loadInput(String input, int tapeIndex) {
        if (tapeIndex < 0 || tapeIndex >= numTapes) {
            throw new IllegalArgumentException("Invalid tape index: " + tapeIndex);
        }

        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        // Validate input alphabet - only check non-empty strings
        if (!input.isEmpty()) {
            for (char c : input.toCharArray()) {
                if (!alphabet.contains(c)) {
                    throw new IllegalArgumentException("Invalid character in input: " + c);
                }
            }
        }

        // Clear and load input to specific tape only
        tapes[tapeIndex].clear();
        tapes[tapeIndex].loadInput(input);
    }

    /**
     * Resets the machine to its initial state
     */
    public void reset() {
        currentState = initialState;
        isRunning = false;
        isAccepted = false;
        isRejected = false;
        stepCount = 0;
        for (Tape tape : tapes) {
            tape.clear();
        }
    }

    /**
     * Resets only the machine state without clearing tapes
     */
    public void resetState() {
        currentState = initialState;
        isRunning = false;
        isAccepted = false;
        isRejected = false;
        stepCount = 0;
        // Reset head positions to start
        for (Tape tape : tapes) {
            tape.resetHeadPosition();
        }
    }

    /**
     * Executes a single step of the machine
     * @return true if step was successful, false if machine halted
     */
    public boolean step() {
        if (!isRunning || isAccepted || isRejected) {
            return false;
        }

        // Read current symbols from all tapes
        char[] currentSymbols = new char[numTapes];
        for (int i = 0; i < numTapes; i++) {
            currentSymbols[i] = tapes[i].read();
        }

        MultiTapeTransition transition = getTransition(currentState, currentSymbols);

        if (transition == null) {
            isRejected = true;
            isRunning = false;
            return false;
        }

        // Execute transition on all tapes
        for (int i = 0; i < numTapes; i++) {
            tapes[i].write(transition.getWriteSymbols()[i]);
            tapes[i].moveHead(transition.getDirections()[i]);
        }

        currentState = transition.getNextState();
        stepCount++;

        // Check for halting conditions
        if (acceptStates.contains(currentState)) {
            isAccepted = true;
            isRunning = false;
        } else if (rejectStates.contains(currentState)) {
            isRejected = true;
            isRunning = false;
        } else if (stepCount >= MAX_STEPS) {
            isRejected = true;
            isRunning = false;
        }

        return true;
    }

    /**
     * Runs the machine until it halts
     */
    public void run() {
        isRunning = true;
        while (isRunning && !isAccepted && !isRejected) {
            if (!step()) break;
        }
    }

    /**
     * Starts the machine execution
     */
    public void start() {
        if (!isAccepted && !isRejected) {
            isRunning = true;
        }
    }

    /**
     * Pauses the machine execution
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * Gets the transition for the given state and symbol combination
     */
    private MultiTapeTransition getTransition(String state, char[] symbols) {
        String readKey = String.valueOf(symbols);
        return transitionFunction.getOrDefault(state, new HashMap<>()).get(readKey);
    }

    /**
     * Gets the current machine state
     */
    public MachineState getMachineState() {
        if (isAccepted) return MachineState.ACCEPTED;
        if (isRejected) return MachineState.REJECTED;
        if (isRunning) return MachineState.RUNNING;
        return MachineState.READY;
    }

    /**
     * Checks if a transition exists for the given state and symbol combination
     */
    public boolean hasTransition(String state, char[] symbols) {
        return getTransition(state, symbols) != null;
    }

    /**
     * Gets all transitions from the given state
     */
    public Map<String, MultiTapeTransition> getTransitionsFromState(String state) {
        return new HashMap<>(transitionFunction.getOrDefault(state, new HashMap<>()));
    }

    // Getters
    public String getCurrentState() { return currentState; }
    public Tape[] getTapes() { return tapes; }
    public Tape getTape(int index) {
        if (index < 0 || index >= numTapes) {
            throw new IllegalArgumentException("Invalid tape index: " + index + ", numTapes: " + numTapes);
        }
        return tapes[index];
    }
    public int getNumTapes() { return numTapes; }
    public int getStepCount() { return stepCount; }
    public boolean isRunning() { return isRunning; }
    public boolean isAccepted() { return isAccepted; }
    public boolean isRejected() { return isRejected; }
    public char getBlankSymbol() { return blankSymbol; }
    public Set<String> getStates() { return new HashSet<>(states); }
    public Set<Character> getAlphabet() { return new HashSet<>(alphabet); }
    public Set<Character> getTapeAlphabet() { return new HashSet<>(tapeAlphabet); }
    public Set<String> getAcceptStates() { return new HashSet<>(acceptStates); }
    public Set<String> getRejectStates() { return new HashSet<>(rejectStates); }
    public String getInitialState() { return initialState; }
}