package pushDownAutomataGraphics.ViewManager;

public enum ViewManagerFactory {
    INSTANCE;

    public <K> AbstractViewManager<K> createStackViewManager() {
        return new StackViewManager<>();
    }

    public <K> AbstractViewManager<K> createQueueViewManager() {
        return new QueueViewManager<>();
    }
}