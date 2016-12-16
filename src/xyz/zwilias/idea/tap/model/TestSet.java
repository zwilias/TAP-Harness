package xyz.zwilias.idea.tap.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TestSet<T extends TestResult> {
    void addTestResult(T testResult);
    void addTestResultListener(TestResultListener<T> testResultListener);

    @NotNull
    List<T> getTestResults();

    default int getNumberOfTests() {
        return getTestResults().size();
    }
}
