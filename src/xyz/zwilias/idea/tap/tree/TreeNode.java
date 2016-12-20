package xyz.zwilias.idea.tap.tree;

public interface TreeNode {
    TreeBranch getParent();

    String getDescription();

    String getId();

    default String getParentId() {
        return getParent().getId();
    }
}
