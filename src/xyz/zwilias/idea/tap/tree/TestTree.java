package xyz.zwilias.idea.tap.tree;

import xyz.zwilias.idea.tap.parser.Parser;

public class TestTree extends TreeBranch {
    private NodeEventHandler nodeAddedHandler;
    private NodeEventHandler nodeUpdatedHandler;
    private VoidEventHandler onDoneHandler;

    public interface VoidEventHandler {
        void handleEvent();
    }

    public interface NodeEventHandler {
        void handleEvent(TreeNode event);
    }

    public TestTree() {
        super("root", "0", null);
    }

    public TestTree onNodeAdded(NodeEventHandler nodeAddedHandler) {
        this.nodeAddedHandler = nodeAddedHandler;
        return this;
    }

    public TestTree onNodeUpdated(NodeEventHandler nodeUpdatedHandler) {
        this.nodeUpdatedHandler = nodeUpdatedHandler;
        return this;
    }

    public TestTree onDone(VoidEventHandler onDoneHandler) {
        this.onDoneHandler = onDoneHandler;
        return this;
    }

    @Override
    public String getParentId() {
        return "0";
    }

    @Override
    protected void onNodeAdded(TreeNode node) {
        if (this.nodeAddedHandler != null) {
            this.nodeAddedHandler.handleEvent(node);
        }
    }

    @Override
    protected void onNodeUpdated(TreeNode node) {
        if (this.nodeUpdatedHandler != null) {
            this.nodeUpdatedHandler.handleEvent(node);
        }
    }

    public void attachToParser(Parser parser) {
        parser
                .onAllDone(event -> {
                    if (this.nodeAddedHandler != null) {
                        this.onDoneHandler.handleEvent();
                    }
                })
                .onTestPassed(event -> {
                    this.getLeafOrCreate(event.getTestNumber(), event.getName()).setPassed();
                })
                .onTestFailed(event -> {
                    this.getLeafOrCreate(event.getTestNumber(), event.getName()).setFailed();
                })
                .onTestSkipped(event -> {
                    this.getLeafOrCreate(event.getTestNumber(), event.getName()).setSkipped();
                })
                .onTestTodo(event -> {
                    this.getLeafOrCreate(event.getTestNumber(), event.getName()).setTodo();
                });
    }
}
