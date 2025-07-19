package turingMachine;

/**
 * Enumeration representing the possible states of the Turing Machine execution.
 */
public enum MachineState {
    READY,      // Machine is ready to start or has been reset
    RUNNING,    // Machine is currently executing
    ACCEPTED,   // Machine has reached an accept state
    REJECTED,   // Machine has reached a reject state or no valid transition
    ERROR       // Machine encountered an error during execution
}