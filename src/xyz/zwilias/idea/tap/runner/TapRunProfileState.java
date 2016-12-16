package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.zwilias.idea.tap.configuration.TapRunConfiguration;

public class TapRunProfileState implements RunProfileState {
    private final TapRunConfiguration configuration;

    public TapRunProfileState(TapRunConfiguration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
        TapExecutionSession session = new TapExecutionSession(configuration.getProject(), configuration, executor);
        SMTRunnerConsoleView consoleView = session.getConsoleView();
        ProcessHandler processHandler = session.getProcessHandler();


        return new DefaultExecutionResult(consoleView, processHandler);
    }
}
