package pushDownAutomata;

import java.util.*;

public class TreeNode<K> {
    private StackViewData<K> data;
    private TreeNode<K> parent;
    private final List<TreeNode<K>> children;
    private int level;

    public TreeNode(StackViewData<K> data) {
        this.data = data;
        this.children = new ArrayList<>();
        this.level = 0;
    }

    public TreeNode<K> addChild(StackViewData<K> childData) {
        TreeNode<K> child = new TreeNode<>(childData);
        child.parent = this;
        child.level = this.level + 1;
        this.children.add(child);
        return child;
    }

    public TreeNode<K> addChild(TreeNode<K> child) {
        child.parent = this;
        child.level = this.level + 1;
        this.children.add(child);
        return child;
    }

    public void removeChild(TreeNode<K> child) {
        this.children.remove(child);
        child.parent = null;
    }

    public void removeChild(Stack<K> childData) {
        children.removeIf(child -> Objects.equals(child.data.stack, childData));
    }

    // Print tree by levels (breadth-first)
    public void printByLevels() {
        Queue<TreeNode<K>> queue = new LinkedList<>();
        queue.offer(this);

        int currentLevel = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.println("=== LEVEL " + currentLevel + " ===");

            for (int i = 0; i < levelSize; i++) {
                TreeNode<K> node = queue.poll();
                assert node != null;
                System.out.println("Node " + i + ": " + node.data);

                // Add children to queue for next level
                queue.addAll(node.children);
            }

            currentLevel++;
            System.out.println();
        }
    }

    // Print with indentation to show hierarchy
    public void printWithIndentation() {
        printWithIndentation(0);
    }

    private void printWithIndentation(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "├─ " + data);

        for (TreeNode<K> child : children) {
            child.printWithIndentation(depth + 1);
        }
    }

    // Get all nodes at a specific level
    public List<TreeNode<K>> getNodesAtLevel(int targetLevel) {
        List<TreeNode<K>> result = new ArrayList<>();

        if (targetLevel == 0) {
            result.add(this);
            return result;
        }

        Queue<TreeNode<K>> queue = new LinkedList<>();
        queue.offer(this);

        while (!queue.isEmpty()) {
            TreeNode<K> node = queue.poll();

            if (node.level == targetLevel) {
                result.add(node);
            } else if (node.level < targetLevel) {
                queue.addAll(node.children);
            }
        }

        return result;
    }

    // Get path from root to this node
    public List<TreeNode<K>> getPathFromRoot() {
        List<TreeNode<K>> path = new ArrayList<>();
        TreeNode<K> current = this;

        while (current != null) {
            path.add(0, current); // Add at beginning for root-to-node order
            current = current.parent;
        }

        return path;
    }

    // Get all leaf nodes (nodes with no children)
    public List<TreeNode<K>> getAllLeaves() {
        List<TreeNode<K>> leaves = new ArrayList<>();
        collectLeaves(leaves);
        return leaves;
    }

    private void collectLeaves(List<TreeNode<K>> leaves) {
        if (isLeaf()) {
            leaves.add(this);
        } else {
            for (TreeNode<K> child : children) {
                child.collectLeaves(leaves);
            }
        }
    }

    // Find node by data
    public TreeNode<K> findNode(Stack<K> searchData) {
        if (this.data.stack == searchData) {
            return this;
        }

        for (TreeNode<K> child : children) {
            TreeNode<K> found = child.findNode(searchData);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    // Get tree height
    public int getHeight() {
        if (isLeaf()) {
            return 0;
        }

        int maxChildHeight = 0;
        for (TreeNode<K> child : children) {
            maxChildHeight = Math.max(maxChildHeight, child.getHeight());
        }

        return maxChildHeight + 1;
    }

    // Get number of nodes in subtree
    public int getSize() {
        int size = 1; // Count this node
        for (TreeNode<K> child : children) {
            size += child.getSize();
        }
        return size;
    }

    // Getters
    public StackViewData<K> getData() { return data; }
    public void setData(StackViewData<K> data) { this.data = data; }
    public TreeNode<K> getParent() { return parent; }
    public List<TreeNode<K>> getChildren() { return new ArrayList<>(children); }
    public boolean isLeaf() { return children.isEmpty(); }
    public boolean isRoot() { return parent == null; }
    public int getLevel() { return level; }
    public int getChildCount() { return children.size(); }

    @Override
    public String toString() {
        return "TreeNode{data=" + data + ", level=" + level + ", children=" + children.size() + "}";
    }
}