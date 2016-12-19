package xyz.zwilias.idea.tap.tree;

public interface LeafNode<T> extends Node<T> {
    T getContent();
    default void fireAdded() {
        getParent().fireAdded(this);
    }

    default void fireUpdated() {
        getParent().fireUpdated(this);
    }
}
