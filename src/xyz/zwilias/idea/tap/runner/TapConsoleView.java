package xyz.zwilias.idea.tap.runner;

import com.intellij.execution.testframework.TestConsoleProperties;
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;

class TapConsoleView extends SMTRunnerConsoleView {
    TapConsoleView(TestConsoleProperties consoleProperties) {
        super(consoleProperties);
    }
}
