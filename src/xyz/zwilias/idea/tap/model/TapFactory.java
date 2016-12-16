package xyz.zwilias.idea.tap.model;

public interface TapFactory<T extends TestResult> {
    T createTestResult(TestResult.Status status, Integer testNumber);
    T createTestResult(TestResult.Status status, Integer testNumber, String description);

    TestSet<T> createTestSet();
}
