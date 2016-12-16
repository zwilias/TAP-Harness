package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.execution.testframework.TestConsoleProperties;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.CharsetToolkit;
import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.configuration.TapRunConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

class TapExecutionSession {
    private static final String FRAMEWORK_NAME = "TapRunner";

    private final Project project;
    private final TapRunConfiguration configuration;
    private final Executor executor;
    private final SMTRunnerConsoleView consoleView;
    private final ProcessHandler processHandler;

    TapExecutionSession(Project project, TapRunConfiguration configuration, Executor executor) throws ExecutionException {
        this.project = project;
        this.configuration = configuration;
        this.executor = executor;

        this.consoleView = createSMTRunnerConsoleView();
        this.processHandler = getOSProcessHandler();
    }

    SMTRunnerConsoleView getConsoleView() {
        return consoleView;
    }

    @NotNull
    private SMTRunnerConsoleView createSMTRunnerConsoleView() {
        TestConsoleProperties testConsoleProperties = new TapConsoleProperties(configuration, executor);
        TapConsoleView consoleView = new TapConsoleView(testConsoleProperties);
        Disposer.register(project, consoleView);
        SMTestRunnerConnectionUtil.initConsoleView(consoleView, FRAMEWORK_NAME);
        return consoleView;
    }

    @NotNull
    private ProcessHandler getOSProcessHandler() throws ExecutionException {
        GeneralCommandLine commandLine = createCommandLine();
        OSProcessHandler commandLineProcess = new KillableColoredProcessHandler(commandLine);
        ProcessTerminatedListener.attach(commandLineProcess);

        TapToSMTEventAdapter adapter = new TapToSMTEventAdapter(commandLineProcess);

        ProcessHandler smtProcessHandler = adapter.getProcessHandler();

        smtProcessHandler.addProcessListener(new ProcessAdapter() {
            @Override
            public void startNotified(ProcessEvent event) {
                adapter.forwardStartNotify();
            }
        });

        consoleView.attachToProcess(smtProcessHandler);
        ProcessTerminatedListener.attach(smtProcessHandler);

        return smtProcessHandler;
    }

    ProcessHandler getProcessHandler() {
        return this.processHandler;
    }

    @NotNull
    private GeneralCommandLine createCommandLine() {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setCharset(CharsetToolkit.UTF8_CHARSET);

        commandLine.setWorkDirectory(project.getBasePath());

        LinkedList<String> parts = new LinkedList<>(Arrays.asList(configuration.getCommand().split("\\s+")));

        commandLine.setExePath(parts.removeFirst());
        commandLine.addParameters(parts);

        return commandLine;
    }

    private static class TapConsoleProperties extends SMTRunnerConsoleProperties {
        TapConsoleProperties(TapRunConfiguration configuration, Executor executor) {
            super(configuration, FRAMEWORK_NAME, executor);
            setUsePredefinedMessageFilter(false);
            setIfUndefined(TestConsoleProperties.HIDE_PASSED_TESTS, false);
            setIfUndefined(TestConsoleProperties.HIDE_IGNORED_TEST, true);
            setIfUndefined(TestConsoleProperties.SCROLL_TO_SOURCE, true);
            setIfUndefined(TestConsoleProperties.SELECT_FIRST_DEFECT, true);
            setIdBasedTestTree(true);
            setPrintTestingStartedTime(false);
        }
    }
}
