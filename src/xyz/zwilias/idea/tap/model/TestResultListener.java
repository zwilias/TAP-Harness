package xyz.zwilias.idea.tap.model;

public interface TestResultListener<T extends TestResult> {
    void onTestResultAdded(T testResult);
}
