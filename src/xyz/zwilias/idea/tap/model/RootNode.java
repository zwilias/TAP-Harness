package xyz.zwilias.idea.tap.model;

public interface RootNode extends Node {
    @Override
    default String getNodeId() {
        return "0";
    }

    @Override
    default Node getParent() {
        return null;
    }

    @Override
    default String getParentNodeId() {
        return "0";
    }

    @Override
    default void setParent(Node parent) {
        throw new IllegalAccessError("Cannot set parent of rootnode");
    }
}
