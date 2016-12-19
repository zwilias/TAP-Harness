package xyz.zwilias.idea.tap.tree;

import java.util.List;

public interface BranchNode<T> extends Node<T> {
    List<Node<T>> getChildren();
}
