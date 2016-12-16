package xyz.zwilias.idea.tap.model;

public class TapFactoryImpl implements TapFactory<TestResultImpl> {

    @Override
    public TestResultImpl createTestResult(TestResult.Status status, Integer testNumber) {
        return new TestResultImpl(status, testNumber);
    }

    @Override
    public TestResultImpl createTestResult(TestResult.Status status, Integer testNumber, String description) {
        return new TestResultImpl(status, testNumber, description);
    }

    @Override
    public TestSet<TestResultImpl> createTestSet() {
        return new TestSetImpl();
    }
}
