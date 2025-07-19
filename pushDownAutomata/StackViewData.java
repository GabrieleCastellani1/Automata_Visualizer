package pushDownAutomata;

import pushDownAutomataGraphics.ViewManager.AbstractViewManager;

import java.util.Stack;

class StackViewData<K> {
    Stack<K> stack;
    AbstractViewManager<K> viewManager;

    public StackViewData(Stack<K> stack, AbstractViewManager<K> viewManager) {
        this.stack = stack;
        this.viewManager = viewManager;
    }

    @Override
    public String toString() {
        return "Stack: " + stack + ", ViewManager: " + viewManager;
    }
}