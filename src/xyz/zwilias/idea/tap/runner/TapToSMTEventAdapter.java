package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.process.*;
import com.intellij.execution.testframework.sm.ServiceMessageBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import xyz.zwilias.idea.tap.model.TestResult;
import xyz.zwilias.idea.tap.model.TestSuiteImpl;
import xyz.zwilias.idea.tap.parser.TapParser;

import java.util.LinkedList;
import java.util.List;

public class TapToSMTEventAdapter extends ProcessAdapter {
    private final static Logger LOG = Logger.getInstance(TapToSMTEventAdapter.class);

    private final ProcessHandler commandLineProcess;
    private final ProcessHandler smtProcess;
    private final TapParser tapParser;
    private final List<TestSuiteImpl> suites = new LinkedList<>();

    TapToSMTEventAdapter(ProcessHandler commandLineProcess) {
        this.commandLineProcess = commandLineProcess;
        this.smtProcess = new NopProcessHandler();

        this.tapParser = new TapParser(testResult -> {
            buildServiceMessageBuildersForResult(testResult).forEach(builder -> {
                builder.addAttribute("parentNodeId", testResult.getParentNodeId())
                        .addAttribute("nodeId", testResult.getNodeId());

                smtProcess.notifyTextAvailable(builder.toString().concat("\n"), new Key<String>("stdout"));
            });
        }, suite -> {
            suites.add(suite);

            ServiceMessageBuilder builder = ServiceMessageBuilder
                    .testSuiteStarted(suite.getName())
                    .addAttribute("parentNodeId", suite.getParentNodeId())
                    .addAttribute("nodeId", suite.getNodeId());

            smtProcess.notifyTextAvailable(builder.toString().concat("\n"), new Key<String>("stdout"));
        });

        commandLineProcess.addProcessListener(this);
    }

    private List<ServiceMessageBuilder> buildServiceMessageBuildersForResult(final TestResult testResult) {
        List<ServiceMessageBuilder> result = new LinkedList<>();
        String description = testResult.getDescription();
        if (description == null) {
            description = "Test ".concat(testResult.getTestNumber().toString());
        }

        result.add(ServiceMessageBuilder.testStarted(description));

        if (testResult.getStatus().equals(TestResult.Status.OK)) {
            result.add(ServiceMessageBuilder.testFinished(description));
        } else {
            result.add(ServiceMessageBuilder.testFailed(description).addAttribute("message", "Test failed…"));
        }

        return result;
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

        suites.forEach(suite -> {
            ServiceMessageBuilder builder = ServiceMessageBuilder
                    .testSuiteFinished(suite.getName())
                    .addAttribute("parentNodeId", suite.getParentNodeId())
                    .addAttribute("nodeId", suite.getNodeId());

            smtProcess.notifyTextAvailable(builder.toString().concat("\n"), new Key<String>("stdout"));
        });

        smtProcess.destroyProcess();
    }

    @Override
    public void onTextAvailable(ProcessEvent processEvent, Key key) {
        if ("stdout".equals(key.toString())) {
            tapParser.parseLine(processEvent.getText());
        }
    }

    ProcessHandler getProcessHandler() {
        return this.smtProcess;
    }
}
