package xyz.zwilias.idea.tap.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestResultImpl implements TestResult, Node {
    private final Status status;
    private final Integer testNumber;
    private String description;
    private Node parent;

    TestResultImpl(@NotNull Status status, @NotNull Integer testNumber) {
        this(status, testNumber, null);
    }

    TestResultImpl(@NotNull Status status, @NotNull Integer testNumber, @Nullable String description) {
        this.status = status;
        this.testNumber = testNumber;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Override
    public Status getStatus() {
        return status;
    }

    @NotNull
    @Override
    public Integer getTestNumber() {
        return testNumber;
    }

    @Nullable
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getNodeId() {
        return testNumber.toString();
    }

    @Override
    public Node getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }
}
