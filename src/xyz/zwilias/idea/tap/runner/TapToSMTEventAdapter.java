package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.process.*;
import com.intellij.execution.testframework.sm.ServiceMessageBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import xyz.zwilias.idea.tap.parser.Parser;
import xyz.zwilias.idea.tap.tree.TreeBranch;
import xyz.zwilias.idea.tap.tree.TreeLeaf;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TapToSMTEventAdapter extends ProcessAdapter {
    private final static Logger LOG = Logger.getInstance(TapToSMTEventAdapter.class);

    private final ProcessHandler commandLineProcess;
    private final ProcessHandler smtProcess;
    private final OutputStreamWriter parserStream;
    private final Parser parser;
    private final List<TreeBranch> createdBranches = new LinkedList<>();

    TapToSMTEventAdapter(ProcessHandler commandLineProcess) throws IOException {
        this.commandLineProcess = commandLineProcess;
        this.smtProcess = new NopProcessHandler();

        PipedOutputStream outputStream = new PipedOutputStream();
        this.parserStream = new OutputStreamWriter(outputStream);
        this.parser = Parser.parseStream(new PipedInputStream(outputStream));

        this.parser.getTreeModel()
                .onDone(this::finishTree)
                .onNodeAdded(node -> {
                    ServiceMessageBuilder builder;

                    if (node instanceof TreeBranch) {
                        createdBranches.add((TreeBranch) node);
                        builder = ServiceMessageBuilder.testSuiteStarted(node.getDescription());
                    } else {
                        builder = ServiceMessageBuilder.testStarted(node.getDescription());
                    }

                    builder
                            .addAttribute("parentNodeId", node.getParentId())
                            .addAttribute("nodeId", node.getId());

                    smtProcess.notifyTextAvailable(
                            builder.toString() + "\n",
                            new Key("stdout")
                    );
                })
                .onNodeUpdated(node -> {
                    if (!(node instanceof TreeLeaf)) {
                        return;
                    }

                    TreeLeaf leafNode = (TreeLeaf) node;
                    ServiceMessageBuilder builder;

                    switch (leafNode.getStatus()) {
                        case SKIPPED:
                        case TODO:
                            builder = ServiceMessageBuilder.testIgnored(node.getDescription());
                            break;
                        case FAILED:
                            builder = ServiceMessageBuilder
                                    .testFailed(node.getDescription())
                                    .addAttribute("message", "Todo: add diagnostics.");
                            break;
                        case PASSED:
                        default:
                            builder = ServiceMessageBuilder.testFinished(node.getDescription());
                    }

                    String message = builder
                            .addAttribute("nodeId", node.getId())
                            .addAttribute("parentNodeId", node.getParentId())
                            .toString();

                    smtProcess.notifyTextAvailable(
                            message + "\n",
                            new Key("stdout")
                    );
                })
        ;

        commandLineProcess.addProcessListener(this);
    }

    private void finishTree() {
        LOG.info("All done!");

        Collections.reverse(createdBranches);
        createdBranches.forEach(branch -> {
            smtProcess.notifyTextAvailable(
                    ServiceMessageBuilder
                        .testSuiteFinished(branch.getDescription())
                        .addAttribute("nodeId", branch.getId())
                        .addAttribute("parentNodeId", branch.getParentId())
                        .toString() + "\n",
                    new Key("output")
            );
        });

        smtProcess.destroyProcess();
    }

    Runnable getRunnable() {
        return parser;
    }

    void forwardStartNotify() {
        commandLineProcess.startNotify();
    }

    @Override
    public void processTerminated(ProcessEvent event) {
        try {
            parserStream.close();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    @Override
    public void onTextAvailable(ProcessEvent processEvent, Key key) {
        try {
            parserStream.write(processEvent.getText() + "\n");
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    ProcessHandler getProcessHandler() {
        return this.smtProcess;
    }
}
