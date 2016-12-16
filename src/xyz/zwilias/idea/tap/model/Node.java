package xyz.zwilias.idea.tap.model;

public interface Node {
    String getNodeId();
    Node getParent();
    void setParent(Node parent);

    default String getParentNodeId() {
        return getParent().getNodeId();
    }
}
