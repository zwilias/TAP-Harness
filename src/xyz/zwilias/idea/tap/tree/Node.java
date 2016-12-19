package xyz.zwilias.idea.tap.tree;

public interface Node<T> {
    String getId();
    BranchNode<T> getParent();

    default String getParentId() {
        return getParent().getId();
    }

    default void fireAdded(LeafNode<T> node) {
        getParent().fireAdded(node);
    }

    default void fireUpdated(LeafNode<T> node) {
        getParent().fireUpdated(node);
    }
}
