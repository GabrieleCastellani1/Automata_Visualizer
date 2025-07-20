package pushDownAutomata;

import pushDownAutomataGraphics.ViewManager.AbstractViewManager;
import pushDownAutomataGraphics.ViewManager.ViewManagerFactory;
import util.Util;

import java.util.*;

public class PushDownAutomata <K> {
    public TreeNode<K> viewManagers;
    private Queue<K> inputQueue;
    private Stack<K> inititalStack;
    private AbstractViewManager<K> queueViewManager;
    // NPDA components
    private final Map<TransitionKey<K>, List<TransitionValue<K>>> transitionMatrix;
    private final Set<Integer> states;
    private final Set<Integer> acceptStates;
    private int initialState;
    private final ViewManagerFactory viewManagerFactory;

    // Transition key: (current_state, input_symbol, stack_top)
    public static class TransitionKey<K> {
        public final int state;
        public final K input;  // null for epsilon transitions
        public final K stackTop;

        public TransitionKey(int state, K input, K stackTop) {
            this.state = state;
            this.input = input;
            this.stackTop = stackTop;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TransitionKey<?> that)) return false;
            return state == that.state &&
                    Objects.equals(input, that.input) &&
                    Objects.equals(stackTop, that.stackTop);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, input, stackTop);
        }
    }

    // Transition value: (new_state, symbols_to_push)
    public static class TransitionValue<K> {
        public final int newState;
        public final List<K> pushSymbols;  // empty list means pop only

        public TransitionValue(int newState, List<K> pushSymbols) {
            this.newState = newState;
            this.pushSymbols = pushSymbols != null ? pushSymbols : new ArrayList<>();
        }
    }

    // Configuration for NPDA computation
    private static class Configuration<K> {
        public final int state;
        public final Queue<K> remainingInput;
        public final Stack<K> stack;
        private final int inputIndex;

        public Configuration(int state, Queue<K> input, Stack<K> stack, int inputIndex) {
            this.state = state;
            this.remainingInput = input;
            this.stack = stack;
            this.inputIndex = inputIndex;
        }
    }

    public PushDownAutomata() {
        this.transitionMatrix = new HashMap<>();
        this.states = new HashSet<>();
        this.acceptStates = new HashSet<>();
        this.viewManagerFactory = ViewManagerFactory.INSTANCE;
        this.inputQueue = new LinkedList<>();
        this.queueViewManager = viewManagerFactory.createQueueViewManager();
        this.inititalStack = new Stack<>();
        this.viewManagers = new TreeNode<>(new StackViewData<>(inititalStack, viewManagerFactory.createStackViewManager()));
    }

    // Setup methods
    public void setInitialState(int state) {
        this.initialState = state;
        this.states.add(state);
    }

    public void addAcceptState(int state) {
        this.acceptStates.add(state);
        this.states.add(state);
    }

    // Transition matrix methods
    public void addTransition(int fromState, K input, K stackTop, int toState, List<K> pushSymbols) {
        TransitionKey<K> key = new TransitionKey<>(fromState, input, stackTop);
        transitionMatrix.computeIfAbsent(key, k -> new ArrayList<>())
                .add(new TransitionValue<>(toState, pushSymbols));
    }

    public void addEpsilonTransition(int fromState, K stackTop, int toState, List<K> pushSymbols) {
        addTransition(fromState, null, stackTop, toState, pushSymbols);
    }

    public Collection<AbstractViewManager<K>> getViewManagers(){
        List<AbstractViewManager<K>> managers = new ArrayList<>();
        managers.add(queueViewManager);
        managers.addAll(viewManagers.getAllLeaves()
                .stream()
                .sorted(Comparator.comparingInt(TreeNode::getHeight))
                .map(n -> n.getData().viewManager)
                .toList());
        return managers;
    }

    /**
     * The input queue is always painted as the first array in the panel
     * @param input: symbol to add to the input queue
     */
    public void addInput(K input){
        inputQueue.add(input);
        queueViewManager.addSquare(input);
    }

    public void addStack(K input){
        inititalStack.push(input);
        AbstractViewManager<K> viewManager = viewManagerFactory.createStackViewManager();
        inititalStack.forEach(viewManager::addSquare);
        viewManagers.findNode(inititalStack).setData(new StackViewData<>(inititalStack, viewManager));
    }

    public void clearInput(){
        inputQueue = new LinkedList<>();
        queueViewManager = viewManagerFactory.createQueueViewManager();
    }

    public void clearStack(){
        this.inititalStack = new Stack<>();
        this.viewManagers = new TreeNode<>(new StackViewData<>(inititalStack, viewManagerFactory.createStackViewManager()));
    }

    public boolean evaluateString() {
        return evaluateNonDeterministic(new Configuration<>(initialState, inputQueue, inititalStack, 0));
    }

    private boolean evaluateNonDeterministic(Configuration<K> config) {
        Queue<Configuration<K>> configurations = new LinkedList<>();
        configurations.add(config);

        while (!configurations.isEmpty()) {
            Configuration<K> current = configurations.poll();

            // Check accept condition
            if (acceptStates.contains(current.state) && current.remainingInput.isEmpty()) {
                Util.waitAction(1000);
                clearInput();
                clearStack();
                return true;
            }

            K inputSymbol = current.remainingInput.isEmpty() ? null : current.remainingInput.peek();

            if (inputSymbol != null) {
                queueViewManager.removeAllPins();
                queueViewManager.addPin(current.inputIndex);
                Util.waitAction(1000);
            }

            if(!current.stack.isEmpty()) {
                // Try all possible transitions
                viewManagers.findNode(current.stack).getData().viewManager.removeAllRect();
                Util.waitAction(1000);
                viewManagers.findNode(current.stack).getData().viewManager.highlightElement(current.stack.size() - 1);
                Util.waitAction(1000);
            }

            K stackTop = current.stack.isEmpty() ? null : current.stack.pop();
            System.out.println("the removed element from the stack is: " + stackTop);

            // Try input transitions
            TransitionKey<K> key = new TransitionKey<>(current.state, inputSymbol, stackTop);
            List<TransitionValue<K>> transitions = transitionMatrix.get(key);
            if (transitions != null) {
                for (TransitionValue<K> transition : transitions) {
                    configurations.add(createNextConfiguration(current, transition, true));
                }
                Util.waitAction(1000);
            }
            // Try epsilon transitions
            TransitionKey<K> epsilonKey = new TransitionKey<>(current.state, null, stackTop);
            List<TransitionValue<K>> epsilonTransitions = transitionMatrix.get(epsilonKey);
            if (epsilonTransitions != null) {
                for (TransitionValue<K> transition : epsilonTransitions) {
                    configurations.add(createNextConfiguration(current, transition, false));
                }
                Util.waitAction(1000);
            }
        }
        Util.waitAction(1000);
        clearInput();
        clearStack();
        return false;
    }

    private Configuration<K> createNextConfiguration(Configuration<K> current,
                                                     TransitionValue<K> transition,
                                                     boolean consumeInput) {
        Queue<K> newInput = new LinkedList<>(current.remainingInput);
        int inputIndex = current.inputIndex;
        if (consumeInput && !newInput.isEmpty()) {
            newInput.poll();
            inputIndex++;
        }

        Stack<K> newStack = new Stack<>();
        newStack.addAll(current.stack);
        AbstractViewManager<K> newViewManager = viewManagerFactory.createStackViewManager();

        for (K symbol : transition.pushSymbols) {
            newStack.push(symbol);
        }

        newStack.forEach(newViewManager::addSquare);
        viewManagers.findNode(current.stack).addChild(new StackViewData<>(newStack, newViewManager));

        if(!transition.pushSymbols.isEmpty()) {
            newViewManager.highlightArea(0, transition.pushSymbols.size() - 1);
        }

        return new Configuration<>(transition.newState, newInput, newStack, inputIndex);
    }

    // Utility methods
    public Map<TransitionKey<K>, List<TransitionValue<K>>> getTransitionMatrix() {
        return transitionMatrix;
    }

    public Set<Integer> getStates() {
        return states;
    }

    public Set<Integer> getAcceptStates() {
        return acceptStates;
    }
}