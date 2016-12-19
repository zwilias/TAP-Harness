package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.process.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import xyz.zwilias.idea.tap.parser.Parser;
import xyz.zwilias.idea.tap.parser.event.Event;
import xyz.zwilias.idea.tap.parser.event.TestEvent;
import xyz.zwilias.idea.tap.tree.TapTree;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TapToSMTEventAdapter extends ProcessAdapter {
    private final static Logger LOG = Logger.getInstance(TapToSMTEventAdapter.class);

    private final ProcessHandler commandLineProcess;
    private final ProcessHandler smtProcess;
    private final OutputStreamWriter parserStream;
    private final Parser parser;

    TapToSMTEventAdapter(ProcessHandler commandLineProcess) throws IOException {
        this.commandLineProcess = commandLineProcess;
        this.smtProcess = new NopProcessHandler();

        PipedOutputStream outputStream = new PipedOutputStream();
        this.parserStream = new OutputStreamWriter(outputStream);
        this.parser = Parser.parseStream(new PipedInputStream(outputStream));

        (new TapTree(parser)).onTreeNodeAdded(node -> {
            Event e = node.getContent();
            if (e instanceof TestEvent) {
                LOG.info(((TestEvent) e).getTapLine());
            }
        }).onAllDone(this::finishTree);
        commandLineProcess.addProcessListener(this);
    }

    private void finishTree() {
        LOG.info("All done!");
    }

    Runnable getRunnable() {
        return parser;
    }

    void forwardStartNotify() {
        commandLineProcess.startNotify();
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
