package xyz.zwilias.idea.tap.tree;

import com.intellij.util.containers.MultiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class TreeBranch implements TreeNode {
    @NotNull
    private final String description;
    @NotNull
    private final String id;
    @Nullable
    private final TreeBranch parent;

    private final MultiMap<String, TreeNode> nodeMap = new MultiMap<>();

    public TreeBranch(@NotNull String description, @NotNull String id, @Nullable TreeBranch parent) {
        this.description = description;
        this.id = id;
        this.parent = parent;
    }

    public TreeBranch getBranchOrCreate(String id, String description) {
        if (nodeMap.containsKey(id) && nodeMap.get(id).stream().anyMatch(node -> node instanceof TreeBranch)) {
            return (TreeBranch) nodeMap.get(id).stream().filter(node -> node instanceof TreeBranch).findFirst().get();
        } else {
            TreeBranch branch = new TreeBranch(description, id, this);
            onNodeAdded(branch);

            return branch;
        }
    }

    public TreeLeaf getLeafOrCreate(String id, String description) {
        if (nodeMap.containsKey(id) && nodeMap.get(id).stream().anyMatch(node -> node instanceof TreeLeaf)) {
            return (TreeLeaf) nodeMap.get(id).stream().filter(node -> node instanceof TreeLeaf).findFirst().get();
        } else {
            TreeLeaf branch = new TreeLeaf(description, id, this);

            onNodeAdded(branch);

            return branch;
        }
    }

    @Override
    public TreeBranch getParent() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public List<TreeNode> getNodes() {
        return new LinkedList<>(nodeMap.values());
    }

    protected void onNodeAdded(TreeNode node) {
        this.getParent().onNodeAdded(node);
    }

    protected void onNodeUpdated(TreeNode node) {
        this.getParent().onNodeUpdated(node);
    }
}
