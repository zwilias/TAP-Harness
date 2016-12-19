package xyz.zwilias.idea.tap.tree;

public class LeafNodeImpl<T> implements LeafNode<T> {
    private final T content;
    private final String id;
    private final BranchNode<T> parent;

    LeafNodeImpl(T content, String id, BranchNode<T> parent) {

        this.content = content;
        this.id = id;
        this.parent = parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BranchNode<T> getParent() {
        return parent;
    }

    @Override
    public T getContent() {
        return content;
    }
}
