package xyz.zwilias.idea.tap.tree;

import xyz.zwilias.idea.tap.parser.Parser;
import xyz.zwilias.idea.tap.parser.event.Consumer;
import xyz.zwilias.idea.tap.parser.event.Event;

import java.util.*;

public class TapTree implements BranchNode<Event> {
    private int idCounter = 1;
    private final Map<String, Node<Event>> nodeMap;
    private TreeNodeAddedHandler treeNodeAddedHandler;
    private TreeNodeUpdatedHandler treeNodeUpdatedHandler;
    private AllDoneHandler allDoneHandler;

    public void onAllDone(AllDoneHandler allDone) {
        allDoneHandler = allDone;
    }

    public interface AllDoneHandler {
        void handleAllDone();
    }

    public interface TreeNodeAddedHandler {
        void handleTreeNodeAddedEvent(LeafNode<Event> node);
    }

    public interface TreeNodeUpdatedHandler {
        void handleTreeNodUpdatedEvent(LeafNode<Event> node);
    }

    public TapTree(Parser parser) {
        this.nodeMap = new HashMap<>();

        this.setUpListeners(parser);
    }

    @SuppressWarnings("unchecked")
    private void setUpListeners(Parser parser) {
        Consumer consumer = event -> {
            LeafNode<Event> node = new LeafNodeImpl<>(event, Integer.toString(idCounter), this);
            this.nodeMap.put(node.getId(), node);

            node.fireAdded();
        };

        parser
                .onTestPassed(consumer)
                .onTestTodo(consumer)
                .onTestSkipped(consumer)
                .onTestFailed(consumer)
                .onAllDone(event -> {
                    if (this.allDoneHandler != null) {
                        this.allDoneHandler.handleAllDone();
                    }
                });
    }

    public TapTree onTreeNodeAdded(TreeNodeAddedHandler treeNodeAddedHandler) {
        this.treeNodeAddedHandler = treeNodeAddedHandler;

        return this;
    }

    public TapTree onTreeNodeUpdated(TreeNodeUpdatedHandler treeNodeUpdatedHandler) {
        this.treeNodeUpdatedHandler = treeNodeUpdatedHandler;

        return this;
    }

    @Override
    public String getId() {
        return "0";
    }

    @Override
    public BranchNode<Event> getParent() {
        return null;
    }

    @Override
    public List<Node<Event>> getChildren() {
        return new LinkedList<>(nodeMap.values());
    }

    @Override
    public String getParentId() {
        return "0";
    }

    @Override
    public void fireAdded(LeafNode<Event> node) {
        if (treeNodeAddedHandler != null) {
            treeNodeAddedHandler.handleTreeNodeAddedEvent(node);
        }
    }

    @Override
    public void fireUpdated(LeafNode<Event> node) {
        if (treeNodeUpdatedHandler != null) {
            treeNodeUpdatedHandler.handleTreeNodUpdatedEvent(node);
        }
    }
}
