package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.process.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;

public class TapToSMTEventAdapter extends ProcessAdapter {
    private final static Logger LOG = Logger.getInstance(TapToSMTEventAdapter.class);

    private final ProcessHandler commandLineProcess;
    private final ProcessHandler smtProcess;

    TapToSMTEventAdapter(ProcessHandler commandLineProcess) {
        this.commandLineProcess = commandLineProcess;
        this.smtProcess = new NopProcessHandler();

        commandLineProcess.addProcessListener(this);
    }

    void forwardStartNotify() {
        commandLineProcess.startNotify();
    }

    @Override
    public void processTerminated(ProcessEvent processEvent) {
        LOG.info("ProcessTerminated");

        smtProcess.destroyProcess();
    }

    @Override
    public void processWillTerminate(ProcessEvent processEvent, boolean b) {
        LOG.info("ProcessWillTerminate - ".concat(b ? "true" : "false"));

        smtProcess.destroyProcess();
    }

    @Override
    public void onTextAvailable(ProcessEvent processEvent, Key key) {
    }

    ProcessHandler getProcessHandler() {
        return this.smtProcess;
    }
}
