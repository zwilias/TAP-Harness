package xyz.zwilias.idea.tap.tree;

import org.jetbrains.annotations.NotNull;

public class TreeLeaf implements TreeNode {
    private final String description;
    private final String id;
    private final TreeBranch parent;

    public enum Status {
        DECLARED, PASSED, FAILED, SKIPPED, TODO;
    }

    private Status status = Status.DECLARED;

    public TreeLeaf(String description, String id, @NotNull TreeBranch parent) {
        this.description = description;
        this.id = id;
        this.parent = parent;
    }

    public Status getStatus() {
        return status;
    }

    public void setPassed() {
        status = Status.PASSED;
        this.fireUpdate();
    }

    public void setFailed() {
        status = Status.FAILED;
        this.fireUpdate();
    }

    public void setSkipped() {
        status = Status.SKIPPED;
        this.fireUpdate();
    }

    public void setTodo() {
        status = Status.TODO;
        this.fireUpdate();
    }

    @Override
    public TreeBranch getParent() {
        return parent;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getId() {
        return id;
    }

    private void fireUpdate() {
        this.getParent().onNodeUpdated(this);
    }
}
